This is a sample project for demonstrating how to use PhoenixMapReduceUtil to Upsert data and at the same time, show case a small issue with it.

Problem:
PhoenixMapReduceUtil Upserts with relative earlier timestamp (compared to latest data timestamp in table) slower by 25x after stats collection

Description of the problem:
1) We face a 25x slow down when go back in time to load data in a table (when specific timestamps set on connections during upserts)
2) set phoenix.stats.useCurrentTime=false (and phoenix.stats.guidepost.per.region 1) which at least makes the forward timestamps upserts perform correctly
3) From what I can tell from the phoenix source code, logs attached and jstacks from the region servers --
    we continuously try to lookup/build the definition of the table (instead of caching it) when we have client timestamp earlier than the last modified timestamp of the table in stats
4) To reproduce, create a table with timestamp=100, and load 10M rows with PhoenixMapReduceUtil and timestamps=1447574400000,1448092800000, wait for 20 mins (15+ min, phoenix.stats.updateFrequency is 15mins)
    After 20 mins, load 10M rows with a earlier timestamp compared to the latest data (timestamp=1447660800000) and observe the 25x slowness, after this once again load a forward timestamp 1448179200000 and observe the quickness
5) I was not able to reproduce this issue with simple multi threaded upserts from a jdbc connection, with simple multi threaded upserts the stats table never gets populated unlike PhoenixMapReduceUtil

We are trying to use phoenix as a cache store to do analytics with the last 60 days of data, a total of about 1.5 billion rows
The table has a composite key and the data arrives in different times from different sources, so it is easier to maintain the timestamps of the data and expire the data automatically, this performance makes a difference between inserting the data in 10 mins versus 2 hours, 2 hours for data inserts blocking up the cluster that we have.
We are even talking about our use cases in the upcoming strata conference in March..  (Thanks to the excellent community)

Steps to reproduce:
Source code is available in (https://github.com/athangamani/phoenix_mapreduce_load) and the jar the source code produces is attached which is readily runnable
1) We use the following params to keep the stats collection happy to isolate the specific issue
     phoenix.stats.useCurrentTime false
     phoenix.stats.guidepost.per.region 1
2) Create a table in phoenix
   Run the following main class from the project.. (StatPhoenixTableCreationTest).. It will create a table with timestamp=100
		CREATE TABLE stat_table (
			pk1 VARCHAR NOT NULL,
			pk2 VARCHAR NOT NULL,
			pk3 UNSIGNED_LONG NOT NULL,
			 stat1 UNSIGNED_LONG,
			 stat2 UNSIGNED_LONG,
			 stat3 UNSIGNED_LONG,
			 CONSTRAINT pk PRIMARY KEY (pk1, pk2, pk3)
		) SALT_BUCKETS=32, COMPRESSION='LZ4'
3) Open the code base to look at the sample for PhoenixMapReduceUtil.. With DBWritable..
4) Within the codebase, we get phoenix connection for the mappers using the following settings in order to have a fixed client timestamp
     conf.set(PhoenixRuntime.CURRENT_SCN_ATTRIB, ""+(timestamp));
5) fix the hbase-site.xml in the codebase for zookeeper quorum and hbase parent znode info
6) simply run the StatDataCreatorTest to create data for the run and load it in hdfs for 10M records
7) to run the ready made jar attached, use the following commands,
    --- export HADOOP_CLASSPATH="./:/usr/hdp/2.3.0.0-2557/hbase/lib/*:'hadoop classpath'"; -- or something similar to set the classpath
   hadoop jar phoenix_mr_ts_upsert-jar-with-dependencies.jar statPhoenixLoader hdfs:///user/*****/stat-data-1.txt STAT_TABLE 1447574400000
   hadoop jar phoenix_mr_ts_upsert-jar-with-dependencies.jar statPhoenixLoader hdfs:///user/*****/stat-data-1.txt STAT_TABLE 1448092800000
    After 20 mins…
   hadoop jar phoenix_mr_ts_upsert-jar-with-dependencies.jar statPhoenixLoader hdfs:///user/*****/stat-data-1.txt STAT_TABLE 1447660800000
   hadoop jar phoenix_mr_ts_upsert-jar-with-dependencies.jar statPhoenixLoader hdfs:///user/*****/stat-data-1.txt STAT_TABLE 1449000000000
8) observe the 25x slowness in the 3rd run and observe the normal pace in the 4th run
9) Attached are the region server logs for the fast process in forward progression versus the slow progress in reverse timestamp

1) make sure to create a phoenix table with a lower timestamp than your earliest data timestamp
    creating the table can also be done from StatPhoenixAdminCommandsTest class

2) please substitute the correct zookeeper quorum in hbase-site.xml
3) please substitute the correct zookeeper.znode.parent in hbase-site.xml
    in cloudera or normal hadoop distributions, it should be /hbase
    in hdp distributions, it should be /hbase-unsecure

4) creating the jar using maven assembly plugin, the default jar name is
    jar uf phoenix-timestamp-dataload-jar-with-dependencies.jar hbase-site.xml

5) to run the jar use the following command
    hadoop jar phoenix-timestamp-dataload-jar-with-dependencies.jar widgetPagesStatPhoenixLoader
    zookeeperQuorum zookeeperPort HbaseZnodeParent TableName dataTimestamp

    hdfs:///data/bi/widget/WIDGET_CACHE_V1/rundate=20151205
    WIDGET_PAGES_STAT 20151205 master01.preprod.datalake.cdk.com

hadoop jar phoenix-timestamp-dataload-jar-with-dependencies.jar widgetPagesStatPhoenixLoader
hdfs://user/thangar/data-1.txt master01.preprod.datalake.cdk.com 2181 /hbase-unsecure WIDGET_PAGES_STAT 1447574400000
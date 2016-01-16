package org.apache.phoenix.dataload.stat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.phoenix.mapreduce.util.PhoenixConfigurationUtil;
import org.apache.phoenix.util.PhoenixRuntime;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

/**
 * Created by thangar
 */
public class StatPhoenixRunnerTest {

    @Test
    public void runPhoenixSeed() throws InterruptedException, IOException, ClassNotFoundException {
        final Configuration conf = new Configuration();
        conf.set("fs.default.name", "file:////");

        HBaseConfiguration.addHbaseResources(conf);
        String rundateString = "20160101";
        conf.set("rundate", rundateString);

        long timestamp = StatLineParser.parseDateString(rundateString);
        conf.set(PhoenixRuntime.CURRENT_SCN_ATTRIB, ""+(timestamp));
        conf.set(PhoenixConfigurationUtil.CURRENT_SCN_VALUE, "" + timestamp);

        URL resource = getClass().getClassLoader().getResource("stat-input-test.txt");
        StatPhoenixLoader.createJob(conf, new Path(resource.getPath()), "STAT_TABLE");
    }


    @Test
    public void runPhoenixSeedWithBadRecords() throws InterruptedException, IOException, ClassNotFoundException {
        final Configuration conf = new Configuration();
        conf.set("fs.default.name", "file:////");

        HBaseConfiguration.addHbaseResources(conf);

        String rundateString = "20160101";
        conf.set("rundate", rundateString);

        URL resource = getClass().getClassLoader().getResource("stat-input-badrecords.txt");
        StatPhoenixLoader.createJob(conf, new Path(resource.getPath()), "STAT_TABLE");
    }
}

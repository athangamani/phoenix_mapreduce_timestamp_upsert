package org.apache.phoenix.dataload.stat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.phoenix.mapreduce.PhoenixOutputFormat;
import org.apache.phoenix.mapreduce.util.PhoenixConfigurationUtil;
import org.apache.phoenix.mapreduce.util.PhoenixMapReduceUtil;
import org.apache.phoenix.util.PhoenixRuntime;

import java.io.IOException;

/**
 * Created by thangar on 9/3/15.
 */
public class StatPhoenixLoader {

    public static final String NAME = "StatPhoenixLoader";
    private static int count = 0;
    private static final double id = Math.random();

    public static class StatPhoenixMapper extends Mapper<LongWritable, Text, NullWritable, StatWritable> {
        @Override
        public void map(LongWritable longWritable, Text text, Context context) throws IOException, InterruptedException {
            StatLineParser parser = new StatLineParser();
            try {
                Stat stat = parser.parse(text.toString());

                if (stat != null && primaryKeysNotEmpty(stat)) {
                    StatWritable statWritable = new StatWritable();
                    statWritable.setStat(stat);
                    context.write(NullWritable.get(), statWritable);
                    count++;
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }

        private boolean primaryKeysNotEmpty(Stat stat) {
            return stat.getPk1() != null && !("").equals(stat.getPk1())
                    && stat.getPk2() != null && !("").equals(stat.getPk2());
        }
    }

    public static void main(String[] args) throws Exception {
        final Configuration conf = new Configuration();

        if (args != null && args.length != 3){
            throw new Exception("Usage : HDFSInputPath TableName dataTimestamp");
        }

        String inputpath = args[0];
        String tableName = args[1];
        String dataTimestamp = args[2];

        conf.set("dataTimestamp", dataTimestamp);
        conf.set(PhoenixRuntime.CURRENT_SCN_ATTRIB, ""+(dataTimestamp));
        conf.set(PhoenixConfigurationUtil.CURRENT_SCN_VALUE, "" + dataTimestamp);

        HBaseConfiguration.addHbaseResources(conf);

        Path inputPath = new Path(inputpath);
        createJob(conf, inputPath, tableName);
    }

    public static void createJob(Configuration conf, Path inputPath, String tableName) throws IOException, InterruptedException, ClassNotFoundException {
        Job job = Job.getInstance(conf, NAME);
        // Set the target Phoenix table and the columns
        PhoenixMapReduceUtil.setOutput(job, tableName, "PK1,PK2,PK3,STAT1,STAT2,STAT3");
        FileInputFormat.setInputPaths(job, inputPath);
        job.setMapperClass(StatPhoenixMapper.class);
        job.setMapOutputKeyClass(NullWritable.class);
        job.setMapOutputValueClass(StatWritable.class);
        job.setOutputFormatClass(PhoenixOutputFormat.class);
        TableMapReduceUtil.addDependencyJars(job);
        job.setNumReduceTasks(0);
        job.waitForCompletion(true);
        if (!job.waitForCompletion(true)){
            throw new RuntimeException("Stat PhoenixHbase Upload Job Failed or Timed out");
        }
    }
}

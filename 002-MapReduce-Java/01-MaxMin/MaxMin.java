import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MaxMin {
    public static class MapMinMax extends Mapper<LongWritable, Text, Text, FloatWritable> {
        private Text outputKey = new Text();
        private FloatWritable outputValue = new FloatWritable();

        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            try {
                String[] values = value.toString().split("\\s+");

                if (values.length >= 17) {
                    float max_value = Float.parseFloat(values[15]);
                    float min_value = Float.parseFloat(values[16]);

                    outputKey.set("MAX");
                    outputValue.set(max_value);
                    context.write(outputKey, outputValue);

                    outputKey.set("MIN");
                    outputValue.set(min_value);
                    context.write(outputKey, outputValue);
                }
            } catch (NumberFormatException e) {
                // Handle parsing errors if needed
            }
        }
    }

    public static class ReduceMinMax extends Reducer<Text, FloatWritable, Text, FloatWritable> {
        public void reduce(Text key, Iterable<FloatWritable> values, Context context)
                throws IOException, InterruptedException {
            float MIN = Float.MAX_VALUE;
            float MAX = Float.MIN_VALUE;

            for (FloatWritable value : values) {
                float floatValue = value.get();
                if (floatValue > MAX) {
                    MAX = floatValue;
                }
                if (floatValue < MIN) {
                    MIN = floatValue;
                }
            }

            if ("MAX".equals(key.toString())) {
                context.write(new Text("MAX"), new FloatWritable(MAX));
            } else if ("MIN".equals(key.toString())) {
                context.write(new Text("MIN"), new FloatWritable(MIN));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: MaxMin <input path> <output path>");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "MaxMin MapReduce");

        job.setJarByClass(MaxMin.class);
        job.setMapperClass(MapMinMax.class);
        job.setReducerClass(ReduceMinMax.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FloatWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

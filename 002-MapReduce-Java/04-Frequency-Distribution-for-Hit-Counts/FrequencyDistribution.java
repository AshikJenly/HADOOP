
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;


public class FrequencyDistribution
{
    public static class  MapFrequency
                extends Mapper<LongWritable,Text,IntWritable,Text>                  
    {
        public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException
        {
        String words[] = value.toString().strip().split("\\s");
        
        context.write(new IntWritable(Integer.parseInt(words[1])),new Text(words[0]));
        
    }
    }

    public static class ReduceFrequency
                        extends Reducer<IntWritable,Text,Text,IntWritable>
    {
        public void reduce(IntWritable key,Iterable<Text> values,Context context) throws IOException,InterruptedException
        {
        
        for(Text value:values)
        {
            context.write(new Text(value),key);
        }
    }
    }
    public static void main(String args[]) throws Exception
    {
        if(args.length != 2)
        {
            System.err.println("Insufficient Parameters");
            System.exit(-1);
        }

        Configuration conf = new Configuration();
           
        Job job = new Job(conf,"Word Count Map Reduce");

        job.setJarByClass(FrequencyDistribution.class);
        job.setMapperClass(MapFrequency.class);
        job.setReducerClass(ReduceFrequency.class);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(Text.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);


        TextInputFormat.addInputPath(job,new Path(args[0]));
        TextOutputFormat.setOutputPath(job,new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);


    }
}
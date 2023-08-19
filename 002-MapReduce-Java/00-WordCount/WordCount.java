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

public class WordCount
{
    public static class  MapWordCount 
                extends Mapper<LongWritable,Text,Text,IntWritable>                  
    {
        public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException
        {
        String words[] = value.toString().strip().split("\\s+");
        
        for (String word:words){
            context.write(new Text(word),new IntWritable(1));

        }
    }
    }

    public static class ReduceWordCount
                        extends Reducer<Text,IntWritable,Text,IntWritable>
    {
        public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException
        {
        
        int sum = 0;
        for(IntWritable value:values)
        {
            sum +=value.get();
        }
        context.write(key,new IntWritable(sum));
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

        // conf.set

        Job job = new Job(conf,"Word Count Map Reduce");

        job.setJarByClass(WordCount.class);
        job.setMapperClass(MapWordCount.class);
        job.setReducerClass(ReduceWordCount.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        
        
        
        // job.setNumReduceTasks(1);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);


        TextInputFormat.addInputPath(job,new Path(args[0]));
        TextOutputFormat.setOutputPath(job,new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);


    }
}
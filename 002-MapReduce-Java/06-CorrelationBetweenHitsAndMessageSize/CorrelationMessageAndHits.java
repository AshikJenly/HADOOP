// 199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] "GET /history/apollo/ HTTP/1.0" 200 6245
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


public class CorrelationMessageAndHits {
    public static class SizeAndHitsMapper  extends Mapper<LongWritable,Text,IntWritable,IntWritable>
    {
        private final IntWritable one = new IntWritable(1);
        public void map(LongWritable key,Text value,Context cont) throws InterruptedException,IOException
        {

            String values[] = value.toString().strip().split(" ");
            try{
            if(values.length > 7)
            {
                int size = Integer.parseInt(values[values.length - 1]);
                cont.write(new IntWritable(size/1024),one);

            }
        }catch(Exception e){}
        
        }

        
    }

    public static class SizeAndHitsReducer extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>

    {
        public void reduce(IntWritable key,Iterable<IntWritable> values,Context cont)throws InterruptedException,IOException
        {
            int sum = 0;
            for(IntWritable value: values)
            {
                sum += value.get();
            }
            cont.write(key,new IntWritable(sum));

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

        job.setJarByClass(CorrelationMessageAndHits.class);
        job.setMapperClass(SizeAndHitsMapper.class);
        job.setReducerClass(SizeAndHitsReducer.class);

        job.setNumReduceTasks(3);

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);
        
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(IntWritable.class);
        
        
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);


        TextInputFormat.addInputPath(job,new Path(args[0]));
        TextOutputFormat.setOutputPath(job,new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);





    }
}

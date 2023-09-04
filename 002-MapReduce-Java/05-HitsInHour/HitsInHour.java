
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

public class HitsInHour
{
    public static class  MapHitsInHour 
                extends Mapper<LongWritable,Text,IntWritable,IntWritable>                  
    {
        //String logString = "tlorek.interart.com - - [25/Jul/1995:14:34:31 -0400] \"GET /images/ksclogo-medium.gif HTTP/1.0\" 304 0 ";
        public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException
        {


        try{
        String words[] = value.toString().strip().split("\\s+");
        String date = words[3].strip().substring(1);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);

        int hour = dateTime.getHour();

        context.write(new IntWritable(hour),new IntWritable(1));
        }
        catch(ArrayIndexOutOfBoundsException e)
        {}
        
    }
    }

    public static class ReduceHitsInHour
                        extends Reducer<IntWritable,IntWritable,IntWritable,IntWritable>
    {
        public void reduce(IntWritable key ,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException
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
        
        Job job = new Job(conf,"Word Count Map Reduce");

        job.setJarByClass(HitsInHour.class);
        job.setMapperClass(MapHitsInHour.class);
        job.setReducerClass(ReduceHitsInHour.class);

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
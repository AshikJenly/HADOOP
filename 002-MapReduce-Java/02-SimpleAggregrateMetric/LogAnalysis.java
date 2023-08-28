import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


//To find Mean,Max, and Min of message size

public class LogAnalysis {
    public static class  MessageSizeMapper
    extends Mapper<LongWritable,Text,Text,IntWritable>                  
    {

        private Text logKey = new Text();
        private IntWritable logValue = new IntWritable();
        // 199.72.81.55 - - [01/Jul/1995:00:00:01 -0400] "GET /history/apollo/ HTTP/1.0" 200 6245
        public void map(LongWritable key,Text value,Context context) throws IOException,InterruptedException
        {

         String []parts = value.toString().split(" ");
         try{
         if(parts.length >= 7 )
         {
            String ipAddr = parts[0];
            
            int messageLength = Integer.parseInt(parts[parts.length -1]);
            logKey.set("MessageSize");
            logValue.set(messageLength);
            
            // context.write(logKey,logValue);
                        context.write(logKeyc,logValue);


         }     
         
        }catch(Exception e)
        {
            
        }
        }

    }

    public static class MessageInfoReducer
                        extends Reducer<Text,IntWritable,Text,Text>
    {
        
        
        public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException,InterruptedException
        {
        
        
            int max = Integer.MIN_VALUE; // Initialize max to the smallest possible integer
            int total = 0;
            int count = 0;
            int min = Integer.MAX_VALUE;
            for (IntWritable value : values) {
                int currentValue = value.get();
                if (currentValue > max) {
                    max = currentValue;
                }
                if (currentValue < min)
                {
                    min = currentValue;
                }
                total += currentValue;
                count ++;
            }
    
            context.write(new Text("MAX"),new Text(""+ max));
            context.write(new Text("MIN"),new Text(""+ min));
            context.write(new Text("MEAN"),new Text(""+ (total / count)));


        }
    }
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "log analysis");

        job.setNumReduceTasks(1);

        job.setJarByClass(LogAnalysis.class);
        job.setMapperClass(MessageSizeMapper.class);
        job.setReducerClass(MessageInfoReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

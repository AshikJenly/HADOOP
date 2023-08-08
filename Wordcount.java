import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

class TokenizerMapper extends Mapper{
public void map(Object key,Text value,Context context){
    try
    {
        StringTokenizer itr = new StringTokenizer(value.toString());

        while (itr.hasMoreTokens()) 
        {
        word.set(itr.nextToken());
        context.write(word, new IntWritable(1));
        }
    catch(Exception e)
    {
        System.out.println(e.toString());
    }

}
}
class IntSumReducer extends Reducer{

}
public class Wordcount {
    
}

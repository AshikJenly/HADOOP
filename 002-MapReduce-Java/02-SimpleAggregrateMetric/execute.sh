clear
hdfs dfs -put ./datas/NASA_access_log_Jul95 /user/mapreduce/ex02/input/
rm ./*.class
javac LogAnalysis.java 
rm ./*.jar
jar -cvf LogJar.jar *.class

hdfs dfs -rm -r /user/mapreduce/ex02/output
hadoop jar LogJar.jar LogAnalysis /user/mapreduce/ex02/input/NASA_access_log_Jul95 /user/mapreduce/ex02/output
rm -r ./output
hdfs dfs -get /user/mapreduce/ex02/output .
cat ./output/part-r-00000
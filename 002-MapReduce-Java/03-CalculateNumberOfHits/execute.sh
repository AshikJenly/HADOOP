rm *.class
# javac CountHits.java
javac CountHits.java

rm *.jar
jar -cvf count.jar *.class
hdfs dfs -rm -r /user/counthits/output

# hadoop jar count.jar CountHits /user/counthits/input/head /user/counthits/output

hadoop jar count.jar CountHits /user/counthits/input/NASA_access_log_Jul95 /user/counthits/output

rm -r output
hdfs dfs -get /user/counthits/output .
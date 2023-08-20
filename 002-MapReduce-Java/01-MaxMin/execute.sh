clear
rm *.class
javac MaxMin.java 
rm *.jar
jar -cvf maxmin.jar *.class

hdfs dfs -rm -r /user/MaxMinOutput
hadoop jar maxmin.jar MaxMin /user/MaxMinData/* /user/MaxMinOutput

hdfs dfs -get /user/MaxMinOutput/* .
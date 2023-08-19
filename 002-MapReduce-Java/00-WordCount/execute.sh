clear
javac WordCount.java
jar -cvf wordcount.jar *.class
hadoop jar wordcount.jar WordCount /user/inputdata/inputfile.txt /user/wordcountoutput

hdfs dfs -get /user/wordcountoutput/ .
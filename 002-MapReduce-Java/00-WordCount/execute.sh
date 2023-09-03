hdfs dfs -mkdir /user
hdfs dfs -mkdir /user/mapreduce
hdfs dfs -mkdir /user/mapreduce/ex00
hdfs dfs -mkdir /user/mapreduce/ex00/input


INPUT_FILE_NAME="*"

INPUT_PATH="/user/mapreduce/ex03/input/datas"
OUTPUT_PATH="/user/mapreduce/ex03/output"
DATA_SOURCE_PATH="./datas"
MAIN_JAVA_FILE_NAME="WordCount.java"
MAIN_CLASS_NAME="WordCount"
JAR_NAME="Count.jar"

hdfs dfs -put $DATA_SOURCE_PATH  $INPUT_PATH
rm *.class
rm *.jar
javac $MAIN_JAVA_FILE_NAME
jar -cvf $JAR_NAME *.class

hdfs dfs -rm -r $OUTPUT_PATH
hadoop jar $JAR_NAME $MAIN_CLASS_NAME $INPUT_PATH/$INPUT_FILE_NAME $OUTPUT_PATH 

rm -r ./output
hdfs dfs -get $OUTPUT_PATH .
cat ./output/part-r-00000
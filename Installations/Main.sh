#parameter(java version)
function install_java() {
    version=$1
    if java --version 2>&1 | grep -q "openjdk"; then
        echo "Java is installed."
    else
        echo "Installing java"
    
        sudo apt update
        sudo apt install openjdk-$version-jdk

        if java --version 2>&1 | grep -q "openjdk"; then
            echo "Java is installed."
        else 
            echo "Java installation  Failed";
            return
        fi
    fi
}

#parameter(hadoop version)
function install_hadoop(){
    hadoopVersion=$1
    if which hadoop &>/dev/null; then
        echo "Hadoop is installed."
    else
        echo "Installing Hadoop"
        cd ~
        # wget https://archive.apache.org/dist/hadoop/common/$hadoopVersion/$hadoopVersion.tar.gz
        tar -xzvf $hadoopVersion.tar.gz
        sudo mv $hadoopVersion/usr/local/hadoop
        clear
        echo "Hadoop Installed Successfully"
    fi
}
#parameter(hadoop version)
function set_path(){
    hadoopVersion=$1
    javaVersion=$2
    versionOnly="${hadoopVersion#*-}"

    echo "Setting Up Hadoop Path ...."
    echo "export JAVA_HOME=/usr/lib/jvm/java-$javaVersion-openjdk-amd64" >> ~/.bashrc
    echo 'export PATH=$PATH:/usr/lib/jvm/java-'$javaVersion'-openjdk-amd64/bin' >> ~/.bashrc
    echo 'export HADOOP_HOME=~/'$hadoopVersion'/' >> ~/.bashrc
    echo 'export PATH=$PATH:$HADOOP_HOME/bin' >> ~/.bashrc
    echo 'export PATH=$PATH:$HADOOP_HOME/sbin' >> ~/.bashrc
    echo 'export HADOOP_MAPRED_HOME=$HADOOP_HOME' >> ~/.bashrc
    echo 'export YARN_HOME=$HADOOP_HOME' >> ~/.bashrc
    echo 'export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop' >> ~/.bashrc
    echo 'export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native' >> ~/.bashrc
    echo 'export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib/native"' >> ~/.bashrc
    echo 'export HADOOP_STREAMING=$HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-'$versionOnly'.jar' >> ~/.bashrc
    echo 'export HADOOP_LOG_DIR=$HADOOP_HOME/logs' >> ~/.bashrc
    echo 'export PDSH_RCMD_TYPE=ssh' >> ~/.bashrc

    echo 'export HADOOP_CLASSPATH=$(hadoop classpath)'  >> ~/.bashrc
    echo "Path Setting Successfull ...."

    source ~/.bashrc
}
#parameters (java verision)
function set_configurations(){

    javaVersion=$1
    echo "Setting Configuations"

    echo "JAVA_HOME=/usr/lib/jvm/java-$javaVersion-openjdk-amd64" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh

    source ~/.bashrc

    # Core Site.xml
    echo "Altering Core site"
    cat ~/Installations/XMLs/core-site.xml  > $HADOOP_HOME/etc/hadoop/core-site.xml
    
    echo "Altering Hdfs site"
    #Hdfs Site.xml
    cat ~/Installations/XMLs/hdfs-site.xml  > $HADOOP_HOME/etc/hadoop/hdfs-site.xml

    echo "Altering MapRed site"
    #MapRed Site.xml
    cat ~/Installations/XMLs/mapred-site.xml > $HADOOP_HOME/etc/hadoop/mapred-site.xml
    
    echo "Altering Yarn site"
    #Yarn Site.xml
    cat ~/Installations/XMLs/yarn-site.xml  >$HADOOP_HOME/etc/hadoop/yarn-site.xml
}
function setup_SSH(){
    echo "Setting Up SSH"
    ssh-keygen -t rsa -P "" -f ~/.ssh/id_rsa
    cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys
    echo "SSH setup successfull"

}
function format_node(){

    echo "Formatting Nodes"
    cd ~/hadoop/sbin
    hdfs namenode -format
}

# Main Method
javaVersion="11"
hadoopVersion="hadoop-3.3.1"

install_java $javaVersion
install_hadoop $hadoopVersion
set_path $hadoopVersion $javaVersion
set_configurations $javaVersion
setup_SSH
format_node

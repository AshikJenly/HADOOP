function install_java() {
    if java --version 2>&1 | grep -q "openjdk"; then
        echo "Java is installed."
    else
        echo "Installing java"
    
        sudo apt update
        sudo apt install openjdk-11-jdk

        if java --version 2>&1 | grep -q "openjdk"; then
            echo "Java is installed."
        else 
            echo "Java installation  Failed";
            return
        fi
    fi
}



function install_hadoop(){
    if which hadoop &>/dev/null; then
        echo "Hadoop is installed."
    else
        echo "Installing Hadoop"
        cd ~
        wget https://archive.apache.org/dist/hadoop/common/hadoop-3.3.1/hadoop-3.3.1.tar.gz
        tar -xzvf hadoop-3.3.1.tar.gz
        sudo mv hadoop-3.3.1 /usr/local/hadoop
        clear
        echo "Hadoop Installed Successfully"
    fi
}
function set_path(){

    echo "Setting Up Hadoop Path ...."
    echo "export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64" >> ~/.bashrc
    echo 'export PATH=$PATH:/usr/lib/jvm/java-11-openjdk-amd64/bin' >> ~/.bashrc
    echo 'export HADOOP_HOME=~/hadoop-3.3.1/' >> ~/.bashrc
    echo 'export PATH=$PATH:$HADOOP_HOME/bin' >> ~/.bashrc
    echo 'export PATH=$PATH:$HADOOP_HOME/sbin' >> ~/.bashrc
    echo 'export HADOOP_MAPRED_HOME=$HADOOP_HOME' >> ~/.bashrc
    echo 'export YARN_HOME=$HADOOP_HOME' >> ~/.bashrc
    echo 'export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop' >> ~/.bashrc
    echo 'export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_HOME/lib/native' >> ~/.bashrc
    echo 'export HADOOP_OPTS="-Djava.library.path=$HADOOP_HOME/lib/native"' >> ~/.bashrc
    echo 'export HADOOP_STREAMING=$HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-3.3.1.jar' >> ~/.bashrc
    echo 'export HADOOP_LOG_DIR=$HADOOP_HOME/logs' >> ~/.bashrc
    echo 'export PDSH_RCMD_TYPE=ssh' >> ~/.bashrc

    echo 'export HADOOP_CLASSPATH=$(hadoop classpath)'  >> ~/.bashrc
    echo "Path Setting Successfull ...."

    source ~/.bashrc
}
function set_configurations(){

    echo "Setting Configuations"

    echo "JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64" >> $HADOOP_HOME/etc/hadoop/hadoop-env.sh


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

install_java
install_hadoop
set_path
set_configurations
setup_SSH
format_node

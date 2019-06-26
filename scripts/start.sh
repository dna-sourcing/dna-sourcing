# 编译
# mvn clean install -Dmaven.test.skip=true

# 部署运行
#
APPNAME="dna-sourcing-0.0.1-SNAPSHOT.jar"
APPLOCATION="/home/ubuntu/dna-sourcing/target/dna-sourcing-0.0.1-SNAPSHOT.jar"
APPENV="/home/ubuntu/dna-sourcing/config/application-remotetest.properties"
APPPORT=7071
LOGLOCATION="/home/ubuntu/dna-sourcing/log/all.log"

# touch /home/ubuntu/dna-sourcing/log/all.log

#
ps -ef | grep $APPNAME | grep -v grep | awk '{print $2}' | xargs kill -9

#
# echo "nohup java -Dspring.profiles.active=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &"
# nohup java -Dspring.profiles.active=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &

echo "nohup java -Dspring.config.location=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &"
nohup java -Dspring.config.location=$APPENV -Dserver.port=$APPPORT -jar $APPLOCATION >/dev/null 2>&1 &

#
tail -f $LOGLOCATION
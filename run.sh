export JAVA_HOME="/mnt/c/Java/jdk1.8.0_211_linux"

PROG_ID="SB.EBILL"


cnt=`ps -ef |grep -v ps |grep -v grep|grep SB.EBILL | wc -l | awk '{printf $1"\n";}'` 


echo "***************************************************"
echo "*  Start SB.EBILL                           *"
echo "***************************************************"

echo "cnt=${cnt}"


if [ $cnt -eq 0 ]
then
    echo $JAVA_HOME/bin/java --Dlogging.config=file:./config/log4j2.xml -Dname=PROG_ID -classpath ./config -jar ./target/EBill.jar 
    $JAVA_HOME/bin/java -Dlogging.config=file:./config/log4j2.xml -Dname=$PROG_ID -classpath ./config -jar ./target/EBill.jar > ./LOG/startup.txt &  echo $! >./bin/PID_FILE
else
    echo EBILL is running, please do not re-run.
		ps -ef |grep -v grep | grep ${PROG_ID}
		exit 1
fi
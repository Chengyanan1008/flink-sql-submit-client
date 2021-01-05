#!/bin/bash
#export FLINK_HOME=/data/software/flink-1.11.0
sql_file=$2
if [ -z "$FLINK_HOME" ];then
	echo "请指定FLINK_HOME 或者在该配置文件中配置"
	exit 1
fi

if [ $# -lt 2 ];then
	echo "命令格式为 ./sql-submit.sh -f <sql-file>"
	exit 1
fi
# 要依赖的jar包
SQL_JAR=./flink-sql-submit-1.0-SNAPSHOT.jar
if [ -f $SQL_JAR ];then
	echo "`date +%Y-%m-%d" "%H:%M:%S` load jars from ${SQL_JAR}"
else
	echo "failed to load dependent jars for sql-submit.sh,please specify it"
	exit 1
fi

if [ ! -f $sql_file ];then
	echo "sql文件 $sql_file 不存在,请检查文件路径"
	exit 1
fi
#提交命令
if [ $1 = "-f" ];then
	$FLINK_HOME/bin/flink run -p 5 -c com.idengyun.flink.sqlsubmit.SqlSubmit $SQL_JAR $1 $sql_file
else
	echo "命令格式为 ./sql-submit.sh -f <sql-file>"
	exit 1
fi

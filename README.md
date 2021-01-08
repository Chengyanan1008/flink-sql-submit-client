
# flink-sql-submit-client
执行Flink SQL 文件的客户端



- Flink 版本：flink 1.11.0
- 其他版本待测试
- 是在[flink-sql-submit](https://github.com/wuchong/flink-sql-submit)的基础上修改而来




# 使用简单方便
- 需要指定FLINK_HOME
- 下载上面code中的jar包[flink-sql-submit-1.0-SNAPSHOT.jar](https://github.com/Chengyanan1008/flink-sql-submit-client/blob/master/flink-sql-submit-1.0-SNAPSHOT.jar)
- **修改 sql-submit.sh 脚本中jar包的路径 和指定 FLINK_HOME**
- jar包可以直接用如有需要可以根据源码自行修改，然后再打包，改源码后注意修改jar包主类

# 使用方式

```sh
./sql-submit.sh -f <sql-file>
```

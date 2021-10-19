
# flink-sql-submit-client
执行Flink SQL 文件的客户端



- Flink 版本：flink 1.11.0
- 其他版本待测试
- 是在[flink-sql-submit](https://github.com/wuchong/flink-sql-submit)的基础上修改而来




# 使用简单方便
- 需要指定FLINK_HOME
- 下载上面code中的jar包[flink-sql-submit-1.0-SNAPSHOT.jar](https://github.com/Chengyanan1008/flink-sql-submit-client/blob/master/flink-sql-submit-1.0-SNAPSHOT.jar)
- **修改 sql-submit.sh 脚本中jar包的路径 和指定 FLINK_HOME**
- jar包可以直接用，如有需要可以根据源码自行修改，然后再打包，改源码后注意修改jar包主类

# 使用方式

```sh
./sql-submit.sh -f <sql-file>
```


# 2021年10月19日12:35🕥 
现在flink 1.14版本已经支持SQL客户端执行SQLfile了：

![image](https://user-images.githubusercontent.com/48700073/137844625-7c38498a-d1b7-497e-a011-c7d3abcfd744.png)

参考：
https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/dev/table/sqlclient/#sql-client-startup-options

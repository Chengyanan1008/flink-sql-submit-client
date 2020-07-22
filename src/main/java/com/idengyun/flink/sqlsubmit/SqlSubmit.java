package com.idengyun.flink.sqlsubmit;

import com.idengyun.flink.sqlsubmit.cli.CliOptions;
import com.idengyun.flink.sqlsubmit.cli.CliOptionsParser;
import com.idengyun.flink.sqlsubmit.cli.SqlCommandParser;
import org.apache.flink.api.common.JobID;
import org.apache.flink.core.execution.JobClient;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.SqlParserException;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

/**
 * @date : 2020-7-22 10:12:01
 * @desc : 提交SQL的命令
 */
public class SqlSubmit {

    public static void main(String[] args) throws Exception {
        final CliOptions options = CliOptionsParser.parseClient(args);
        SqlSubmit submit = new SqlSubmit(options);
        submit.run();
    }

    // --------------------------------------------------------------------------------------------

    private String sqlFilePath;
    private TableEnvironment tEnv;

    private SqlSubmit(CliOptions options) {
        this.sqlFilePath = options.getSqlFilePath();
    }

    private void run() throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment();
        this.tEnv = StreamTableEnvironment.create(environment,
                EnvironmentSettings.newInstance().inStreamingMode().useBlinkPlanner().build());
        List<String> sql = Files.readAllLines(Paths.get(sqlFilePath));
        List<SqlCommandParser.SqlCommandCall> calls = SqlCommandParser.parse(sql);
        if (calls.size() == 0) {
            //no sql to execute
            throw new RuntimeException("There is no sql statement to execute,please check your sql file: " + sqlFilePath);
        }
        for (SqlCommandParser.SqlCommandCall call : calls) {
//            System.out.println(call.command.toString());
            callCommand(call);
        }
    }

    // --------------------------------------------------------------------------------------------

    private void callCommand(SqlCommandParser.SqlCommandCall cmdCall) {
        switch (cmdCall.command) {
            case SET:
                callSet(cmdCall);
                break;
            case CREATE_TABLE:
                callCreateTable(cmdCall);
                break;
            case INSERT_INTO:
                callInsertInto(cmdCall);
                break;
            default:
                throw new RuntimeException("Unsupported command: " + cmdCall.command);
        }
    }

    private void callSet(SqlCommandParser.SqlCommandCall cmdCall) {
        String key = cmdCall.operands[0];
        String value = cmdCall.operands[1];
        tEnv.getConfig().getConfiguration().setString(key, value);
        System.out.println("设置 " + key + "-->" + value + " 成功");
    }

    private void callCreateTable(SqlCommandParser.SqlCommandCall cmdCall) {
        String ddl = cmdCall.operands[0];
        try {
            tEnv.executeSql(ddl);
        } catch (SqlParserException e) {
            throw new RuntimeException("SQL parse failed:\n" + ddl + "\n", e);
        }
        String tableName = ddl.split("\\s+")[2];
        System.out.println("创建表 " + tableName + " 成功");
    }

    private void callInsertInto(SqlCommandParser.SqlCommandCall cmdCall) {
        String dml = cmdCall.operands[0];
        Optional<JobClient> jobClient;
        try {
            TableResult result = tEnv.executeSql(dml);
            jobClient = result.getJobClient();
        } catch (SqlParserException e) {
            throw new RuntimeException("SQL parse failed:\n" + dml + "\n", e);
        }

        if (jobClient.isPresent()) {
            JobID jobID = jobClient.get().getJobID();
            System.out.println("任务提交成功,JobId: " + jobID);
        }

    }
}

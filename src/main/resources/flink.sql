CREATE TABLE user_behavior (
 distinct_id STRING COMMENT '唯一ID',
 ip STRING COMMENT 'ip地址',
 request_time BIGINT COMMENT '时间戳',
 event STRING COMMENT '事件',
 province STRING COMMENT '省份',
 ts as to_timestamp(from_unixtime(request_time/1000,'yyyy-MM-dd HH:mm:ss')),
 proctime as PROCTIME(),
 WATERMARK FOR ts as ts - INTERVAL '5' SECOND 
) WITH (
 'connector' = 'kafka',
 'topic' = 'flink_test_new',
 'scan.startup.mode'='earliest-offset',
 'properties.bootstrap.servers' = 'cdh-test05:9092',
 'properties.group.id'='test',
 'format' = 'json'
)

CREATE TABLE pro_cnt ( 
    province varchar,
    cnt BIGINT,
    PRIMARY KEY (province) NOT ENFORCED
) WITH (
    'connector' = 'elasticsearch-6',  
    'hosts' = 'http://10.60.11.5:9200',  
    'index' = 'pro_cnt',  
    'document-type' = 'doc',  
    'sink.bulk-flush.max-actions' = '1', 
    'format' = 'json'  -- 输出数据格式 json
)

INSERT INTO pro_cnt
select province,count(*)
from user_behavior
where province is not null and province <> ''
group by province

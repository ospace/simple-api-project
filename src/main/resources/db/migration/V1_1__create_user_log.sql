-------------------------------------------------------------------------------------
-- DB Schema
-------------------------------------------------------------------------------------
 
CREATE TABLE `tl_user_log` (
    `user_id`       bigint(20)      NOT NULL,
    `remote_ip`     varchar(39)     NULL,
    `action`        varchar(128)    NULL,
    `status`        int(4)          NULL,
    `message`       varchar(3000)   NULL,
    `user_agent`   varchar(255)    NULL,
    `create_date`   timestamp       NOT NULL DEFAULT current_timestamp()
) default character set utf8mb4 collate utf8mb4_general_ci;

ALTER TABLE `tl_user_log` ADD CONSTRAINT `FK_tb_user_TO_tl_user_log_1` FOREIGN KEY (
    `user_id`
)
REFERENCES `tb_user` (
    `id`
);

-------------------------------------------------------------------------------------
-- DB Data
-------------------------------------------------------------------------------------



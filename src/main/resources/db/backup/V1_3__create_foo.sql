-- apk version 관리 테이블
CREATE TABLE `tb_foo` (
    `id`                bigint(20)  NOT NULL,
    `file_id`                bigint(20)  NULL,
    `name`              varchar(255) NULL,
    `create_date`       timestamp   NULL,
    `creator`           bigint(20)  NULL
) default character set utf8mb4 collate utf8mb4_general_ci;

ALTER TABLE `tb_foo` ADD CONSTRAINT `PK_TB_APK_VERSION` PRIMARY KEY (
    `id`,
);

ALTER TABLE `tb_foo` CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT ;

ALTER TABLE `tb_foo` ADD CONSTRAINT `FK_tc_file_TO_tb_foo_1` FOREIGN KEY (
    `file_id`
)
REFERENCES `tc_file` (
    `id`
);

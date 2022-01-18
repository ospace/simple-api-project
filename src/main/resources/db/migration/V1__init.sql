-------------------------------------------------------------------------------------
-- DB Schema
-------------------------------------------------------------------------------------

CREATE TABLE `tc_file` (
    `id`                char(32)        NOT NULL PRIMARY KEY,
    `org_filename`      varchar(255)    NULL,
    `filepath`          varchar(255)    NULL,
    `extention`         varchar(8)      NULL,
    `type`              varchar(128)    NULL,
    `size`              int(11)         NULL,
    `create_date`       timestamp       NOT NULL DEFAULT current_timestamp(),
    `creator`           bigint(20)      NULL
) default character set utf8mb4 collate utf8mb4_general_ci;
-- utf8 collate utf8_general_ci, utf8mb4 collate utf8mb4_general_ci

CREATE TABLE `tc_code` (
    `code`          char(8)         NOT NULL PRIMARY KEY,
    `group_code`    char(4)         NOT NULL,
    `name`          varchar(255)    NULL,
    `order`         int(2)          NULL,
    `create_date`   timestamp       NOT NULL DEFAULT current_timestamp(),
    `modify_date`   timestamp       NULL,
    `creator`       bigint(20)      NULL,
    `modifier`      bigint(20)      NULL
) default character set utf8mb4 collate utf8mb4_general_ci;

CREATE TABLE `tc_code_group` (
    `code`          char(4)         NOT NULL PRIMARY KEY,
    `name`          varchar(255)    NULL,
    `create_date`   timestamp       NOT NULL DEFAULT current_timestamp(),
    `modify_date`   timestamp       NULL,
    `creator`       bigint(20)      NULL,
    `modifier`      bigint(20)      NULL
) default character set utf8mb4 collate utf8mb4_general_ci;

CREATE TABLE `tb_user` (
    `id`            bigint(20)      NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `image_id`      char(32)        NULL,
    `login_id`      varchar(300)    NOT NULL,
    `password`      varchar(255)    NULL,
    `name`          varchar(300)    NULL,
    `role_code`     char(8)         NULL DEFAULT 'ROLEUSER' COMMENT '역할',
    `use_yn`        char(1)         NULL DEFAULT 'Y',
    `create_date`   timestamp       NOT NULL DEFAULT current_timestamp(),
    `modify_date`   timestamp       NULL,
    `creator`       bigint(20)      NULL,
    `modifier`      bigint(20)      NULL
) default character set utf8mb4 collate utf8mb4_general_ci;

ALTER TABLE `tb_user` ADD CONSTRAINT `FK_tc_file_TO_tb_user_1` FOREIGN KEY (
    `image_id`
)
REFERENCES `tc_file` (
    `id`
);

-------------------------------------------------------------------------------------
-- DB Data
-------------------------------------------------------------------------------------

-- 계정 --
insert into tb_user(id, login_id, password, role_code, use_yn, creator, modifier) 
    values(1, "system", "", "", "N", 0, 0);
    
insert into tb_user(id, login_id, password, role_code, creator, modifier) 
    values(10, "admin", "$2a$10$28V3FCerYJgv9yxJKsLTq.tPW4rgerGuDNCcE.bujo4oE4G51DJeC", "ROLEADMN", 1, 1);


-- 그룹 코드 --
insert into tc_code_group(code, name, create_date, modify_date, creator, modifier)
    values("ROLE", "권한", now(), now(), 10, 10);


-- 상세 코드 --
insert into tc_code(code, group_code, name, create_date, modify_date, creator, modifier)
    values("ROLEADMN", "ROLE", "관리자", now(), now(), 10, 10);
insert into tc_code(code, group_code, name, create_date, modify_date, creator, modifier)
    values("ROLEOPER", "ROLE", "운영자", now(), now(), 10, 10);
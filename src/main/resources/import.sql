
create table T_USER(
	id    varchar(50),
	uname varchar(20),
	upwd  varchar(30),
	PRIMARY KEY (id)
) engine=innodb DEFAULT CHARACTER SET utf8 COMMENT='用户表';

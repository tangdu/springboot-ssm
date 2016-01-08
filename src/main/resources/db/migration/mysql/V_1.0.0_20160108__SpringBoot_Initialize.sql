
create table T_USER(
	id    varchar(50),
	uname varchar(20),
	upwd  varchar(30),
	PRIMARY KEY (id)
) engine=innodb DEFAULT CHARACTER SET utf8 COMMENT='用户表2';

insert into T_USER values('001','唐三','123');
insert into T_USER values('002','唐四','123');
insert into T_USER values('003','唐五','123');

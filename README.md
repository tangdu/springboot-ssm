##关于

项目由gradle构建，使用springBoot做为项目框架

持久化使用MyBatis

以下为版本信息：

	junit_version=4.12
	servlet_api_version=3.1.0
	spring_boot_version=1.3.1.RELEASE
	spring_framework_version=4.0.3.RELEASE
	jackson_version=2.4.5
	javax_el_version=2.2.6
	mysql_version=5.1.34
	mybatis_version=3.3.0
	mybatis_spring_version=1.2.3
	ehcache_version=2.10.1
##环境搭建
第1步：git下载代码
第2步：如果测试mongo需要下载springboot-mongo
第3步：执行gradle eclipse
第4步：导入项目
第5步：执行import.sql
第6步：修改application.properties中的数据库连接信息
第7步：运行Application.java
第8步：输入地址：http://localhost:9080/ssm 密码admin/admin
###解说
1 ComponentScanAutoConfiguration.java
	自动扫描，便于模块化开发
2 EhcacheAutoConfiguration.java
	ehcache支持
3 FrameworkMvcAutoConfiguration.java
	自定义mvc，可添加拦截器、静态路径等
4 MyBatisAutoConfiguration.java
	集成mybatis
5 MybatisProperties.java
	mybatis以自己配置文件
6 SimpleFlywayAutoConfiguration.java
	自定义flyway配置 自动加载【db.migration.mysql】该目录下sql，便于数据库初始化
7 WebMvcSecurityJpaAutoConfiguration.java
	自定义mvc权限过滤器

##说明
项目使用SpringSecurity做权限控制，默认用户名密码是admin/admin

前台使用模板引擎是thf

具体的配置请看这个包下的：com.tdu.autoconfiguration

本实例为SpringBoot与MyBatis集成，参考官方的实现。

数据库移植工具是flyway

如果想到SpringBoot与JPA集成的，请看我另一个项目:springboot-ssh

请关注我的主页，后期会提供一个比较完成的实例给大家。也是现在项目正在用到的。

##后期

准备集成或测试的模块为，solr

还在springBoot与redis持久化session集成，

内容都比较简单，差不多都是配置了事的.以上的内容都是实践过，需要整理。

还有一点内容将通过配置化mybatis反向生成代码，想通过编写gralde 任务或是单独使用groovy编写也可

联系邮箱:tangdu0228@aliyun.com
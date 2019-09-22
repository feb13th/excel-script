## 报表生成工具

### 1. 配置conf/db.json文件
	type: 数据库类型, 可选值 ["mysql", "sqlserver"]
	url: jdbc连接字符串, 选填，如果填写会忽略 ip,port,database
	ip:数据库安装的主机ip
	port:数据库运行的端口号
	database: 连接的数据库名称
	username: 数据库用户名
	password:数据库密码
	dateFromat: 日期转换格式, yyyy表示年, MM:表示月, dd:表示天, HH:表示小数, mm:表示分钟, ss:表示秒
	filepath: 生成的excel文件存储路径，不填则为当前软件启动路径下的excel文件夹

### 2.配置conf/sql.json文件
	sql: 执行查询的SQL语句
	filename: 该sql语句生成的excel文件名
	备注：sql和filename必须成对出现, 必须符合标准的json字符串格式

### 3.双击run.bat启动程序

### 4.程序运行结束后可查看控制台日志"create excel file complete, filepath:[path to file]"查看excel文件生成位置
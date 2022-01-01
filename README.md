> 期末大作业:综合使用本课程所学知识，完成一个大作业，该作业将作为期末评分的重要组成部分要求如下:
>
> 一、题目（三选一）
>
> 1、建立一个基于MySQL数据库的学生成绩管理系统，存储及管理学生信息、课程信息、成绩等内容。要求具有图形化界面，实现数据录入、查询、删除、修改等功能。
>
> 2、实现一个类QQ通信软件。要求具有图形化客户端界面、服务器端（不要求图形化），实现好友添加、删除功能、收发信息功能。
>
> 3、建立一个图片浏览器软件。可浏览不同文件夹，当该文件夹存在图片时，显示所有图片的缩略图，点击缩略图查看大图。
>
> 二、要求
>
> 每人独立完成，要求使用Java技术为主，开发内容属于应用软件（不含JSP、WEB、移动应用）。可参考书本或网络上范例，但必须在文档中加以说明。
>
> 警告！不得抄袭！违者将在年级通知群中公布！
>
> 
>
> 三、提交内容
>
> 1、作品的完整项目文件夹，命名为“班别姓名”模式，如“3班张达”
>
> 2、开发文档，内容至少包括作品简介、数据库设计（如果有）、模块设计、核心代码说明、总结等
>
> 3、在开发文档的附件中说明参考文档、参考范例、并附上相应网络地址
>
> 
>
> 四、提交时间
>
> 2022年1月24日前，即本学期最后一周结束之前。
>
> 
>
> 五、提交方式
>
> 将作品及文档压缩为“班别姓名.rar”模式，“3班张达.rar”后提交到励儒云。
>
> 
>
> 六、评分标准
>
> 根据作品的实现难度、完善程度、创新性评分



## 功能规划

### 概述

总体概述：

1. 实现MySQL的数据录入、查询、删除、修改，不局限于题目要求，拓展通用性(设计包和类时使其快速能够适应其他数据表)，使得以后迁移应用到别的场合更方便

   

规划功能细目：

1. 初始化

   1. (可选)实现注册和登录，使用加密算法存密码

   2. 选择数据库版本、URL、用户名、密码、(编码格式) 

      (可选)支持高级模式参数自定义(全部参数自己输入)

      (可选)支持重新选择

   3. 自动创建数据表

2. 通用设置

   1. (可选)多语言支持
   2. (可选)清空、重新初始化
   3. 备份和回档
   4. 保存和自动保存设置

3. 通用功能

   1. 单独增删改、显示所有
   2. 条件设置(预设和自定义MySQL语句)
   3. (可选)按格式批量添加、导入已有
   4. 使用帮助
   5. 操作日志记录

4. 具体功能

   1. 添加、删除、修改单个学生、课程、成绩
   2. 按学生名、课程名、成绩范围等单项查询(支持模糊)
   3. 统计(各学生、各课程平均分、最值)



实际实现的功能细目：





### 配置文件列表

在路径`data/` 下，配置文件

- `user.txt` 登录密码的MD5加密，密码为类包里的默认密码,如无特别规定的话下同

- `validate.txt` 校验文本，为了防止删除data/强行初始化来破解程序而保留的文件，该文件必须与user.txt的类包默认密码AES加密一致，当 `user.txt` 为空时，必须与 `Empty&` 文本的类包默认密码AES加密一致

  缺陷：破解方法是直接下载发行版的`data/`复制替代即可

- `settings.txt` 数据库设置文本

  格式为一行一个信息，依次是：

  - 0下标 版本号 
  - 1下标 IP
  - 2下标 端口
  - 3下标 数据库名
  - 4下标 用户名
  - 5下标 密码(AES加密)
  - 6下标 参数(一行一个)




在路径`log/` 下，为若干程序运行产生的日志文件，每次程序正常退出时写入一次日志文件(未来如有必要增加关闭写日志的选项)

> 日志文件为未来可能增加的诸如逐步撤销和逐步重做、自动批处理等功能做铺垫

文件格式为：`年yyyy-月mm-日dd-时hh-分mm-秒ss.log` ，如 `2022-01-01-15-54-09` 

每行是一条用户的操作行为及其操作的时间(`时:分:秒`)



### 数据库结构

以 `went` 数据库为例(在样例中使用`mysql8` 的 `127.0.0.1:3306`)

#### 数据表结构

##### 全局信息

全局变量表 info (类似于`.ini`配置文件) 记录全局状态信息

表结构：

- id 主键
- key 信息名
- value 信息值整数

建表语句：

```mysql
create table if not exists `info` (
    `id` int not null auto_increment, 
    `key` varchar(20) not null, 
    `value` int not null, 
    primary key(`id`), 
    unique(`key`)
) engine=InnoDB default charset=utf8;
```

存储的信息有：

- `cnt` 累计建表数(下一次生成的表号由此决定)
- `saved` 是否已保存临时表,是1否0
- `main` 主表编号
- `temp` 临时表编号
- `a_x` $x$号表累积学生数，默认为0
- `b_x` $x$号表累积课程数，默认为0
- `c_x` $x$号表累积成绩数，默认为0



性质：

- 所有操作在 `temp` 表进行，每次操作赋值 `saved=0`
- 当 `saved=1` 时， `main` 表和 `temp` 表数据完全一致
- 当保存操作时，复制 `temp` 表到 `main` 表，置 `saved=1`
- 当还原/不保存时，复制 `main` 表到 `temp` 表，置 `saved=1`



##### 学生

学生表 student_x 第$x$份学生表

- id
- name
- student\_number 字符串 学号
- major 字符串

建表语句：

```mysql
create table if not exists `student` (
    `id` int not null auto_increment, 
    `name` varchar(20) not null, 
    `student_number` varchar(20) not null, 
    `major` varchar(20), 
    primary key(`id`)
) engine=InnoDB default charset utf8;
```



##### 课程

课程表 subject_x

- id
- name
- semester 字符串，如2020秋

建表语句：

```mysql
create table if not exists `subject` (
    `id` int not null auto_increment, 
    `name` varchar(40) not null, 
    `semester` varchar(10), 
    primary key(`id`)
) engine InnoDB default charset utf8;
```



##### 成绩

成绩表 score_x

- id
- student_id
- subject_id
- value

建表语句：

```mysql
create table if not exists `score` (
    `id` int not null auto_increment, 
    `student_id` int not null, 
    `subject_id` int not null, 
    `value` int not null, 
    primary key(`id`)
) engine InnoDB default charset utf8;
```



#### 数据表列表

- `info` 全局状态信息表

- `student_x` 第 $x$ 份学生表

  `subject_x` 第 $x$ 份课程表

  `score_x ` 第 $x$ 份成绩表



### 类包结构

包 - 类 - 主要成员的树状结构表。

#### plugin

`plugin`包  通用功能

- `Base64Plugin` Base64编码解码类
  - `public static String get(byte[] key)` 编码
  - `public static byte[] from(String key)` 解码
- `Encrypt` 加解密类
  - `public static byte[] fill(String psw)`
  - `public static String encode(String ori[, String psw])` 
  - `public static String decode(String ori[, String psw])` 失败返回 null
- `PswMD5` MD5加密类
  - `public static String encrypt(String data)`
  - `public static String password_md5(String psw)` 加盐
- `Checker` 检验输入合法性等
- `FileHelper` 文件操作类
  - `public static String read(File/String f)` 失败null
  - `public static String[] readlines(File/String f)` 失败null
  - `public static boolean write(String t, File/String f)` 失败false
  - `public static boolean writelines(String[] t, File/String f)` 失败false
  - `public static boolean touch(File/String f)` 新建文件，失败false

- `SwingHelper` 简化 `Swing` 操作
  - `public static String font_size(String s, int siz) ` 得到 HTML 的 `div` 标签， `font-size:` 为 `siz`
- `FsLabel` 设置了字体大小的 `JLabel` 
  - `public FsLabel([String s[, int siz]])`
- 



#### mysql

`mysql`包  数据库功能

- `Link` 数据库链接和加载配置
  - `public static int version = 8` 数据库版本,当前版本只能取8;未来版本支持5
  - `public static boolean loaded = false` 数据库外部库是否已加载
  - `public static Connection con = null` 数据库连接
  - `public static String err_msg = ""` 报错信息
  - `public static boolean load()` 导入数据库外部库，返回是否成功
  - `public static void create_database(String ip, String port, String db, String name, String psw)` 创建数据库(不存在时)
  - `public static boolean connect(String ip, String port, String db, String name, String psw, String cfg)` 按参数连接到数据库，返回是否成功
  - `public static boolean connect() ` 按配置文件连接到数据库
- `Ctrl` 数据库控制操作类
  - `public static void raised(Exception e)` 输出报错信息到前台
  - `public static boolean run(String cmd)` 执行一般SQL语句
  - `public static boolean update(String cmd)` 执行更新SQL语句
  - `public static ResultSet query(String cmd)` 执行查询语句
  - `public static boolean exists(ResultSet res, String col, String key)` 查找结果某列是否有某值
  - `public static PreparedStatement pre(String cmd)` 创建一个预处理语句
  - `public static int getv(ResultSet res, String col, String key, String value)` 得到结果中 `col` 列为 `key` 的这一行里面 `value` 列的值(`int`类型)
  - `public static int getv(ResultSet/String res)` 执行只获得一行一列的查询语句并返回整数结果



#### base

`base`包  基础功能

- `Init` 初始化
  - `public static String login_md5 = null` 登录密码MD5加密
  - `public static boolean isValidate()` 密码存储是否正确
  - `public static boolean change_psw(String psw)` 修改密码
  - `public static String[] read_db_settings()` 读取数据库连接信息(下标 $[1,6]$ 有效;有可能读空)
  - `public static boolean is_inited_db()` 数据库连接信息是否不为空
  - `public static void update_db_settings(String ip, String port, String db, String user, String psw, String cfg)` 将数据库连接信息保存到文件
  - `public static final int psw_pos = 5` 数据库连接信息密码项下标
- `DbLoader` 数据库初始化和加载
  - `public static void checkinit() ` 检查并执行(若需要)初始化
  - `public static void cr_table()` 建立新的表
  - `public static void del_table(int x)` 删除指定编号的表
  - `public static void overwrite(int from, int to)` 用 from 表覆盖 to 表
  - `public static void save()` 保存临时表覆盖主表
  - `public static void undo()` 撤销临时表回退为主表
  - `public static int t_main = 0` 主表编号
  - `public static int t_temp = 0` 临时表编号
  - `public static int saved = 1` 临时表是否已保存到主表
  - `public static Vector<String> backups = new Vector<>()` 备份表编号列表
  - `public static int cnt_stu = 0` 临时表累积学生数
  - `public static int cnt_sub = 0` 临时表累积课程数
  - `public static int cnt_sco = 0` 临时表累积成绩数
  - `public static void add_info(String key, int value)` 增加一条全局信息
  - `public static void set_info(String key, int value)` 修改一条全局信息
  - `public static int get_info(String key)` 获取一条全局信息
  - `public static int del_info(String key)` 删除一条全局信息
- `DbCtrl` 数据库操纵
  - `public static void write_diary(String dia)` 增加运行日志行 (`dia`不需换行符)
  - `public static void save_diary()` 将日记写入磁盘
  - `public static int add_stu(String name, String number, String major)` 添加一个学生到临时表
  - `public static void upd_stu(int id, String name, String number, String major)` 修改临时表学生



#### ui

`ui`包  窗口化功能

- `Root` 主窗口
  - `public static void start_root()` 检查登录并启动程序
  - `public static void updateTitle()` 更新标题(未保存时有`*` 字符)

- `Login` 登录本程序的对话窗
  - `public boolean suc = false` 登录是否成功
  - `public Login()` 启动对话窗，进行登录，返回结果 `suc`
- `RootMenu` 主窗口的菜单栏
  - `public RootMenu(Root frame)` 

- `ChangePsw` 修改密码对话窗
  - `public ChangePsw(Root frame)` 

- `SetDatabase` 修改数据库连接配置对话窗
  - `public SetDatabase(Root frame)`

- `Page` 主面板类
  - `public Page()`

- `DbTable` 数据库结果表格类
  - `public DbTable()`
  - `public void render(ResultSet/String res)` 将查询语句(或结果)传入，将查询结果显示
  - `public void refresh()` 刷新当前查询语句
  - `public static void fresh()` 刷新当前查询语句
  - `public void addRow(String[] row)` 增加一行
  - `public void setRow(int row, String[] s)` 更新一行
  - `public static void updRow(int row, String[] s)` 更新一行
  - `public static int table_idx = 0 ` 当前表格编号,0学生,1课程,2成绩
  - ` public static DbTable that = null` 当前组件对象
  
- `Tabbar` 操作栏类 各种按钮的集合
  - `public Tabbar(DbTable jt)` 传入操作的表格

- `TbGlobal`
  - `public TbGlobal(DbTable jt)`
  - `public static ActionListener e_save` 事件监听器(保存)
  - `public static ActionListener e_undo` 事件监听器(撤销)
  - `public static ActionListener e_backup` 事件监听器(备份)
  - `public static ActionListener e_frombackup` 事件监听器(还原)
  - `public static ActionListener e_delbackup` 事件监听器(删除备份)
  - `public static ActionListener e_importall` 事件监听器(导入)
  - `public static ActionListener e_exportall` 事件监听器(导出全部)
  - `public static ActionListener e_exporta` 事件监听器(导出当前表)
  - `public static String get_newpath()` 从文件选择器获得一个`.sql`文件路径
  
- `TbStu` 学生数据管理的全部按钮和输入框
  - `public TbStu(DbTable jt)`
  - `public static void upd_input(String[] s)` 更新输入框




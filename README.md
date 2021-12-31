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

   

功能细目：

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



### 数据库结构

学生表 student

- id
- name
- student\_id
- (可选)major



课程表 subject

- id
- name
- (可选)semester



成绩表 score

- id
- student_id
- subject_id
- score



### 类包结构

包 -类-主要成员列表。这些主要成员将会被外部调用

`plugin`包  通用功能

- `Base64Plugin` Base64编码解码
  - `public static String get(byte[] key)` 编码
  - `public static byte[] from(String key)` 解码
- `Encrypt` 加解密
  - `public static byte[] fill(String psw)`
  - `public static String encode(String ori[, String psw])` 
  - `public static String decode(String ori[, String psw])` 失败返回 null
- `PswMD5` MD5加密
  - `public static String encrypt(String data)`
  - `public static String password_md5(String psw)` 加盐
- `Checker` 检验输入合法性等
- `FileHelper` 文件操作
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



`base`包  基础功能

- `Init` 初始化
  - `public static String login_md5 = null` 登录密码MD5加密
  - `public static boolean isValidate()` 密码存储是否正确
  - `public static boolean change_psw(String psw)` 修改密码



`mysql`包  数据库功能

- 



`ui`包  交互页面

- `Root` 主窗口
  - `public static void start_root()` 检查登录并启动程序

- `Login` 登录本程序的对话窗
  - `public boolean suc = false` 登录是否成功
  - `public Login()` 启动对话窗，进行登录，返回结果 `suc`
- `RootMenu` 主窗口的菜单栏
  - `public RootMenu(Root frame)` 

- `ChangePsw` 修改密码对话窗
  - `public ChangePsw(Root frame)` 




### 文件结构

data/ 数据文件

- `user.txt` 登录密码的MD5加密，密码为类包里的默认密码

- `validate.txt` 校验文本，为了防止删除data/强行初始化来破解程序而保留的文件，该文件必须与user.txt的类包默认密码AES加密一致，当 `user.txt` 为空时，必须与 `Empty&` 文本的类包默认密码AES加密一致

  缺陷：破解方法是直接下载发行版的`data/`复制替代即可

- `settings.txt` 数据库设置文本

  格式为一行一个信息，依次是：版本号、IP、端口、数据库名、用户名、密码(AES加密)、参数(一行一个)



### 变量


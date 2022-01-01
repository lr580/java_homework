package base;

import java.sql.*;
import mysql.*;
import ui.*;
import java.util.Vector;

public class DbLoader {
    private static PreparedStatement info_adder = null;
    private static PreparedStatement info_changer = null;
    public static int t_main = 0;// 主表
    public static int t_temp = 0;// 临时表
    public static int saved = 1;// 是否已保存
    public static Vector<String> backups = new Vector<>();// 备份编号
    public static int cnt_stu = 0;// 临时表累积学生数
    public static int cnt_sub = 0;// 临时表累积课程数
    public static int cnt_sco = 0;// 临时表累积成绩数

    public static void checkinit() {// 检查是否需要初始化，是则自动执行
        cr_info();
        init_info();
    }

    private static void cr_info() {// 不存在则创建全局信息表
        String cmd = "create table if not exists `info` (`id` int not null auto_increment, `key` varchar(20) not null, `value` int not null, primary key(`id`), unique(`key`)) engine=InnoDB default charset=utf8;";
        Ctrl.run(cmd);

    }

    public static void add_info(String key, int value) {
        try {
            info_adder.setString(1, key);
            info_adder.setInt(2, value);
            info_adder.executeUpdate();
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    public static void set_info(String key, int value) {
        try {
            info_changer.setInt(1, value);
            info_changer.setString(2, key);
            info_changer.executeUpdate();
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    private static int get_info(String key) {
        String cmd1 = "select `key`, `value` from info where `key`='" + key + "';";
        ResultSet res1 = Ctrl.query(cmd1);
        return Ctrl.getv(res1, "key", key, "value");
    }

    public static void del_info(String key) {
        String cmd = "delete from `info` where `key` = '" + key + "';";
        Ctrl.update(cmd);
    }

    private static void init_info() {// 初始化表格
        String cmd1 = "insert ignore into `info` (`key`, `value`) values (?, ?)";
        info_adder = Ctrl.pre(cmd1);

        String cmd2 = "update `info` set `value`= ? where `key` = ?";
        info_changer = Ctrl.pre(cmd2);

        String cmd0 = "select `key` from info where `key`='main';";
        ResultSet res0 = Ctrl.query(cmd0);
        if (!Ctrl.exists(res0, "key", "main")) {// 表格未初始化
            add_info("main", 1);
            add_info("temp", 2);
            add_info("saved", 1);
            add_info("backups", 0);
            add_info("top", 0);
            cr_table();
            cr_table();
            t_main = 1;
            t_temp = 2;
            saved = 1;
        } else {
            t_main = get_info("main");
            t_temp = get_info("temp");
            saved = get_info("saved");
            cnt_stu = get_info("a_" + t_temp);
            cnt_sub = get_info("b_" + t_temp);
            cnt_sco = get_info("c_" + t_temp);
            get_backups();
        }
        if (saved == 0) {
            Root.updateTitle();
        }
    }

    private static void cr_student(String name) {// 不存在则创建学生表
        String cmd = "create table if not exists `" + name
                + "` (`id` int not null auto_increment, `name` varchar(20) not null, `student_number` varchar(20) not null, `major` varchar(20), primary key(`id`)) engine=InnoDB default charset utf8";
        Ctrl.run(cmd);
    }

    private static void cr_subject(String name) {// 不存在则创建课程表
        String cmd = "create table if not exists `" + name
                + "` (`id` int not null auto_increment, `name` varchar(40) not null, `semester` varchar(10), primary key(`id`)) engine InnoDB default charset utf8;";
        Ctrl.run(cmd);
    }

    private static void cr_score(String name) {// 不存在则创建成绩表
        String cmd = "create table if not exists `" + name
                + "` (`id` int not null auto_increment, `student_id` int not null, `subject_id` int not null, `value` int not null, primary key(`id`)) engine InnoDB default charset utf8;";
        Ctrl.run(cmd);
    }

    private static void cr_table(int x) {
        cr_student("student_" + x);
        cr_subject("subject_" + x);
        cr_score("score_" + x);
        add_info("a_" + x, 0);
        add_info("b_" + x, 0);
        add_info("c_" + x, 0);
    }

    public static void cr_table() {
        int cnt = get_info("top") + 1;
        cr_table(cnt);
        set_info("top", cnt);
    }

    public static void del_table(int x) {
        Ctrl.run("drop table if exists `student_" + x + "`;");
        Ctrl.run("drop table if exists `subject_" + x + "`;");
        Ctrl.run("drop table if exists `score_" + x + "`;");
    }

    public static void overwrite(int from, int to) {
        del_table(to);
        Ctrl.run("create table `student_" + to + "` select * from `student_" + from + "`");
        Ctrl.run("create table `subject_" + to + "` select * from `subject_" + from + "`");
        Ctrl.run("create table `score_" + to + "` select * from `score_" + from + "`");
    }

    private static void update_saved() {
        set_info("saved", 1);
        saved = 1;
        Root.updateTitle();
    }

    public static void save() {
        overwrite(t_temp, t_main);
        update_saved();
    }

    public static void undo() {
        overwrite(t_main, t_temp);
        update_saved();
        DbTable.fresh();
    }

    private static void get_backups() {
        backups.clear();
        ResultSet res = Ctrl.query("select `key` from info where `key` like 'a_%'");
        try {
            while (res.next()) {
                String tmp = res.getString(1);
                tmp = tmp.substring(2);
                int v = Integer.valueOf(tmp);
                if (v == t_main || v == t_temp) {
                    continue;
                }
                backups.add(tmp);
            }
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }
}

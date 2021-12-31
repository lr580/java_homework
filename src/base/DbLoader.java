package base;

import java.sql.*;
import mysql.*;

public class DbLoader {
    private static PreparedStatement info_adder = null;

    public static void checkinit() {// 检查是否需要初始化，是则自动执行
        cr_info();
        init_info();
    }

    private static void cr_info() {// 不存在则创建全局信息表
        String cmd = "create table if not exists `info` (`id` int not null auto_increment, `key` varchar(20) not null, `value` int not null, primary key(`id`), unique(`key`)) engine=InnoDB default charset=utf8;";
        Ctrl.run(cmd);

    }

    private static void add_info(String key, int value) {
        try {
            info_adder.setString(1, key);
            info_adder.setInt(2, value);
            info_adder.executeUpdate();
        } catch (Exception e) {
            Ctrl.raised(e);
        }

    }

    private static void init_info() {// 初始化表格
        String cmd1 = "insert ignore into `info` (`key`, `value`) values (?, ?)";
        info_adder = Ctrl.pre(cmd1);
        String cmd0 = "select `key` from info where `key`='main';";
        ResultSet res0 = Ctrl.query(cmd0);
        if (!Ctrl.exists(res0, "key", "main")) {// 表格未初始化
            add_info("main", 1);
            add_info("temp", 2);
            add_info("saved", 1);
            add_info("backups", 0);
            cr_table(1);
            cr_table(2);
        }
    }

    private static void cr_student(String name) {// 不存在则创建学生表
        String cmd = "create table if not exists `" + name
                + "` (`id` int not null auto_increment, `name` varchar(20) not null, `student_id` varchar(20) not null, `major` varchar(20), primary key(`id`)) engine=InnoDB default charset utf8";
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

    public static void cr_table(int x) {
        cr_student("student_" + x);
        cr_subject("subject_" + x);
        cr_score("score_" + x);
        add_info("a_" + x, 0);
    }
}

package base;

import java.sql.*;
import mysql.*;
import ui.Root;

public class DbCtrl {
    private static StringBuilder diary = new StringBuilder();// 运行日志
    private static PreparedStatement s_add_stu = null;
    static {// 初始化
        s_add_stu = Ctrl.pre(
                "insert into `student_" + DbLoader.t_temp
                        + "` (`id`, `name`, `student_number`, `major`) values (?, ?, ?, ?)");
    }

    private static void change() {
        DbLoader.set_info("saved", 0);
        DbLoader.saved = 0;
        Root.updateTitle();
    }

    private static void change(String dia) {
        write_diary(dia);
        change();
    }

    public static void write_diary(String dia) {
        diary.append(dia);
        diary.append('\n');
    }

    public static int get_lastid() {// 暂时废置
        return Ctrl.getv("select last_insert_id();");
    }

    public static int add_stu(String name, String number, String major) {
        try {
            s_add_stu.setInt(1, ++DbLoader.cnt_stu);
            s_add_stu.setString(2, name);
            s_add_stu.setString(3, number);
            s_add_stu.setString(4, major);
            s_add_stu.executeUpdate();
            change("添加学生 (" + name + ", " + number + ", " + major + ")");
            return DbLoader.cnt_stu;
        } catch (Exception e) {
            Ctrl.raised(e);
            return -1;
        }
    }
}

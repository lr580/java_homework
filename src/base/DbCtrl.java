package base;

import java.sql.*;
import java.util.Date;
import mysql.*;
import plugin.FileHelper;
import ui.Root;
import java.io.File;

public class DbCtrl {
    private static StringBuilder diary = new StringBuilder();// 运行日志
    private static PreparedStatement s_add_stu = null;
    private static PreparedStatement s_upd_stu = null;
    private static File f_dir = new File("log/");
    private static File f_log = new File("log/" + getNow() + ".log");

    static {// 初始化
        s_add_stu = Ctrl.pre(
                "insert into `student_" + DbLoader.t_temp
                        + "` (`id`, `name`, `student_number`, `major`) values (?, ?, ?, ?)");
        s_upd_stu = Ctrl.pre(
                "update `student_" + DbLoader.t_temp + "` set `name`= ?, `student_number`=?, `major`=? where `id`=?");
        write_diary("启动程序");
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
        diary.append(getNow(1));
        diary.append(dia);
        diary.append('\n');
    }

    public static void save_diary() {
        write_diary("正常退出程序");
        diary.deleteCharAt(diary.length() - 1);// 删掉最后的换行
        if (!f_dir.exists()) {
            f_dir.mkdir();
        }
        FileHelper.write(diary.toString(), f_log);
    }

    public static int add_stu(String name, String number, String major) {
        try {
            s_add_stu.setInt(1, ++DbLoader.cnt_stu);
            s_add_stu.setString(2, name);
            s_add_stu.setString(3, number);
            s_add_stu.setString(4, major);
            s_add_stu.executeUpdate();
            DbLoader.set_info("a_" + DbLoader.t_temp, DbLoader.cnt_stu);
            change("添加学生 (" + name + ", " + number + ", " + major + ")");
            return DbLoader.cnt_stu;
        } catch (Exception e) {
            Ctrl.raised(e);
            return -1;
        }
    }

    public static void upd_stu(int id, String name, String number, String major) {
        try {
            s_upd_stu.setString(1, name);
            s_upd_stu.setString(2, number);
            s_upd_stu.setString(3, major);
            s_upd_stu.setInt(4, id);
            s_upd_stu.executeUpdate();
            change("修改学生" + id + "为 (" + name + ", " + number + ", " + major + ")");

        } catch (Exception e) {
            Ctrl.raised(e);
            return;
        }
    }

    private static String getNow() {// 获得当前时间
        Date date = new Date();
        return String.format("%tF-%tH-%tM-%tS", date, date, date, date);// 不能用冒号%tT
    }

    private static String getNow(int i) {// 获得当前时间
        return String.format("[%tT]", new Date());
    }

    public static void main(String[] args) {// 测试用例
        System.out.println(getNow());
    }
}

package ui;

import javax.swing.*;
import base.*;
import mysql.*;

public class Root extends JFrame {
    private final static String title = "学生成绩管理系统";
    private static Root that = null;

    public Root() {
        super(title);
        that = this;// 方便其他窗体类操作Root类
        setJMenuBar(new RootMenu(this));
        check_db_setting();
        getContentPane().add(new Page());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
    }

    public static void start_root() {
        Login login = new Login();
        if (login.suc) {
            login.dispose();
            new Root();
        } else {
            System.exit(0);
        }
    }

    private void check_db_setting() {
        if (!Init.is_inited_db()) {// 未配置过，先配置
            new SetDatabase(this);
        } else if (!Link.connect()) {// 已配置过，但自动连接失败
            JOptionPane.showMessageDialog(null, Link.err_msg);
            new SetDatabase(this);
        }
        DbLoader.checkinit();
    }

    public static void updateTitle() {
        that.setTitle(title + (DbLoader.saved == 1 ? "" : "*"));
    }

    public static void main(String[] args) {
        start_root();
    }
}

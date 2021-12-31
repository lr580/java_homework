package ui;

import java.awt.*;
import javax.swing.*;
import base.*;
import mysql.*;

public class Root extends JFrame {
    private final static String title = "学生成绩管理系统";

    public Root() {
        super(title);
        setJMenuBar(new RootMenu(this));
        check_db_setting();
        setLayout(new GridLayout(1, 1, 10, 10));
        getContentPane().add(new Page());
        // getContentPane().add(new Page(), BorderLayout.CENTER);
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

    public static void main(String[] args) {
        start_root();
    }
}

package ui;

import javax.swing.*;
import base.Init;
import mysql.Link;

public class Root extends JFrame {
    private final static String title = "学生成绩管理系统";

    public Root() {
        super(title);
        setJMenuBar(new RootMenu(this));
        check_db_setting();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400, 400);
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
        if (!Init.is_inited_db()) {
            new SetDatabase(this);
        } else if(!Link.connect()) {
            JOptionPane.showMessageDialog(null, Link.err_msg);
            new SetDatabase(this);
        }
    }

    public static void main(String[] args) {
        start_root();
    }
}

package ui;

import javax.swing.*;

public class Root extends JFrame {
    private final static String title = "学生成绩管理系统";

    public Root() {
        super(title);
        setJMenuBar(new RootMenu(this));
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

    public static void main(String[] args) {
        start_root();
    }
}

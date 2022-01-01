package ui;

import javax.swing.*;
import base.*;
import mysql.*;
import java.awt.event.*;

public class Root extends JFrame {
    private final static String title = "学生成绩管理系统";
    public static Root that = null;

    public Root() {
        super(title);
        that = this;// 方便其他窗体类操作Root类
        setJMenuBar(new RootMenu(this));
        check_db_setting();
        getContentPane().add(new Page());
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setSize(800, 600);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (DbLoader.saved == 0) {
                    int i = JOptionPane.showConfirmDialog(null, "是否保存当前修改", "提示",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.INFORMATION_MESSAGE);
                    if (i == JOptionPane.OK_OPTION) {// 保存
                        DbLoader.save();
                    } else if (i == JOptionPane.CLOSED_OPTION) {// 叉掉
                        return;
                    } else if (i == JOptionPane.CANCEL_OPTION) {// 不保存
                        DbLoader.undo();
                    }
                }
                DbCtrl.save_diary();
                dispose();
            }
        });
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

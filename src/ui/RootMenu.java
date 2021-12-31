package ui;

import java.awt.event.*;
import javax.swing.*;

public class RootMenu extends JMenuBar {
    public RootMenu(Root frame) {
        JMenu setting = new JMenu("设置");
        add(setting);

        JMenuItem reset_psw = new JMenuItem("修改登录密码");
        reset_psw.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ChangePsw(frame);
            }
        });

        JMenuItem reset_mysql = new JMenuItem("设置数据库连接");
        reset_mysql.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SetDatabase(frame);
            }
        });

        setting.add(reset_psw);
        setting.add(reset_mysql);
    }
}

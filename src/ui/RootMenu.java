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
        reset_mysql.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));

        setting.add(reset_mysql);
        setting.add(reset_psw);

        JMenu help = new JMenu("帮助");
        add(help);

        JMenuItem helpme = new JMenuItem("帮助");
        helpme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "暂无帮助");
            }
        });

        JMenuItem about = new JMenuItem("关于");
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "版本:V1.0");
            }
        });

        help.add(helpme);
        help.addSeparator();
        help.add(about);
    }
}

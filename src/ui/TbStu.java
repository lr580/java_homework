package ui;

import javax.swing.*;
import base.DbCtrl;
import plugin.FsLabel;
import java.awt.*;
import java.awt.event.*;

public class TbStu extends JPanel {
    public DbTable jt = null;
    private JTextField i_name = new JTextField(6);
    private JTextField i_number = new JTextField(14);
    private JTextField i_major = new JTextField(8);

    private ActionListener add_stu = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String name = i_name.getText();
            String number = i_number.getText();
            String major = i_major.getText();
            if (name.length() == 0 || number.length() == 0) {
                JOptionPane.showMessageDialog(null, "学号或姓名不能为空");
                return;
            }
            if (name.length() > 20) {
                JOptionPane.showMessageDialog(null, "姓名过长");
                return;
            }
            if (number.length() > 20) {
                JOptionPane.showMessageDialog(null, "学号过长");
                return;
            }
            if (major.length() > 20) {
                JOptionPane.showMessageDialog(null, "专业过长");
                return;
            }
            int id = DbCtrl.add_stu(name, number, major);
            String[] row = { Integer.toString(id), name, number, major};
            jt.addRow(row);
        }
    };

    public TbStu(DbTable jt) {
        this.jt = jt;
        setLayout(new GridLayout(2, 1, 5, 5));
        JPanel p_stu_uf = new JPanel(new FlowLayout(1, 8, 5));
        JPanel p_stu_df = new JPanel(new FlowLayout(1, 8, 5));
        add(p_stu_uf);
        add(p_stu_df);

        p_stu_uf.add(new FsLabel("姓名:"));
        p_stu_uf.add(i_name);
        p_stu_uf.add(new FsLabel("学号:"));
        p_stu_uf.add(i_number);
        p_stu_uf.add(new FsLabel("专业:"));
        p_stu_uf.add(i_major);

        JButton b_addstu = new JButton("添加学生");
        b_addstu.addActionListener(add_stu);
        i_name.addActionListener(add_stu);
        i_number.addActionListener(add_stu);
        i_major.addActionListener(add_stu);
        p_stu_df.add(b_addstu);
    }
}

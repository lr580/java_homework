package ui;

import javax.swing.*;
import base.*;
import java.awt.*;
import java.awt.event.*;
import plugin.FsLabel;

public class TbGlobal extends JPanel {
    public static DbTable jt = null;
    public static JComboBox<String> jc = new JComboBox<>(DbLoader.backups);

    public static ActionListener e_save = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            DbLoader.save();
        }
    };// 可以被菜单栏复用

    public static ActionListener e_undo = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            if (DbLoader.saved == 0) {
                int i = JOptionPane.showConfirmDialog(null, "这将会不可恢复地丢失尚未保存的全部更改,确认撤销吗", "提示",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);
                if (i != JOptionPane.OK_OPTION) {
                    return;
                }
                DbLoader.undo();
            }
        }
    };

    public static ActionListener e_backup = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

        }
    };

    public static ActionListener e_frombackup = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

        }
    };

    public static ActionListener e_delbackup = new ActionListener() {
        public void actionPerformed(ActionEvent e) {

        }
    };

    public TbGlobal(DbTable jt) {
        TbGlobal.jt = jt;
        setLayout(new GridLayout(2, 1, 5, 5));
        JPanel uf = new JPanel(new FlowLayout(1, 5, 5));
        add(uf);

        JButton b_save = new JButton("保存");
        b_save.addActionListener(e_save);
        uf.add(b_save);
        JButton b_undo = new JButton("撤销");
        b_undo.addActionListener(e_undo);
        uf.add(b_undo);

        JPanel df = new JPanel(new FlowLayout(1, 5, 5));
        add(df);

        df.add(new FsLabel("已有备份号:"));
        df.add(jc);

        JButton b_backup = new JButton("添加到备份");
        b_backup.addActionListener(e_backup);
        df.add(b_backup);

        JButton b_frombackup = new JButton("从备份还原");
        b_frombackup.addActionListener(e_frombackup);
        df.add(b_frombackup);

        JButton b_delbackup = new JButton("删除选中备份");
        b_delbackup.addActionListener(e_delbackup);
        df.add(b_delbackup);
    }
}

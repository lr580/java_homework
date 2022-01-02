package ui;

import javax.swing.*;
import javax.swing.event.*;
import base.DbLoader;

public class Tabbar extends JTabbedPane {
    public DbTable jt = null;// 当前面板操纵的表格

    public Tabbar(DbTable jt) {
        this.jt = jt;
        setTabPlacement(JTabbedPane.TOP);
        setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        addTab("学生管理", new TbStu(jt));
        addTab("课程管理", new TbSubj());
        addTab("成绩管理", new TbSco());
        addTab("全局", new TbGlobal(jt));

        addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int idx = getSelectedIndex();
                if (idx == 0) {
                    jt.render("select * from student_" + DbLoader.t_temp);
                } else if (idx == 1) {
                    jt.render("select * from subject_" + DbLoader.t_temp);
                } else if (idx == 2) {
                    jt.render("select * from score_" + DbLoader.t_temp);
                }
                DbTable.table_idx = idx;
            }
        });
    }
}

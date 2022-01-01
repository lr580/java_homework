package ui;

import javax.swing.*;
import javax.swing.table.*;

import base.DbLoader;
import mysql.Ctrl;
import java.awt.Font;
import java.sql.*;
import java.util.*;

public class DbTable extends JTable {
    private DefaultTableModel tm = new DefaultTableModel();
    private final static Map<String, String> h = new HashMap<>();// 列名英译中
    private String last_cmd = "";// 上一次渲染的语句
    private static DbTable that = null;

    public DbTable() {
        init_h();
        that = this;
        setModel(tm);
        DefaultTableCellRenderer crr = new DefaultTableCellRenderer();
        crr.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, crr);// 表格居中
        Font ft = new Font("宋体", Font.PLAIN, 16);
        setFont(ft);
        JTableHeader jh = getTableHeader();
        jh.setFont(ft);
        setTableHeader(jh);

        // ResultSet tmp = Ctrl.query();
        render("select * from student_" + DbLoader.t_temp);// 初始值
    }

    private void init_h() {
        h.put("id", "编号");
        h.put("name", "名字");
        h.put("student_id", "学生编号");
        h.put("student_number", "学号");
        h.put("major", "专业");
        h.put("semester", "学期");
        h.put("subject_id", "课程编号");
        h.put("value", "分数");
        h.put("key", "键");
    }

    private void render(ResultSet res) {
        try {
            ResultSetMetaData reso = res.getMetaData();
            int n = reso.getColumnCount();
            int ty[] = new int[n + 1];
            tm.setColumnCount(0);
            tm.setRowCount(0);
            for (int i = 1; i <= n; ++i) {
                tm.addColumn(h.get(reso.getColumnName(i)));
                ty[i] = reso.getColumnType(i);// 不判断直接全部getString也行
            }
            while (res.next()) {
                String row[] = new String[n];
                for (int i = 1; i <= n; ++i) {
                    if (ty[i] == 4) {// int
                        row[i - 1] = Integer.toString(res.getInt(i));
                    } else {// varchar
                        row[i - 1] = res.getString(i);
                    }
                }
                tm.addRow(row);
            }
        } catch (Exception e) {
            Ctrl.raised(e);
        }
    }

    public void render(String res) {
        last_cmd = res;
        render(Ctrl.query(res));
    }

    public void refresh() {
        render(last_cmd);
    }

    public static void fresh() {
        that.refresh();
    }

    public boolean isCellEditable(int row, int col) {
        return col > 0;
    }

    public void addRow(String[] row) {
        tm.addRow(row);
    }
}

package plugin;

import java.io.*;

public class FileHelper {// 封装读写文件
    public static String read(File f) {
        try {
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String tmp = "";
            while (null != (tmp = bfr.readLine())) {
                sb.append(tmp);
            }
            bfr.close();
            fr.close();
            return sb.toString();
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    public static String read(String f) {
        return read(new File(f));
    }

    public static boolean write(String t, File f) {
        try {
            FileWriter fw = new FileWriter(f);
            BufferedWriter bfw = new BufferedWriter(fw);
            bfw.write(t);
            bfw.close();
            fw.close();
            return true;
        } catch (Exception e) {
            // e.printStackTrace();
            return false;
        }
    }

    public static boolean write(String t, String f) {
        return write(t, new File(f));
    }

    public static boolean touch(File f){
        try{
            f.createNewFile();
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static boolean touch(String f){
        return touch(new File(f));
    }

    public static void main(String[] args) {// 测试用例
        write("你好吗？a", "a.txt");
        String t = read("a.txt");
        System.out.println(t);
        String v = read("b.txt");
        if (v == null) {
            System.out.println("文件不存在");
        }
    }
}

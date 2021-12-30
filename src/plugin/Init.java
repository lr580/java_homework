package plugin;

import java.io.*;

public class Init {
    private String login_md5 = null;// 登录密码

    // 是否已初始化
    public static boolean isInitialized() {
        boolean integrity = true;// 是否路径完整
        File f_dir = new File("data/");
        if (!f_dir.exists()) {
            f_dir.mkdir();
            integrity = false;
        }
        File f_psw = new File("data/user.txt");
        if (!f_psw.exists()) {
            f_psw.mkdir();
            integrity = false;
        }

        String psw_raw = FileHelper.read(f_psw);
        if (psw_raw == null) {
            integrity = false;
            System.out.println("aa!!");
            return false;
        }
        String psw = Encrypt.decode(psw_raw);
        
        if (psw.length() == 0) {
            System.out.println("a!");
        }

        return true;
    }

    public static void main(String[] args) {// 测试用例
        // System.out.println(Encrypt.decode("").length());
        isInitialized();
    }
}

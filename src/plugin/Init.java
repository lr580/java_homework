package plugin;

import java.io.*;

public class Init {
    private String login_md5 = null;// 登录密码

    // 是否已初始化
    public static boolean isInitialized() {
        boolean init = true;
        File f_dir = new File("data/");
        if (!f_dir.exists()) {
            f_dir.mkdir();
            init = false;
        }
        File f_psw = new File("data/user.txt");
        if (!f_psw.exists()) {
            f_psw.mkdir();
            init = false;
        }

        return true;
    }
}

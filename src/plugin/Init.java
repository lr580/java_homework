package plugin;

import java.io.*;

public class Init {
    // 是否已初始化
    public static boolean isInitialized() {
        File f_dir = new File("data/");
        if (!f_dir.exists()) {
            f_dir.mkdir();
        }
        return true;
    }
}

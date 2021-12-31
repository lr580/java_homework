package base;

import java.io.*;
import plugin.*;

/**
 * 为了防止直接删掉data/直接触发初始化来破解登录密码，采用如下策略：
 * 设登录密码的MD5加密存储在user.txt, user.txt文本的AES加密存在validate.txt
 * 如果读取user.txt并AES加密后与validate.txt不符合，直接报错，拒绝运行程序
 * 那么任何试图通过修改/删除user.txt, validate.txt来破解的行为都会拒绝运行程序
 * 特别地，无登录密码时用empty_token变量的AES加密与之检验
 */

public class Init {
    public static String login_md5 = null;// 登录密码
    private static String empty_token = "Empty&";// 空登录密码校验值
    // private static boolean validate = true;// 文件是否有效
    private static File f_dir = new File("data/");
    private static File f_psw = new File("data/user.txt");
    private static File f_vali = new File("data/validate.txt");

    private static boolean create_if_not_exist() {// 创建文件
        boolean integrity = true;// 是否路径完整
        if (!f_dir.exists()) {
            f_dir.mkdir();
            integrity = false;
        }
        if (!f_psw.exists()) {
            FileHelper.touch(f_psw);
            integrity = false;
        }
        if (!f_vali.exists()) {
            FileHelper.touch(f_vali);
            integrity = false;
        }
        return integrity;
    }

    // 是否文件正确
    public static boolean isValidate() {
        boolean integrity = create_if_not_exist();

        login_md5 = FileHelper.read(f_psw);
        String check_std = FileHelper.read(f_vali);
        if (check_std == null) {
            System.out.println("error: check_msg null");
            return false;
        }
        check_std = FileHelper.read(f_vali);
        if (check_std == null) {
            System.out.println("error: check_msg wrong");
            return false;
        }

        String check_psw = "";
        if (login_md5.length() == 0) {
            check_psw = Encrypt.encode(empty_token);
        } else {
            check_psw = Encrypt.encode(login_md5);
        }

        if (!check_psw.equals(check_std)) {
            // validate = false;
            return false;
        }
        return integrity;
    }

    // 创建正确登录密码文件
    public static boolean change_psw(String psw) {
        create_if_not_exist();
        if (psw.length() == 0) {
            psw = empty_token;
            FileHelper.write("", f_psw);
            login_md5 = "";
        } else {
            psw = PswMD5.password_md5(psw);
            FileHelper.write(psw, f_psw);
            login_md5 = psw;
        }
        FileHelper.write(Encrypt.encode(psw), f_vali);
        return true;
    }

    public static void main(String[] args) {// 测试用例
        // System.out.println(Encrypt.decode("").length());
        // change_psw("");
        // System.out.println("".equals(""));
        // String x = "", y = "";
        // System.out.println(x.equals(y));
        System.out.println(isValidate());
        // System.out.println(validate);
    }
}

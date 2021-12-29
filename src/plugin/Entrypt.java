package plugin;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Entrypt {
    // NOPadding
    private static final String transormation = "AES/CBC/PKCS5Padding";
    private static final String key = "1437580lr_CZGv1.";// 密码
    private static final byte key_b[] = key.getBytes();// 密码
    private static final String alg = "AES";
    private static Cipher cipher = null;
    private static SecretKeySpec keySpec = null;
    private static IvParameterSpec iv = null;
    static {// 初始化
        try {
            cipher = Cipher.getInstance(transormation);
            keySpec = new SecretKeySpec(key_b, alg);
            iv = new IvParameterSpec(key_b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encode(String ori) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
            byte[] res = cipher.doFinal(ori.getBytes());// "utf-8"不可
            return Base64Plugin.get(res);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String decode(String ori) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
            byte[] res = cipher.doFinal(Base64Plugin.from(ori));
            return new String(res);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static void main(String[] args) {// 测试用例
        String a = encode("awa你好");// 是否成功测试(含中文测试)
        System.out.println(a);
        System.out.println(decode(a));
        String b = encode("再见QwQ");// 可否复用测试
        System.out.println(b);
        System.out.println(decode(b));
    }
}

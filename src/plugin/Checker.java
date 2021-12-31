package plugin;

public class Checker {
    public static boolean lengthRange(String s, int min, int max) {
        return s.length() >= min && s.length() <= max;
    }
}

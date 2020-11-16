package core.Supports;

import java.util.regex.Pattern;

public class Validator {
    private static String emailPattern ="^[a-zA-Z][\\w-]+@([\\w]+\\.[\\w]+|[\\w]+\\.[\\w]{2,}\\.[\\w]{2,})$";

    public static boolean isEmail(String email){
        return Pattern.matches(emailPattern, email);
    }

    public static boolean isUsername(String username){
        return username.length()>=6;
    }

    public static boolean isPassword(String password){
        return password.length()>=6;
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }
}

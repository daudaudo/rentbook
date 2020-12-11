package core.Supports;

import java.text.Normalizer;

public class VNCharacterUtils {
    public static String removeAccent(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            return temp.replaceAll("\\p{M}", "");
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}

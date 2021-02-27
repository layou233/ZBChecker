package lau.ZBChecker.check;

import java.io.IOException;
import java.util.Locale;

public class BlockedProxyJudge {
    // Only applicable when the return value is not HTML
    public static void judge(String respondedString) throws IOException {
        if (!respondedString.contains("<html"))
            if (!respondedString.contains("www.w3.org"))
                if (!respondedString.toLowerCase(Locale.ROOT).contains("request blocked"))
                    if (!respondedString.equals("You are not allowed to access the document."))
                        if (!respondedString.equals(""))
                            return;
        throw new IOException("Proxy has been blocked");
    }
}

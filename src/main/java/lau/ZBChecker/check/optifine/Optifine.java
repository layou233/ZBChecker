package lau.ZBChecker.check.optifine;

import lau.ZBChecker.Account;
import lau.ZBChecker.Main;
import lau.ZBChecker.check.Get;

import java.io.IOException;

public class Optifine {
    public static Account cape(Account alt) throws IOException {
        final String response = Get.get("http://s.optifine.net/capes/" + alt.playerName, null, null);
        //BlockedProxyJudge.judge(response);
        if (!response.contains("Not found")) {
            alt.isOptifineCape = true;
            Main.counter.optifine++;
        }
        alt.checkedTimes++;
        return alt;
    }
}

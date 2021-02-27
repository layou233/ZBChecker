package lau.ZBChecker.check.minecraft;

import lau.ZBChecker.Account;
import lau.ZBChecker.Main;
import lau.ZBChecker.check.BlockedProxyJudge;
import lau.ZBChecker.check.Get;
import lau.ZBChecker.utils.ReadJsonValue;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;

import java.io.IOException;

public class Minecraft {
    public static Account checkAccount(Account alt, HttpHost proxy) throws IOException {
        final String response = Raw.auth(alt.user, alt.password, proxy);
        BlockedProxyJudge.judge(response);
        if (!response.contains("ForbiddenOperationException")) {
            if (response.contains("{\"user\"")) {
                alt.isAccessible = true;
                alt.accessToken = ReadJsonValue.readString(response, "accessToken");
                if (response.contains("selectedProfile")) {
                    alt.isPremium = true;
                    final String selectedProfile = ReadJsonValue.cutObjString(response, "selectedProfile");
                    alt.playerName = ReadJsonValue.readString(selectedProfile, "name");
                    alt.uuid = ReadJsonValue.readString(selectedProfile, "id");
                }
            }
        }
        alt.checkedTimes++;
        return alt;
    }

    public static Account checkSecurity(Account alt, HttpHost proxy) throws IOException {
        final String response = Raw.security(alt.accessToken, proxy);
        alt.checkedTimes++;
        BlockedProxyJudge.judge(response);
        if (response.equals("[]")) {
            alt.isSFA = true;
            Main.counter.sfa++;
        } else Main.counter.nfa++;
        return alt;
    }

    public static Account mojangCape(Account alt) throws IOException {
        final String response = Get.get("https://crafatar.com/capes/" + alt.uuid, new BasicHeader("user-agent", "Mozilla/5.0"), null);
        alt.checkedTimes++;
        BlockedProxyJudge.judge(response);
        if (response.contains("png")) {
            alt.isMojangCape = true;
            Main.counter.mojangCape++;
        }
        return alt;
    }
}

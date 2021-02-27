package lau.ZBChecker.check.hypixel;

import lau.ZBChecker.Account;
import lau.ZBChecker.Main;
import lau.ZBChecker.check.BlockedProxyJudge;
import lau.ZBChecker.check.Get;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hypixel {
    public static String getRaw(Account alt) throws IOException {
        final String response = Get.get("https://api.slothpixel.me/api/players/" + alt.uuid, null, null);
        alt.checkedTimes++;
        BlockedProxyJudge.judge(response);
        return response;
    }

    public static Account rank(Account alt) {
        if (alt.hypixelRaw.contains("rank\":null")) alt.hypixelRank = "DEFAULT";
        else if (alt.hypixelRaw.contains("rank\":VIP")) alt.hypixelRank = "VIP";
        else if (alt.hypixelRaw.contains("rank\":VIP_PLUS")) alt.hypixelRank = "VIP+";
        else if (alt.hypixelRaw.contains("rank\":MVP")) alt.hypixelRank = "MVP";
        else if (alt.hypixelRaw.contains("rank\":MVP_PLUS")) alt.hypixelRank = "MVP+";
        else if (alt.hypixelRaw.contains("rank\":MVP_PLUS_PLUS")) alt.hypixelRank = "MVP++";
        else if (alt.hypixelRaw.contains("rank\":YOUTUBER")) alt.hypixelRank = "YOUTUBER";
        else if (alt.hypixelRaw.contains("rank\":HELPER")) alt.hypixelRank = "HELPER";
        else if (alt.hypixelRaw.contains("rank\":ADMIN")) alt.hypixelRank = "ADMIN";
        else alt.hypixelRank = "unknown";
        // This API does not seem to have fancy ranks such as PIG+++, OWNER
        if (!alt.hypixelRank.equals("DEFAULT")) Main.counter.hypixelRanked++;
        return alt;
    }

    public static Account level(Account alt) {
        Matcher m = Pattern.compile("level\":[0-9]+(.[0-9]+)?,").matcher(alt.hypixelRaw);
        if (m.find()) {
            final String matchResult = m.group(0);
            alt.hypixelLevel = Float.parseFloat(matchResult.substring(7, matchResult.length() - 1));

            if ((Main.config.sep_hypixelHighLevel != -1) && (alt.hypixelLevel >= Main.config.sep_hypixelHighLevel)) {
                alt.isHypixelHighLevel = true;
                Main.counter.hypixelLeveled++;
            } else if ((Main.config.sep_hypixelMiddleLevel != -1) && (alt.hypixelLevel >= Main.config.sep_hypixelMiddleLevel)) {
                alt.isHypixelMiddleLevel = true;
                Main.counter.hypixelMidLeveled++;
            }
        }
        return alt;
    }
}

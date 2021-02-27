package lau.ZBChecker.threads;

import lau.ZBChecker.Main;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.DecimalFormat;

public class StatusLogThread extends Thread {
    private final transient Log log = LogFactory.getLog("Status");

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(300000);
                log.warn("Hits:" + Main.counter.hits + "(" + (new DecimalFormat("#.####")).format(((double) Main.counter.hits) * 100 / Main.counter.checked) + "%)"
                        + (Main.counter.nfa > 0 ? " NFA:" + Main.counter.nfa : "")
                        + (Main.counter.sfa > 0 ? " SFA:" + Main.counter.sfa : "")
                        + (Main.counter.demo > 0 ? " Demo:" + Main.counter.demo : "")
                        + " | Left/Total:" + (Main.comboList.size() - Main.counter.checked) + "/" + Main.comboList.size()
                        + (Main.counter.hypixelLeveled > 0 ? " HypixelHighLevel:" + Main.counter.hypixelLeveled : "")
                        + (Main.counter.hypixelRanked > 0 ? " HypixelRanked:" + Main.counter.hypixelRanked : "")
                        + (Main.counter.optifine > 0 ? " Optifine Cape:" + Main.counter.optifine : "")
                        + (Main.counter.mojangCape > 0 ? " Mojang Cape:" + Main.counter.mojangCape : "")
                        + " Proxies:" + Main.proxyList.size()
                        + " CPM:" + Main.CPM
                );
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

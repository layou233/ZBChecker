package lau.ZBChecker;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.text.DecimalFormat;

public class Windows {
    public interface CLibrary extends Library {
        CLibrary INSTANCE = Native.load((Platform.isWindows() ? "kernel32" : "c"), CLibrary.class);

        boolean SetConsoleTitleA(String title);
    }

    public static void setTitle(String title) {
        try {
            if (Platform.isWindows())
                CLibrary.INSTANCE.SetConsoleTitleA(title);
        } catch (Exception ignored) { // Ignore exception that caused by not running on Windows NT systems.
        }
    }

    public static void refreshTitle() {
        setTitle("Z " + UpdateCheck.version
                + " | Hits:" + Main.counter.hits + "(" + (new DecimalFormat("#.####")).format(((double) Main.counter.hits) * 100 / Main.counter.checked) + "%)"
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
    }
}

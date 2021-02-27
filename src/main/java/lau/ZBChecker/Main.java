package lau.ZBChecker;

import lau.ZBChecker.threads.CPMCalculatorThread;
import lau.ZBChecker.threads.CheckThread;
import lau.ZBChecker.threads.RefreshApiProxyThread;
import lau.ZBChecker.threads.StatusLogThread;
import lau.ZBChecker.utils.LoadAsList;
import lau.ZBChecker.utils.LoadFileResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fusesource.jansi.AnsiConsole;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import static org.fusesource.jansi.Ansi.ansi;

public class Main {
    public static Config config = new Config();
    public static boolean isCloudMode = false;
    public static transient Log log = LogFactory.getLog("Main");
    public static long totalTriedTimes = 0;
    public static List<String> proxyList = new ArrayList<>();
    public static List<String> comboList = new ArrayList<>();
    public static Counter counter = new Counter();
    public static String fileFolderName = "./results/" + (new Date()).toString().replace(':', '-');
    public static int totalThreads = 0;
    public static int CPM = 0;

    public static void main(String[] args) {
        Windows.setTitle("ZBChecker " + UpdateCheck.version + " | Github@layou233");
        AnsiConsole.systemInstall();
        System.out.println(ansi().render("@|magenta " +
                " ______  _____   _____   _   _   _____   _____   _   _    _____   _____  \n" +
                "|___  / |  _  \\ /  ___| | | | | | ____| /  ___| | | / /  | ____| |  _  \\ \n" +
                "   / /  | |_| | | |     | |_| | | |__   | |     | |/ /   | |__   | |_| | \n" +
                "  / /   |  _  { | |     |  _  | |  __|  | |     | |\\ \\   |  __|  |  _  / \n" +
                " / /__  | |_| | | |___  | | | | | |___  | |___  | | \\ \\  | |___  | | \\ \\ \n" +
                "/_____| |_____/ \\_____| |_| |_| |_____| \\_____| |_|  \\_\\ |_____| |_|  \\_\\|@ \n" +
                "@|yellow By Github@layou233|@\n"));
        UpdateCheck.updateCheck();
        config.loadConfig();
        String text = LoadFileResource.loadFile("combos.txt").trim();
        if (!Objects.equals(text, "")) isCloudMode = true;

        //TODO(Cloud Mode)
        if (isCloudMode) {
            log.warn("Oh, you are using cloud mode!!!");
            // Set printBad to false
            config.printBad = false;
            config.logStatus = true;

            // Load proxies
            RefreshApiProxyThread.loadProxyFromApi();
            (new RefreshApiProxyThread()).start();

            // Load combos
            comboList = Arrays.asList(text.replace("\r\n", "\n").replace('\r', '\n').split("\n"));
        } else {
            // Load proxies
            if (config.useProxyApi) {
                RefreshApiProxyThread.loadProxyFromApi();
                (new RefreshApiProxyThread()).start();
            } else {
                try {
                    proxyList = LoadAsList.load(config.proxyFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    log.fatal(config.proxyFile.getName() + " is not found. Please recheck your proxy file name!");
                    System.exit(0);
                }
                log.warn(ansi().render("@|green Successfully loaded " + Main.proxyList.size() + " proxies.|@"));
            }

            // Load combos
            try {
                comboList = LoadAsList.load(config.comboFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                log.fatal(config.comboFile.getName() + " is not found. Please recheck your combo file name!");
                System.exit(0);
            }
        }
        log.warn(ansi().render("@|green Successfully loaded " + comboList.size() + " combos.|@\n"));

        // Judge if there is no proxy/combo
        if (comboList.size() == 0) {
            log.fatal("Wait, but there is no combo to check in your file?\nPlease recheck your combo file.");
            System.exit(0);
        }
        if (proxyList.size() == 0) {
            log.fatal("Wait, but there is no proxy to use in your file?\nPlease recheck your proxy file.");
            System.exit(0);
        }

        (new File(fileFolderName)).mkdirs();
        log.warn(ansi().render("@|red Preparing finished. Now starting checking threads...|@\n"));

        CPMCalculatorThread cpmThread = new CPMCalculatorThread();
        cpmThread.start();
        Windows.refreshTitle();
        StatusLogThread statusThread = null; // Saving 1 KB of memory is also a saving of memory XD
        if (Main.config.logStatus) {
            statusThread = new StatusLogThread();
            statusThread.start();
        }
        for (String anCombo : comboList) {
            if (anCombo.trim().equals("")) break;
            while (totalThreads >= config.threads) ;
            (new CheckThread(new Account(anCombo.trim()))).start();
            totalThreads++;
        }

        while (counter.checked < comboList.size()) ;

        // Stop function threads
        try {
            cpmThread.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            statusThread.wait();
        } catch (Exception ignored) { // Actually I don’t want to deal with these unnecessary errors anymore
        }

        // Final print
        System.out.println("\n@|bg_red ==============="
                + "\nALL WORKS FINISHED|@"
                + "\n@|cyan Total: " + counter.checked
                + "\n@|green Hits: " + counter.hits
                + "\n@|green +NFA: " + counter.nfa
                + "\n@|green +SFA: " + counter.sfa
                + "\n@|magenta HypixelHighLevel: " + counter.hypixelLeveled
                + "\n@|magenta HypixelRanked: " + counter.hypixelRanked
                + "\n@|magenta Optifine Cape: " + counter.optifine
                + "\n@|magenta Mojang Cape: " + counter.mojangCape
                + "\n@|blue Demo: " + counter.demo
                + "\n@|yellow Thanks for using! ZBChecker, made by Github@layou233.|@");
    }
}

package lau.ZBChecker;

import lau.ZBChecker.utils.LoadFileResource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Locale;

import static org.fusesource.jansi.Ansi.ansi;

public class Config {
    public static transient Log log = LogFactory.getLog(Config.class);
    //public boolean isColorful;
    public String proxyType;
    public boolean useProxyApi;
    public String proxyApi;
    public int refreshDelay;
    public File proxyFile;
    public File comboFile;
    public int threads;
    public int connectionTimeout;
    public int recheckTimes;
    public boolean printBad;
    public boolean logStatus;
    public boolean check_optifine;
    public boolean check_mojangCape;
    public boolean check_hypixelInfo;
    public boolean check_skyblockMoney;
    public int sep_hypixelMiddleLevel;
    public int sep_hypixelHighLevel;

    protected static String readConfig(String file, String key) throws IndexOutOfBoundsException {
        final int indexOfValue = file.indexOf(key) + key.length();
        if (indexOfValue == -1)
            throw new StringIndexOutOfBoundsException("Value of " + key + " is not found in config file.");
        return file.substring(indexOfValue, file.indexOf(";", indexOfValue)).trim();
    }

    public static void generateNewConfig(File configFile) {
        try {
            configFile.createNewFile();
            FileWriter configFileWriter = new FileWriter(configFile.getName(), false);
            configFileWriter.write(LoadFileResource.loadFile("ZBChecker.properties"));
            configFileWriter.close();
        } catch (IOException e) {
            log.fatal("You should run this program under Administrator permission.");
            System.exit(0);
        }
    }

    private static boolean parseBoolean(String str) throws Exception {
        str = str.toLowerCase(Locale.ROOT).trim();
        if (str.equals("true")) return true;
        else if (str.equals("false")) return false;
        else throw new Exception("Error when parsing boolean type from config.");
    }

/*    private static Proxy.Type parseProxyType(String str) throws Exception {
        str = str.toLowerCase(Locale.ROOT).trim();
        if (str.equals("http")||str.equals("https")) return Proxy.Type.HTTP;
        else if (str.equals("socks4")||str.equals("socks5")) return Proxy.Type.SOCKS;
        else throw new Exception("Error when parsing proxy type from config.");
    }*/

    public void loadConfig() {
        final File configFile = new File("ZBChecker.properties");
        try {
            // Read config file
            FileInputStream fileInputStream = new FileInputStream(configFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String text = null;
            while (true) {
                try {
                    if ((text = bufferedReader.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sb.append(text);
            }
            text = sb.toString();

            // Parse config file
            try {
                //this.isColorful = parseBoolean(readConfig(text, "isColorful="));
                this.proxyType = readConfig(text, "proxyType=").toLowerCase(Locale.ROOT);
                this.useProxyApi = parseBoolean(readConfig(text, "useProxyApi="));
                this.proxyApi = readConfig(text, "proxyApi=");
                this.refreshDelay = Integer.parseInt(readConfig(text, "refreshDelay="));
                this.proxyFile = new File(readConfig(text, "proxyFilename="));
                this.comboFile = new File(readConfig(text, "comboFilename="));
                this.threads = Integer.parseInt(readConfig(text, "threads="));
                this.connectionTimeout = Integer.parseInt(readConfig(text, "connectionTimeout="));
                this.recheckTimes = Integer.parseInt(readConfig(text, "recheckTimes="));
                this.printBad = parseBoolean(readConfig(text, "printBad="));
                this.logStatus = parseBoolean(readConfig(text, "logStatus="));
                this.check_optifine = parseBoolean(readConfig(text, "optifine="));
                this.check_mojangCape = parseBoolean(readConfig(text, "mojangCape="));
                this.check_hypixelInfo = parseBoolean(readConfig(text, "hypixelInfo="));
                this.check_skyblockMoney = parseBoolean(readConfig(text, "skyblockMoney="));
                this.sep_hypixelMiddleLevel = Integer.parseInt(readConfig(text, "hypixelMiddleLevel="));
                this.sep_hypixelHighLevel = Integer.parseInt(readConfig(text, "hypixelHighLevel="));
            } catch (Exception e) {
                // No matter what the problem occurs, it will prompt an error check
                e.printStackTrace();
                log.error(ansi().render("@|red FOUND ERROR WHEN LOADING CONFIG FILE.\nPlease recheck it or delete it to automatically generate a new one.|@"));
                System.exit(0);
            }
        } catch (FileNotFoundException e) {
            log.warn(ansi().render("@|red Config file not found. Generating a new one...|@"));
            generateNewConfig(configFile);
            loadConfig(); // And then reload it by recurring this function
        }
    }
}

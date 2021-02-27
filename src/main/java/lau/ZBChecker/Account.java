package lau.ZBChecker;

public class Account {
    public Account(String combo) {
        this.originalCombo = combo;
    }

    public String originalCombo;
    public String user = null;
    public String password = null;
    public String uuid = null;
    public String accessToken = null;
    public String playerName = null;

    public int checkedTimes = 0;
    public boolean isAccessible = false;
    public boolean isPremium = false;
    public boolean isSFA = false;

    public boolean isMojangCape = false;
    public boolean isOptifineCape = false;

    public String hypixelRank = null;
    public float hypixelLevel = 0;
    public boolean isHypixelHighLevel = false;
    public boolean isHypixelMiddleLevel = false;
    public int skyblockMoney = -1;

    public String hypixelRaw = null;
}

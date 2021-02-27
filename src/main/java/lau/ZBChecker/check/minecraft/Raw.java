package lau.ZBChecker.check.minecraft;

import lau.ZBChecker.check.Get;
import lau.ZBChecker.check.Post;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;

import java.io.IOException;

public class Raw {
    //public final static String headers = "{\"Content-Type\": \"application/json\"}";

    public static String auth(String user, String password, HttpHost proxy) throws IOException {
        return Post.post("https://authserver.mojang.com/authenticate",
                "{\"agent\":{\"name\":\"Minecraft\",\"version\":1},\"username\":\"" + user
                        + "\",\"password\":\"" + password
                        + "\",\"requestUser\":\"true\"}", proxy);
    }

/*    public static String uuid(String playerName) throws IOException {
        return Get.get("https://api.mojang.com/profiles/minecraft/" + playerName, null);
    }*/

    public static String security(String token, HttpHost proxy) throws IOException {
        return Get.get("https://api.mojang.com/user/security/challenges", new BasicHeader("Authorization", "Bearer " + token), proxy);
    }
}

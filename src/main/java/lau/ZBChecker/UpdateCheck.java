package lau.ZBChecker;

import lau.ZBChecker.utils.LoadFileResource;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class UpdateCheck {
    public final static String version = LoadFileResource.loadFile("version.txt");

    public static void updateCheck() {
        System.out.println("Version " + version);
        String remoteVersion;
        try {
            CloseableHttpResponse httpClient = HttpClients.createDefault().execute(new HttpGet("https://raw.githubusercontent.com/layou233/NovelDL/master/src/main/resources/version.txt"));
            remoteVersion = EntityUtils.toString(httpClient.getEntity(), "UTF-8");
            httpClient.close();
        } catch (IOException e) {
            System.out.println("FAILED TO CHECK FOR UPDATES.\n" +
                    "You can check it yourself at https://github.com/layou233/NovelDL/releases\n");
            return;
        }
        if (version.equals(remoteVersion))
            System.out.println("Nice! You are using the latest version!\n");
        else System.out.println("THE ZBChecker YOU ARE CURRENTLY USING HAS BEEN OUTDATED!\n" +
                "Check out newer versions at https://github.com/layou233/NovelDL/releases\n");
    }
}
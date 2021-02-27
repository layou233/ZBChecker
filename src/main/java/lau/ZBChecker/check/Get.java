package lau.ZBChecker.check;

import lau.ZBChecker.Main;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Get {
    public static String get(String url, Header headers, HttpHost proxy) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeader("Content-Type", "application/json;charset=utf8");
        if (headers != null) httpGet.addHeader(headers);
        RequestConfig.Builder config = RequestConfig.custom();
        config.setConnectionRequestTimeout(Main.config.connectionTimeout).setConnectTimeout(Main.config.connectionTimeout);
        if (proxy != null) config.setProxy(proxy);
        httpGet.setConfig(config.build());
        CloseableHttpResponse httpClient = HttpClients.createDefault().execute(httpGet);
        String result = EntityUtils.toString(httpClient.getEntity(), "UTF-8");
        httpClient.close();
        return result;
    }
}

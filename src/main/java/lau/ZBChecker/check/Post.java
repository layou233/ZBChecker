package lau.ZBChecker.check;

import lau.ZBChecker.Main;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Post {
    public static String post(String url, String data, HttpHost proxy) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(data, "UTF-8"));
        RequestConfig.Builder config = RequestConfig.custom();
        config.setConnectionRequestTimeout(Main.config.connectionTimeout).setConnectTimeout(Main.config.connectionTimeout);
        if (proxy != null) config.setProxy(proxy);
        httpPost.setConfig(config.build());
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity responseEntity = response.getEntity();
        String result = EntityUtils.toString(responseEntity);
        try {
            // Close
            httpClient.close();
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}

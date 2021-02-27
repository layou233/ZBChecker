package lau.ZBChecker.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class LoadFileResource {
    public static String loadFile(String fileName) {
        String log = null;
        try {
            InputStream is = LoadFileResource.class.getClassLoader().getResourceAsStream(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            log = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return log;
    }
}

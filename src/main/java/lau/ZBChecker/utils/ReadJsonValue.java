package lau.ZBChecker.utils;

import java.io.IOException;

public class ReadJsonValue {
    public static String readString(String json, String key) throws IOException {
        int index1 = json.indexOf(key + "\":") + 1;
        int index2 = json.indexOf("\"", index1 + key.length() + 2);
        return json.substring(index1 + key.length() + 2, index2);
    }

/* This used to get Hypixel level from api raw responded string, but it is unstable so use regex instead.
    public static int readIntFromFloatValue(String json, String key) {
        int index1 = json.indexOf(key + "\":");
        int index2 = json.indexOf(".", index1 + 1);
        return Integer.parseInt(json.substring(index1 + key.length() + 2, index2));
    }
*/

    public static String cutObjString(String json, String key) {
        int index1 = json.indexOf(key + "\":{");
        int index2 = json.indexOf("}", index1 + 1);
        return json.substring(index1, index2);
    }
}

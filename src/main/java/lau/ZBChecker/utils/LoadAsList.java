package lau.ZBChecker.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LoadAsList {
    public static List<String> load(File file) throws FileNotFoundException {
        List<String> result = new ArrayList<String>();
        if (file.exists()) {
            try {
                FileReader fr = new FileReader(file);
                BufferedReader bf = new BufferedReader(fr);
                String str;
                while ((str = bf.readLine()) != null) {
                    if ((null != str) && (!str.trim().equals(""))) {
                        result.add(str);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        } else throw new FileNotFoundException(file.getName() + " not found.");
    }
}

import lau.ZBChecker.check.Get;

public class GetApiProxy {
    public static void main(String[] args){
        try {
            String[] proxies=Get.get("https://api.proxyscrape.com/v2/?request=getproxies&protocol=http&timeout=10000&country=all",null,null).split("\n");
            System.out.println(proxies.length);
            //System.out.println(proxies.);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

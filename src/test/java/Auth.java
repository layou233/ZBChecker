import lau.ZBChecker.check.minecraft.Raw;

public class Auth {
    public static void main(String[] args) throws Exception {
        /*
            Demos:
            adamjonsons@mail.ru:1230123f
            ketrabbartek1@o2.pl:Grabowski1!
            ivanbaranov1999@bk.ru:Ivan10022003
         */
        System.out.println("Premium:");
        System.out.println(Raw.auth("stephanwow@live.dk", "Stobie22!", null) + "\n");
        System.out.println("Demo:");
        System.out.println(Raw.auth("adamjonsons@mail.ru", "1230123f", null) + "\n");
        System.out.println("Bad1:");
        System.out.println(Raw.auth("mojang", "nmsl", null) + "\n");
        System.out.println("Bad2:");
        System.out.println(Raw.auth("mojang@cnmb.loser", "nmsl", null) + "\n");
    }
}

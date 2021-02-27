package lau.ZBChecker.threads;

import lau.ZBChecker.Main;
import lau.ZBChecker.Windows;

public class CPMCalculatorThread extends Thread {
    @Override
    public void run() {
        while (true) {
            final int lastCheckedCount = Main.counter.checked;
            try {
                Thread.sleep(3000);
                Main.CPM = (Main.counter.checked - lastCheckedCount) * 20;
                Windows.refreshTitle();
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

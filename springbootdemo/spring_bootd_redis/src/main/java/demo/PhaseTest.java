package demo;

import java.util.concurrent.Phaser;

/**
 * user:zxp
 * Day:2020,03,16
 **/
public class PhaseTest {

    public static void main(String[] args) throws InterruptedException {
        Phaser phaser = new Phaser();
        for (int i = 0; i < 10; i++) {
            phaser.register();// 注册各个参与者线程
            new Thread(new Task(phaser), "Thread-" + i).start();
            Thread.sleep(10000);
        }
        Thread.sleep(100000);
        phaser.register();// 注册各个参与者线程
    }
}

class Task implements Runnable {
    private final Phaser phaser;

    Task(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.println("==============");
        int i = phaser.arriveAndAwaitAdvance();     // 等待其它参与者线程到达
        // do something
        System.out.println(phaser.getUnarrivedParties());

        System.out.println(Thread.currentThread().getName() + ":000，当前phaser =" + i + "");
    }
}
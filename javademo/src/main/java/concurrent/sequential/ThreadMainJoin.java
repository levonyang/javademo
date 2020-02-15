package concurrent.sequential;

/**
 * user:zxp
 * Day:2020,02,14
 * Description:主线程 等待子线程一个完成任务，开启另外一个任务
 **/
public class ThreadMainJoin {
    public static void main(String[] args) throws InterruptedException {
        final Thread threadSalesperson = new Thread(new Runnable() {
            public void run() {
                System.out.println("有客户了");
            }
        }, "Sales Persion");
        final Thread threadProductManager = new Thread(new Runnable() {
            public void run() {
                System.out.println("产品经理规划需求！");
            }
        }, "Product Manager");
        final Thread threadProgrammer = new Thread(new Runnable() {
            public void run() {
                System.out.println("程序员开始器敲代码!");
            }
        }, "Programemr");
        threadSalesperson.start();
        threadSalesperson.join();
        threadProductManager.start();
        threadProductManager.join();
        threadProgrammer.start();
        threadProgrammer.join();
        System.out.println("完成了！");


    }
}

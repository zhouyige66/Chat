import java.util.concurrent.CountDownLatch;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/8/9 11:26 上午
 * @Version: v1.0
 */
public class CountDownLatchDemo {
    // Thread join方法前置条件是调用join()的线程需要先调用start()
    public static void main(String[] args) {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch2 = new CountDownLatch(1);
        A a = new A(countDownLatch);
        B b = new B(countDownLatch,countDownLatch2);
        C c = new C(countDownLatch2);
        c.start();
        b.start();
        a.start();
    }

    public static class A extends Thread {
        CountDownLatch countDownLatch;

        public A(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println("A执行了");
            countDownLatch.countDown();
        }
    }

    public static class B extends Thread {
        CountDownLatch countDownLatch;
        CountDownLatch countDownLatch2;

        public B(CountDownLatch countDownLatch,CountDownLatch countDownLatch2) {
            this.countDownLatch = countDownLatch;
            this.countDownLatch2 = countDownLatch2;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("B执行了");
            countDownLatch2.countDown();
        }
    }

    public static class C extends Thread {
        CountDownLatch countDownLatch;

        public C(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("C执行了");
        }
    }

}

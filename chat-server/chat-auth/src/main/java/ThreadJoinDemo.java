/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/8/9 11:26 上午
 * @Version: v1.0
 */
public class ThreadJoinDemo {

    // Thread join方法前置条件是调用join()的线程需要先调用start()
    public static void main(String[] args) {
        A a = new A();
        B b = new B(a);
        C c = new C(b);
        a.start();
        b.start();
        c.start();
    }

    public static class A extends Thread {
        @Override
        public void run() {
            System.out.println("A执行了");
        }
    }

    public static class B extends Thread {
        Thread preThread;

        public B(Thread preThread) {
            this.preThread = preThread;
        }

        @Override
        public void run() {
            try {
                preThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("B执行了");
        }
    }

    public static class C extends Thread {
        Thread preThread;

        public C(Thread preThread) {
            this.preThread = preThread;
        }

        @Override
        public void run() {
            try {
                preThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("C执行了");
        }
    }

}

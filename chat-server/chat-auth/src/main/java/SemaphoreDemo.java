import java.util.concurrent.Semaphore;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/8/8 8:30 下午
 * @Version: v1.0
 */
public class SemaphoreDemo {
    private Semaphore a = new Semaphore(1);
    private Semaphore b = new Semaphore(0);
    private Semaphore c = new Semaphore(0);

    public void a() {
        try {
            a.acquire();
            System.out.println("a执行");
            b.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void b() {
        try {
            b.acquire();
            System.out.println("b执行");
            c.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void c() {
        try {
            c.acquire();
            System.out.println("c执行");
            a.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SemaphoreDemo condition = new SemaphoreDemo();
        A a = new A(condition);
        B b = new B(condition);
        C c = new C(condition);
        new Thread(c).start();
        new Thread(b).start();
        new Thread(a).start();
        new Thread(c).start();
        new Thread(a).start();
        new Thread(b).start();
        new Thread(b).start();
        new Thread(a).start();
        new Thread(c).start();
    }

    public static class A implements Runnable {
        private SemaphoreDemo condition;

        public A(SemaphoreDemo condition) {
            this.condition = condition;
        }

        @Override
        public void run() {
            condition.a();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static class B implements Runnable {
        private SemaphoreDemo condition;

        public B(SemaphoreDemo condition) {
            this.condition = condition;
        }

        @Override
        public void run() {
            condition.b();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static class C implements Runnable {
        private SemaphoreDemo condition;

        public C(SemaphoreDemo condition) {
            this.condition = condition;
        }

        @Override
        public void run() {
            condition.c();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

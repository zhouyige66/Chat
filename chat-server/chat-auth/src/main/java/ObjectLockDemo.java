/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/8/8 10:04 下午
 * @Version: v1.0
 */
public class ObjectLockDemo {
    private int signal;

    public synchronized void a() {
        System.out.println("调用a");
        while (signal != 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("a" + signal);
        signal++;
        notifyAll();
    }

    public synchronized void b() {
        System.out.println("调用b");
        while (signal != 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("b" + signal);
        signal++;
        notifyAll();
    }

    public synchronized void c() {
        System.out.println("调用c");
        while (signal != 2) {
            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("c" + signal);
        signal = 0;
        notifyAll();
    }

    public static void main(String[] args) {
        ObjectLockDemo d = new ObjectLockDemo();
        A a = new A(d);
        B b = new B(d);
        C c = new C(d);

        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(c).start();
        new Thread(a).start();
        new Thread(a).start();
        new Thread(b).start();
        new Thread(a).start();
        new Thread(b).start();
        new Thread(a).start();
        new Thread(b).start();
        new Thread(b).start();
        new Thread(b).start();
    }

    public static class A implements Runnable {
        private ObjectLockDemo demoCondition;

        public A(ObjectLockDemo demo) {
            this.demoCondition = demo;
        }

        @Override
        public void run() {
            while (true) {
                demoCondition.a();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static class B implements Runnable {
        private ObjectLockDemo demoCondition;

        public B(ObjectLockDemo demo) {
            this.demoCondition = demo;
        }

        @Override
        public void run() {
            while (true) {
                demoCondition.b();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static class C implements Runnable {
        private ObjectLockDemo demoCondition;

        public C(ObjectLockDemo demo) {
            this.demoCondition = demo;
        }

        @Override
        public void run() {
            while (true) {
                demoCondition.c();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            }
        }
    }
}

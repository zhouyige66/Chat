import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/8/8 8:30 下午
 * @Version: v1.0
 */
public class ConditionDemo {
    private int signal;
    private Lock lock = new ReentrantLock(); // 可重入锁
    private Condition a = lock.newCondition();
    private Condition b = lock.newCondition();
    private Condition c = lock.newCondition();

    public void a() {
        lock.lock();
        System.out.println(Thread.currentThread()+"a获取锁");
        while (signal != 0) {
            try {
                a.await();// 当前线程进入等待，同时释放锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread()+"a" + signal);
        signal++;
        b.signal();// 唤醒一个等待的线程
        lock.unlock();
    }

    public void b() {
        lock.lock();
        System.out.println(Thread.currentThread()+"b"+"获取锁");
        while (signal != 1) {
            try {
                b.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread()+"b" + signal);
        signal++;
        c.signal();
        lock.unlock();
    }

    public void c() {
        lock.lock();
        System.out.println(Thread.currentThread()+"c"+"获取锁");
        while (signal != 2) {
            try {
                c.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread()+"c" + signal);
        signal = 0;
        a.signal();
        lock.unlock();
    }

    public static void main(String[] args) {
        ConditionDemo condition = new ConditionDemo();
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

        private ConditionDemo condition;

        public A(ConditionDemo condition) {
            this.condition = condition;
        }

        @Override
        public void run() {
            condition.a();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class B implements Runnable {

        private ConditionDemo condition;

        public B(ConditionDemo condition) {
            this.condition = condition;
        }

        @Override
        public void run() {
            condition.b();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class C implements Runnable {

        private ConditionDemo condition;

        public C(ConditionDemo condition) {
            this.condition = condition;
        }

        @Override
        public void run() {
            condition.c();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

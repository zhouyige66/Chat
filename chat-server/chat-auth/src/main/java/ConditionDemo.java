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
        System.out.println("a获取锁");
        while (signal != 0) {
            try {
                a.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("a" + signal);
        signal++;
        b.signal();
        lock.unlock();
    }

    public void b() {
        lock.lock();
        System.out.println("b获取锁");
        while (signal != 1) {
            try {
                b.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("b" + signal);
        signal++;
        c.signal();
        lock.unlock();
    }

    public void c() {
        lock.lock();
        System.out.println("c获取锁");
        while (signal != 2) {
            try {
                c.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        System.out.println("c" + signal);
        signal = 0;
        a.signal();
        lock.unlock();
    }

    public static void main(String[] args) {
        ConditionDemo condition = new ConditionDemo();
        ACondition a = new ACondition(condition);
        BCondition b = new BCondition(condition);
        CCondition c = new CCondition(condition);
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

    public static class ACondition implements Runnable {

        private ConditionDemo condition;

        public ACondition(ConditionDemo condition) {
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

    public static class BCondition implements Runnable {

        private ConditionDemo condition;

        public BCondition(ConditionDemo condition) {
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

    public static class CCondition implements Runnable {

        private ConditionDemo condition;

        public CCondition(ConditionDemo condition) {
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description:
 * @Author: Roy
 * @Date: 2020/8/9 4:43 下午
 * @Version: v1.0
 */
public class ExecutorsDemo {

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new A());
        executorService.execute(new B());
        executorService.execute(new C());
    }

    public static class A implements Runnable {
        @Override
        public void run() {
            System.out.println("A执行了");
        }
    }

    public static class B implements Runnable {
        @Override
        public void run() {
            System.out.println("B执行了");
        }
    }

    public static class C implements Runnable {
        @Override
        public void run() {
            System.out.println("C执行了");
        }
    }
}

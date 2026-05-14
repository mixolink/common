package test;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPoolDemo {

    public static void main(String[] args) throws InterruptedException {
        // 1. 创建线程池：2个核心线程，有界队列容量10
        // 使用 CallerRunsPolicy 拒绝策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                2, 2, 
                0L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(10), 
                new ThreadPoolExecutor.CallerRunsPolicy() 
        );

        System.out.println("--- 任务提交开始 ---");
        
        

        // 模拟提交 50 个任务
        for (int i = 1; i <= 50; i++) {
            final int taskId = i;
            
            System.out.println(Thread.currentThread().getName() + " 准备添加第 " + taskId + " 个任务");
            
            executor.execute(() -> {
                try {
                    // 模拟耗时操作，比如上传 FTP
                    Thread.sleep(500); 
                    System.out.println(Thread.currentThread().getName() + " [OK] 完成处理任务: " + taskId);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
            
            // 这里的打印可以让你看到主线程什么时候回到了循环
            if (taskId == 50) {
                System.out.println("--- 所有任务已进入提交序列 ---");
            }
        }

        // 优雅关闭
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("--- 所有任务处理完毕 ---");
    }
}
package service.threadPoolSubsystem;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {

    private final int number_of_threads = 10;
    Thread[] threadPool = new Thread[number_of_threads];

    // CONSTRUCTOR
    ThreadPool() throws ExecutionException, InterruptedException {
        // инициализация тредов
        for (Thread thread : threadPool)
            thread = new Thread();

        ExecutorService executor = Executors.newFixedThreadPool(number_of_threads);

        LinkedList<Future<String>> futures = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            Future<String> future = executor.submit(new MyCallable(String.valueOf(i)));
            futures.add(future);
        }

        while (futures.size() > 0) {
            Iterator iterator = futures.iterator();
            while (iterator.hasNext()) {
                Object o = iterator.next();
                Future future = null;
                if (o instanceof Future) {
                    future = (Future) o;
                }
                assert future != null;
                if (future.isDone()) {
                    System.out.println(future.get());
                    iterator.remove();
                }
            }
        }
        executor.shutdown();
    }

    // если есть свободный тред - возвращает его
    // иначе возвращает null
    Thread getFreeThread() {
        for (Thread thread : threadPool)
            if (thread.isAlive()) return thread;
        return null;
    }

    public static void main(String[] args) throws Exception {
        ThreadPool threadPool = new ThreadPool();

    }
}

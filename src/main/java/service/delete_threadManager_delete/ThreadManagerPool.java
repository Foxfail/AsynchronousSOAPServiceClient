package service.delete_threadManager_delete;

import java.util.*;
import java.util.concurrent.*;

public class ThreadManagerPool {
    private static final int NUMBER_OF_THREADS = 10;
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // получает несколько потоков
        args = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
//        ThreadManager delete_threadManager_delete = new ThreadManager();
        ExecutorService executorService = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
        Set<Future<String>> set = new HashSet<>();
        for (String word: args) {
            //noinspection unchecked
            Callable<String> callable = new WordLengthCallable(word);
            Future<String> future = executorService.submit(callable);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            // добавляет в цикле все потоки которые получил на вход
            set.add(future);
        }
        StringBuilder sum = new StringBuilder();
        for (Future<String> future : set) {
//            System.out.println(future.get());
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            sum.append(future.get());
        }
        System.out.println(sum);
    }
}

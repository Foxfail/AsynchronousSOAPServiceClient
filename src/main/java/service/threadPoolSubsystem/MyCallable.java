package service.threadPoolSubsystem;

import java.util.Random;
import java.util.concurrent.Callable;


// Класс который позволяет передавать в поток переменную
public class MyCallable implements Callable<String> {
    private String arg0;

    MyCallable(String arg0) {
        this.arg0 = arg0;
    }

    @Override
    public String call() throws Exception {

        // симуляция долго выполняющегося запроса
        Random r = new Random();
        int mills = r.nextInt(5000);
        Thread.sleep(100);

        // настоящая работа
        System.out.println(Thread.currentThread().getName() + "\t arg0: " + arg0);
        switch (arg0) {
            case ("INN"):
                return "770088994455";

            case ("RND"):
                return String.valueOf(r.nextInt(10000));
        }

        return null;
    }
}

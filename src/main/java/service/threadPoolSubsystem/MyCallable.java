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
        Thread.sleep(mills);

        // настоящая работа
        System.out.println(Thread.currentThread().getName() + "\t arg0: " + arg0);
        switch (arg0) {
            case ("INN"):
                return "770088994455";

            case ("RND"):
                return "rnd0" + (r.nextInt(99999999-10000000) + 10000000);
        }

        return null;
    }
}

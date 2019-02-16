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
        Random r = new Random();
        int mills = r.nextInt(5000);
        Thread.sleep(mills);
        return Thread.currentThread().getName() + " \t value: " + arg0 + "\t mills: " + mills;

    }
}

package service.threadManager;

import java.util.Random;
import java.util.concurrent.Callable;

public class WordLengthCallable implements Callable {
    private String word;

    public WordLengthCallable(String word) {
        this.word = word;
    }

    public String call() {
        System.out.println(word + "\tcall start");
        //тут обрабатываем
        Random rnd = new Random();
        long rndMills = rnd.nextInt(15000);

        System.out.println(word + "\tcall started. " + rndMills);
        try {
            Thread.sleep(rndMills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(word + "\tcall end. " + rndMills);
        return word;
    }
}
package service.threadPoolSubsystem;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {


    @SuppressWarnings("FieldCanBeLocal")
    private final int number_of_threads = 10;
    private ExecutorService threadPool; // здесь экзекутор хранит потоки и управлет ими, отправля фьючерсы на исполнение свободным потокам
    private ArrayList<ThreadPoolListener> threadPoolListeners;
    private HashMap<Integer, Future<String>> futures; // ключ - номер входящего запроса, значение future которое ему соответсвует

    // CONSTRUCTOR
    ThreadPool() {
        // создаем пул потоков
        threadPool = Executors.newFixedThreadPool(number_of_threads);
        // инициализируем фьючерсы
        futures = new HashMap<>();
        // инициализируем слушателей
        threadPoolListeners = new ArrayList<>();


    }


    // добавляет в фьючерсы по айди и строке запроса от клиента
    void addFutureCallable(Integer InID, String request) {
        Future<String> future = threadPool.submit(new MyCallable(request));
        futures.put(InID, future);


        // проверяю в новом треде (каждые 10 мсек) готов ли какой нибудь запрос (готовых может быть несколько)
        // вызываю у слушателей метод onThreadPoolResultsReady в который передаю массив с результатами
        new Thread(() -> {
            while (futures.size() > 0) {
                try {
                    Thread.sleep(1000);
                    HashMap<Integer, String> results = getFuturesResults();
                    if (results.size() > 0) {
                        for (ThreadPoolListener listener : threadPoolListeners) {
                            listener.onThreadPoolResultsReady(results);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    // возврящает массив результатов, которые были подготовлены в этом проходе по фьючерсам
    // результирующий массив может быть возвращен пустым
    private HashMap<Integer, String> getFuturesResults() {
        System.out.println("getFuturesResults()");
        HashMap<Integer, String> resultIntegerStringHashMap = new HashMap<>();


        Iterator<Map.Entry<Integer, Future<String>>> iterator = futures.entrySet().iterator();
        HashSet<Map.Entry> entriesToDelete = new HashSet<>();
        entriesToDelete.clear();
        Map.Entry<Integer, Future<String>> entry = null;
        try {
            while (iterator.hasNext()) {
                entry = iterator.next();
                Future<String> future = entry.getValue();
                if (future.isDone()) {
                    resultIntegerStringHashMap.put(entry.getKey(), future.get());
                    entriesToDelete.add(entry);
                }
            }
        } catch (Exception e) {
            if (entry != null) {
                System.out.println("!!!!!!!! Exception in " + entry.getKey().toString());
                e.printStackTrace();
            }
        } finally {
            if (entriesToDelete.size() > 0) {
                for (Map.Entry entryToDelete : entriesToDelete) {
                    futures.remove(entryToDelete.getKey());
                }
            }
        }
        return resultIntegerStringHashMap;
    }


    void addThreadPoolListener(ThreadPoolListener listener) {
        //
        threadPoolListeners.add(listener);
    }


    // завершает экзекутор с пулом тредов
    public void shutdown() {
        threadPool.shutdown();
    }

}

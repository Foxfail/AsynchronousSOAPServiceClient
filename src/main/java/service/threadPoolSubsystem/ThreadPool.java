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

        // проверяю в новом треде (каждые 10 мсек) готов ли какой нибудь запрос (готовых может быть несколько)
        // вызываю у слушателей метод onThreadPoolResultsReady в который передаю массив с результатами
        new Thread(() -> {
            while (futures.size() > 0) {
                try {
                    Thread.sleep(10);
                    HashMap<Integer, String> results = getFuturesResults();
                    if (results.size() > 0) {
                        for (ThreadPoolListener listener : threadPoolListeners) {
                            listener.onThreadPoolResultsReady(results);
                        }
                    }
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    // добавляет в фьючерсы по айди и строке запроса от клиента
    public void addFutureCallable(Integer InID, String request) {
        Future<String> future = threadPool.submit(new MyCallable(request));
        futures.put(InID, future);
    }


    // возврящает массив результатов, которые были подготовлены в этом проходе по фьючерсам
    // результирующий массив может быть возвращен пустым
    public HashMap<Integer, String> getFuturesResults() throws ExecutionException, InterruptedException {
        HashMap<Integer, String> resultIntegerStringHashMap = new HashMap<>();
        Iterator iterator = futures.entrySet().iterator();
        while (iterator.hasNext()) {
            //noinspection unchecked
            Map.Entry<Integer, Future<String>> entry = (Map.Entry<Integer, Future<String>>) iterator.next();
            Future<String> future = entry.getValue();
            if (future.isDone()) {
                resultIntegerStringHashMap.put(entry.getKey(), future.get());
                iterator.remove();
            }
        }
        return resultIntegerStringHashMap;
    }


    public void addThreadPoolListener(ThreadPoolListener listener) {
        //
        threadPoolListeners.add(listener);
    }


    // завершает экзекутор с пулом тредов
    public void shutdown() {
        threadPool.shutdown();
    }

}

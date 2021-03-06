package service.threadPoolSubsystem;

import service.queueSubsystem.MyQueue;
import service.queueSubsystem.QueueListener;

import java.util.HashMap;
import java.util.Map;

public class ThreadPoolManager implements QueueListener, ThreadPoolListener {

    private ThreadPool threadPool;

    public ThreadPoolManager() {
        threadPool = new ThreadPool();
        threadPool.addThreadPoolListener(this);
    }

    @Override
    public void onInboundQueueMessageAdded() {
            Map.Entry<Integer, String> entry = MyQueue.getInbound();
            threadPool.addFutureCallable(entry.getKey(), entry.getValue());
    }

    @Override
    public void onThreadPoolResultsReady(HashMap<Integer, String> results) {
        // добавляем всё в исходящие

        for(Map.Entry<Integer, String> entry : results.entrySet()) {
            System.out.println("ResultReady! id " + entry.getKey());
            MyQueue.addOutbound(entry.getKey(), entry.getValue());
        }
    }
}

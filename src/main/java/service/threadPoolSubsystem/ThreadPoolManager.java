package service.threadPoolSubsystem;

import service.threadPoolSubsystem.QueueSubsystem.QueueListener;

import java.util.concurrent.ExecutionException;

public class ThreadPoolManager implements QueueListener {
    // FIELDS
    boolean isSomethingInbound = false;
    ThreadPool threadPool;

    {
        try {
            threadPool = new ThreadPool();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onInboundQueueMessageAdded() {
        Thread freeThread = threadPool.getFreeThread();
        if (freeThread != null){
            freeThread.start();
            freeThread.run();
        }
    }

}

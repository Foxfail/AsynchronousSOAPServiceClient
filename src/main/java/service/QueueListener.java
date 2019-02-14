package service;

public interface QueueListener {
    void onInboundQueueMessageAdded();
    void onOutboundQueueMessageAdded();
}

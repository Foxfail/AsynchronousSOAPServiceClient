package service;

public interface QueueListener {
    void onInboundQueueChanged();
    void onOutboundQueueChanged();
}

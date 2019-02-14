package service;

public interface QueueListener {
    public void onInboundQueueChanged();
    public void onOutboundQueueChanged();
}

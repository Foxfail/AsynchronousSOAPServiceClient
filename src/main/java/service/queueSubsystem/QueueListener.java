package service.queueSubsystem;

public interface QueueListener {
    default void onInboundQueueMessageAdded() {}
    default void onOutboundQueueMessageAdded(){}
}

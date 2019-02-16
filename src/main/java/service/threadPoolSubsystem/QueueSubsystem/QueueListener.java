package service.threadPoolSubsystem.QueueSubsystem;

public interface QueueListener {
    default void onInboundQueueMessageAdded() {}
    default void onOutboundQueueMessageAdded(){}
}

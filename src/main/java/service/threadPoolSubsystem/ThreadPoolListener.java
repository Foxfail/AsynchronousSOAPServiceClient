package service.threadPoolSubsystem;

import java.util.HashMap;

public interface ThreadPoolListener {
    default void onThreadPoolResultsReady(HashMap<Integer, String> results){};
}

package service.queueSubsystem;

import javax.validation.constraints.NotNull;
import java.util.*;

public class MyQueue {
    private static ArrayList<QueueListener> listeners = new ArrayList<>();
    private static int inID = -1;
    private static LinkedHashMap<Integer, String> inboundQueue = new LinkedHashMap<>();
    private static LinkedHashMap<Integer, String> outboundQueue = new LinkedHashMap<>();


    public static Integer addInbound(String message) {
        inID++;
        System.out.println("addInbound() id = " + inID);
        inboundQueue.put(inID, message);
        notifyInboundQueueChanged();
        return inID;
    }
    public static Integer addOutbound(Integer inID, String message) {
        outboundQueue.put(inID, message);
        System.out.println("addOutbound() id = " + inID);
        notifyOutboundQueueChanged();
        return inID;
    }


    public static Map.Entry<Integer, String> getInbound() {
        Map.Entry<Integer, String> entry = inboundQueue.entrySet().iterator().next();
        System.out.println("getInbound() id = " + entry.getKey());
        inboundQueue.remove(entry.getKey());
        return entry;
    }
    public static String getOutbound(Integer inID) {
        // возвращает null если нет элемента с таким ключем
        return outboundQueue.remove(inID);
    }

    public static void addListener(QueueListener listener){
        listeners.add(listener);
        listeners.trimToSize();
    }
    private static void notifyInboundQueueChanged(){
        for (QueueListener listener : listeners){
            listener.onInboundQueueMessageAdded();
        }
    }
    private static void notifyOutboundQueueChanged(){
        for (QueueListener listener : listeners){
            listener.onOutboundQueueMessageAdded();
        }
    }
}

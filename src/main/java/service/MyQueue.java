package service;

import client.Client;

import javax.xml.soap.SOAPMessage;
import java.util.*;

class MyQueue {
    private static ArrayList<QueueListener> listeners = new ArrayList<>();
    static int inID = -1;
    private static LinkedHashMap<Integer, String> inboundQueue = new LinkedHashMap<>();
    private static LinkedHashMap<Integer, String> outboundQueue = new LinkedHashMap<>();


    static Integer addInbound(String message) {
        inID++;
        inboundQueue.put(inID, message);
        notifyInboundQueueChanged();
        return inID;
    }
    static Integer addOutbound(Integer inID, String message) {
        outboundQueue.put(inID, message);
        System.out.println("adding outbound id = " + inID);
        notifyOutboundQueueChanged();
        return inID;
    }


    static Map.Entry<Integer, String> getInbound() {
        Map.Entry<Integer, String> entry = inboundQueue.entrySet().iterator().next();
        System.out.println("getting and removing inbound. id = " + entry.getKey());
        inboundQueue.remove(entry.getKey());
        return entry;
    }
    static String getOutbound() {
        return outboundQueue.remove(0);
    }

            static void addListener(QueueListener listener){
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

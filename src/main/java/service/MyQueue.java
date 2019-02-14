package service;

import javax.xml.soap.SOAPMessage;
import java.util.ArrayList;

 class MyQueue {
    private static ArrayList<QueueListener> listeners = new ArrayList<>();
    private static ArrayList<SOAPMessage> inboundQueue = new ArrayList<>();
    private static ArrayList<SOAPMessage> outboundQueue = new ArrayList<>();


    static void addInbound(SOAPMessage message) {
        inboundQueue.add(message);
        notifyInboundQueueChanged();
    }
    static void addOutbound(SOAPMessage message) {
        outboundQueue.add(message);
        notifyOutboundQueueChanged();
    }

    static SOAPMessage getInbound() {
        return inboundQueue.remove(0);
    }
    static SOAPMessage getOutbound() {
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

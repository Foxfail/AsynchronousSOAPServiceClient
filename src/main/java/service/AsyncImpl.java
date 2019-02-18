package service;

import contract.AsyncInterface;
import service.queueSubsystem.MyQueue;
import service.queueSubsystem.QueueProcessor;
import service.threadPoolSubsystem.ThreadPoolManager;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.Endpoint;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("DefaultAnnotationParam")
@XmlAccessorType(XmlAccessType.FIELD)
@WebService(targetNamespace = "http://localhost:8888/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class AsyncImpl implements AsyncInterface {


    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/", new AsyncImpl());
        System.out.println("service started!");
        // обработчик очереди больше не нужен т.к. обрабатывается всё в MyCallable
//        MyQueue.addListener(new QueueProcessor());
//        System.out.println("Queue Processor subsystem added to queue listeners");
        MyQueue.addListener(new ThreadPoolManager());
        System.out.println("Thread Pool subsystem added to queue listeners");
    }

    // Тут обрабатываю запросы от клиентов
    @WebMethod
    public Integer addDataRequest(String message) {
        // сообщение отправляется в очередь входящих
        // где вызывает событие "очередь входящих изменена"
        // это событие запускает обработчик очереди и далее обрабатывается
        System.out.println("addDataRequest inbound");

        Integer inID = MyQueue.addInbound(message);
        System.out.println("addDataRequest added id = " + inID);
        return inID;
    }

    @Override
    public HashMap<Integer, String> pollForResult(List<Integer> idsList) {
        return null;
    }

    @WebMethod
    public String pollForResult(Integer inID) {
        // тут обрабатывается опрос от клиента
        System.out.println("pollForResult(" + inID + ")");
        return MyQueue.getOutbound(inID);
    }
//    static void messageProcessed(String callback) {
//        SOAPMessage message = MyQueue.getOutbound();
//        callback.pollForResult(message);
//    }

}

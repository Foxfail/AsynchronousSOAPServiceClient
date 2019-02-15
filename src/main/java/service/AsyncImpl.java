package service;

import contract.AsyncInterface;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.ws.Endpoint;

@SuppressWarnings("DefaultAnnotationParam")
@XmlAccessorType(XmlAccessType.FIELD)
@WebService(targetNamespace = "http://localhost:8888/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class AsyncImpl implements AsyncInterface {


    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/", new AsyncImpl());
        System.out.println("service started!");
        MyQueue.addListener(new QueueProcessor());
        System.out.println("queue processor added to queue listeners");
    }

    // Тут обрабатываю запросы от клиентов
    @WebMethod
    public Integer addDataRequest(String message) {
        // сообщение отправляется в очередь входящих
        // где вызывает событие "очередь входящих изменена"
        // это событие запускает обработчик очереди и далее обрабатывается
        System.out.println("addDataRequest inbound");

        Integer inID = MyQueue.addInbound(message);
        System.out.println("sending inbound id = " + inID);
        return inID;
    }

    @WebMethod
    public String pollForResult(Integer inID) {
        // тут обрабатываются ответы на запросы.
        // поскольку сервер не делает запросов, то и ответов никаких от клиента не надо обрабатывать
        return null;
    }
//    static void messageProcessed(String callback) {
//        SOAPMessage message = MyQueue.getOutbound();
//        callback.pollForResult(message);
//    }

}

package service;

import contract.AsyncInterface;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Endpoint;

@SuppressWarnings("DefaultAnnotationParam")
@XmlAccessorType(XmlAccessType.FIELD)
@WebService(targetNamespace = "http://localhost:8888/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public class AsyncImpl implements AsyncInterface {


    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8888/", new AsyncImpl());
        System.out.println("service started!");
    }

    // Тут обрабатываю запросы от клиентов
    @WebMethod
    public void SOAPRequest(SOAPMessage message) throws SOAPException {
        // по идее тут надо передать данные в обработчик
        // а callback передать в систему callback'ов
        // но всё это отправляется в очередь и будет парсится отдельно
        MyQueue.addInbound(message);
    }

    @WebMethod
    public void SOAPResponse(SOAPMessage message) {

    }

    static void messageProcessed(AsyncInterface callback) throws SOAPException {
        SOAPMessage message = MyQueue.getOutbound();
        callback.SOAPResponse(message);
    }

}

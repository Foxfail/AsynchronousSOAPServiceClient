package service.queueSubsystem;

import contract.AsyncInterface;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.*;
import java.util.Base64;
import java.util.Map;

public class QueueProcessor implements QueueListener {


    // ОБРАБОТКА ОЧЕРЕДИ
    private static void processQueue() {
        System.out.println("processQueue");
        // message in
        Map.Entry<Integer, String> messageIn = MyQueue.getInbound();
        String dataOut;

        // process message
//        String dataIn = getDataFromRequest(messageIn);
        if (messageIn.getValue().equals("INN")) {
            dataOut = "7731000101923";
        } else {
            dataOut = "error";
        }

        // message out
//        SOAPMessage messageOut = makeSoapMessage(dataOut);
        MyQueue.addOutbound(messageIn.getKey(), dataOut);

//        AsyncImpl.messageProcessed(getCallBackFromRequest(messageIn));
    }


    // СОЗДАНИЕ SOAPMESSAGE
    private static SOAPMessage makeSoapMessage(String data) throws SOAPException {
        // если не заработает то можно поменять протокол на 1.2
        SOAPMessage soapMessage = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();

        SOAPBody body = soapMessage.getSOAPBody();

        QName bodyName = new QName("http://localhost:8888/", "Info");
        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);

        QName name = new QName("dataRequest");

        SOAPElement symbol = bodyElement.addChildElement(name);
        symbol.addTextNode(data);

        return soapMessage;
    }


    // ПОЛУЧЕНИЕ ДАННЫХ ИЗ SOAPMESSAGE
    private static String getDataFromRequest(SOAPMessage message) throws SOAPException {
        System.out.print("getDataFromRequest...");
        SOAPBody soapBody = message.getSOAPBody();
        java.util.Iterator iterator = soapBody.getChildElements(new QName("GetInfo"));
        SOAPBodyElement bodyElement = (SOAPBodyElement) iterator.next();
        String value = bodyElement.getValue();
        System.out.println("success");
        return value;
    }

    private static AsyncInterface getCallBackFromRequest(SOAPMessage message) throws SOAPException, IOException, ClassNotFoundException {
        System.out.print("getCallBackFromRequest...");
        SOAPBody soapBody = message.getSOAPBody();
        java.util.Iterator iterator = soapBody.getChildElements(new QName("http://localhost:8888/", "Callback"));
        SOAPBodyElement bodyElement = (SOAPBodyElement) iterator.next();
        String data = bodyElement.getValue();

        Object o = fromString(data);
        AsyncInterface asyncInterface = null;
        if (o instanceof AsyncInterface) {
            asyncInterface = (AsyncInterface) o;
        }
        System.out.println("success");
        return asyncInterface;
    }


    // СЕРИАЛИЗАЦИЯ \ ДЕСЕРИАЛИЗАЦИЯ
    private static Object fromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    @SuppressWarnings("unused")
    private static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }


    @Override
    public void onInboundQueueMessageAdded() {
            processQueue();
    }

    @Override
    public void onOutboundQueueMessageAdded() {

    }
}

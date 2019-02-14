package service;

import contract.AsyncInterface;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.io.*;
import java.util.Base64;

class QueueProcessor {


    // ОБРАБОТКА ОЧЕРЕДИ
    static void processQueue() throws SOAPException, IOException, ClassNotFoundException {
        // message in
        SOAPMessage messageIn = MyQueue.getInbound();
        String dataOut = "";

        // process message
        String dataIn = getDataFromRequest(messageIn);
        if (dataIn.equals("INN")){
            dataOut = "7731000101923";
        } else {
            dataOut = "error";
        }

        // message out
        SOAPMessage messageOut = makeSoapMessage(dataOut);
        MyQueue.addOutbound(messageOut);

        AsyncImpl.messageProcessed(getCallBackFromRequest(messageIn));
    }





    // СОЗДАНИЕ SOAPMESSAGE
    static SOAPMessage makeSoapMessage(String data) throws SOAPException {
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
    static String getDataFromRequest(SOAPMessage message) throws SOAPException {
        SOAPBody soapBody = message.getSOAPBody();
        java.util.Iterator iterator = soapBody.getChildElements(new QName("http://localhost:8888/", "GetInfo"));
        SOAPBodyElement bodyElement = (SOAPBodyElement)iterator.next();
        String data = bodyElement.getValue();
        return data;
    }
    static AsyncInterface getCallBackFromRequest(SOAPMessage message) throws SOAPException, IOException, ClassNotFoundException {
        SOAPBody soapBody = message.getSOAPBody();
        java.util.Iterator iterator = soapBody.getChildElements(new QName("http://localhost:8888/", "Callback"));
        SOAPBodyElement bodyElement = (SOAPBodyElement)iterator.next();
        String data = bodyElement.getValue();

        Object o = fromString(data);
        AsyncInterface asyncInterface = null;
        if (o instanceof AsyncInterface){
            asyncInterface = (AsyncInterface) o;
        }
        return asyncInterface;
    }





    // СЕРИАЛИЗАЦИЯ \ ДЕСЕАРИЛИЗАЦИЯ
    private static Object fromString( String s ) throws IOException,
            ClassNotFoundException {
        byte [] data = Base64.getDecoder().decode( s );
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }
    private static String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        oos.writeObject( o );
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}

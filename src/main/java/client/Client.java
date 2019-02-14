package client;


import com.sun.xml.internal.ws.fault.ServerSOAPFaultException;
import contract.AsyncInterface;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Service;
import java.io.*;
import java.net.URL;
import java.util.Base64;


public class Client implements AsyncInterface, Serializable {
    public static void main(String[] args) throws IOException, SOAPException {
        URL wsdlURL = new URL("http://localhost:8888/?wsdl");
        QName qNameService = new QName("http://localhost:8888/", "AsyncImplService");
        QName qNamePort = new QName("http://localhost:8888/", "AsyncImplPort");
        Service service = Service.create(wsdlURL, qNameService);

        System.out.println("trying to connect");
        AsyncInterface ps = service.getPort(qNamePort, AsyncInterface.class);
        System.out.println("connected");

        // REQUEST ORIGINATOR
        Client callback = new Client();
        String data = "INN";

        //тут формируется сообщение
        System.out.println("forming message");
        SOAPMessage soapMessage = makeSOAPMessage(data, callback);

//        MyMessage message = new MyMessage(data, callback);
        System.out.println("sending message");
        try {
            ps.SOAPRequest(soapMessage);
        } catch (ServerSOAPFaultException e) {
            e.printStackTrace();
        }

        System.out.println("sent!");
    }

    public void SOAPRequest(SOAPMessage message) {

    }

    // тут обрабатываю ответы от сервиса

    public void SOAPResponse(SOAPMessage message) {
        SOAPBody soapBody = null;
        try {
            soapBody = message.getSOAPBody();
        } catch (SOAPException e) {
            e.printStackTrace();
        }
        java.util.Iterator iterator;
        String inn = "null";
        if (soapBody != null) {
            iterator = soapBody.getChildElements(new QName("http://localhost:8888/", "Info"));

            SOAPBodyElement bodyElement = (SOAPBodyElement) iterator.next();
            inn = bodyElement.getValue();
        }
        System.out.println("received INN = " + inn);
    }


    private static SOAPMessage makeSOAPMessage(String data, Client clientCallback) throws SOAPException, IOException {
        // если не заработает то можно поменять протокол на 1.2
        SOAPMessage soapMessage = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();

        // можно так
//        SOAPPart soapPart = soapMessage.getSOAPPart();
//        SOAPEnvelope envelope = soapPart.getEnvelope();
//        SOAPHeader header = envelope.getHeader();
//        SOAPBody body = envelope.getBody();
//        SOAPHeader header = soapMessage.getSOAPHeader(); // пустой поэтому не нужен
        // но удобнее так
        SOAPBody body = soapMessage.getSOAPBody();

        QName bodyName = new QName("http://localhost:8888/", "GetInfo");
        SOAPBodyElement bodyElement = body.addBodyElement(bodyName);
        QName name = new QName("dataRequest");
        SOAPElement symbol = bodyElement.addChildElement(name);
        symbol.addTextNode(data);

        QName nameCallback = new QName("dataCallback");
        SOAPElement symbolCallback = bodyElement.addChildElement(nameCallback);
        symbolCallback.addTextNode(toString(clientCallback));
        System.out.println(soapToString(soapMessage));
        return soapMessage;
    }

    private static String soapToString(SOAPMessage msg) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            msg.writeTo(out);
        } catch (SOAPException | IOException e) {
            e.printStackTrace();
        }
        return new String(out.toByteArray());
    }

    @SuppressWarnings("unused")
    private static Object fromString(String s) throws IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }
    private static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }
}

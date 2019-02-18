
package delete_client_delete;


import com.sun.xml.internal.ws.fault.ServerSOAPFaultException;
import contract.AsyncInterface;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Service;
import java.io.*;
import java.net.URL;
import java.util.Base64;


public class Client {
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
//        try {
//            SOAPMessage soapMessage = createSOAPRequestTest();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        System.out.println();
//        MyMessage message = new MyMessage(data, callback);
        System.out.println("sending message");
        try {
//            ps.addDataRequest(soapMessage);
        } catch (ServerSOAPFaultException e) {
            e.printStackTrace();
        }

        System.out.println("sent!");
    }

    // тут обрабатывались бы запросы от сервиса, если бы он их слал
    public Integer addDataRequest(String message) {
        return 0;
    }
    // тут обрабатываю ответы от сервиса
    public String pollForResult(Integer inID) {
//        SOAPBody soapBody = null;
//        try {
//            soapBody = message.getSOAPBody();
//        } catch (SOAPException e) {
//            e.printStackTrace();
//        }
        java.util.Iterator iterator;
        String inn = "null";
//        if (soapBody != null) {
//            iterator = soapBody.getChildElements(new QName("http://localhost:8888/", "Info"));
//
//            SOAPBodyElement bodyElement = (SOAPBodyElement) iterator.next();
//            inn = bodyElement.getValue();
//        }
        System.out.println("received INN = " + inn);
        return null;
    }


    private static SOAPMessage makeSOAPMessage(String data, Client clientCallback) throws SOAPException, IOException {
        // если не заработает то можно поменять протокол на 1.2
        SOAPMessage soapMessage = MessageFactory.newInstance(SOAPConstants.SOAP_1_1_PROTOCOL).createMessage();

        // можно так
        SOAPPart soapPart = soapMessage.getSOAPPart();
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
        SOAPHeader soapHeader = soapEnvelope.getHeader();
        SOAPBody soapBody = soapEnvelope.getBody();
//        SOAPHeader header = soapMessage.getSOAPHeader(); // пустой поэтому не нужен
        soapEnvelope.removeNamespaceDeclaration("SOAP-ENV");
        soapEnvelope.removeNamespaceDeclaration("env");

        soapEnvelope.setPrefix("soapenv");
        soapHeader.setPrefix("soapenv");
        soapBody.setPrefix("soapenv");
//        soapEnvelope.addNamespaceDeclaration("S1", "http://schemas.xmlsoap.org/soap/envelope/");

        soapEnvelope.addNamespaceDeclaration("loc", "http://localhost:8888/");


        QName bodyName = new QName("loc:addDataRequest");
        SOAPBodyElement bodyElement = soapBody.addBodyElement(bodyName);
        QName name = new QName("dataRequest");
        SOAPElement symbol = bodyElement.addChildElement(name);
        symbol.addTextNode(data);


        QName nameCallback = new QName("dataCallback");
        SOAPElement symbolCallback = bodyElement.addChildElement(nameCallback);
//        symbolCallback.addTextNode(toString(clientCallback));

        soapMessage.saveChanges(); // сохраняет изменения ???
        System.out.println(soapToString(soapMessage)); // выводит xml в консоль

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

package contract;

import service.MyMessage;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

@SuppressWarnings("DefaultAnnotationParam")
@XmlAccessorType(XmlAccessType.FIELD)
@WebService(targetNamespace = "http://localhost:8888/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface AsyncInterface {
    public void SOAPResponse(SOAPMessage message) throws SOAPException;
    public void SOAPRequest(SOAPMessage message) throws SOAPException;
}

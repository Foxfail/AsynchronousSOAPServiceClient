package contract;

import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@SuppressWarnings("DefaultAnnotationParam")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PolicyException_type")
@WebService(targetNamespace = "http://localhost:8888/")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT)
public interface AsyncInterface {

    String pollForResult(Integer inID);
    Integer addDataRequest(String message);
    String pollForResults(List<Integer> idsList);

}
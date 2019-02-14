package service;

import contract.AsyncInterface;

import javax.ws.rs.DELETE;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@Deprecated
@XmlAccessorType(XmlAccessType.PROPERTY)
public class MyMessage {

    public String data;
    public AsyncInterface callbackInterface;


    public MyMessage(String data, AsyncInterface callbackInteface){
        this.data = data;
        this.callbackInterface = callbackInteface;
    }

}

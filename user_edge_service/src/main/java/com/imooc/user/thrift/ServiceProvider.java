package com.imooc.user.thrift;

import com.imooc.thrift.message.MessageService;
import com.imooc.thrift.user.UserService;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServiceProvider {

    @Value("${thrift.user.ip}")
    private String serverIp;
    @Value("${thrift.user.port}")
    private  int serverPort;

    @Value("${thrift.message.ip}")
    private String messageserverIp;
    @Value("${thrift.message.port}")
    private  int messageserverPort;

    public UserService.Client getUserService() {

        TSocket socket = new TSocket(serverIp,serverPort,30000);
        TTransport transport = new TFramedTransport(socket);
        try {
            transport.open();
        }catch (TTransportException ex){
            return null;
        }
        TProtocol protocol = new TBinaryProtocol(transport);

        UserService.Client client = new UserService.Client(protocol);
        return client;
    }

    public MessageService.Client getMessageService() {

        TSocket socket = new TSocket(messageserverIp,messageserverPort,30000);
        TTransport transport = new TFramedTransport(socket);
        try {
            transport.open();
        }catch (TTransportException ex){
            return null;
        }
        TProtocol protocol = new TBinaryProtocol(transport);

        MessageService.Client client = new MessageService.Client(protocol);
        return client;
    }


}

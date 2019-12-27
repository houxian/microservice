package com.imooc.user.thrift;

import com.imooc.thrift.user.UserService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TNonblockingServer;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.print.attribute.standard.PrinterInfo;

@Configuration
public class ThriftServer {

    @Autowired
    private UserService.Iface userService;

    @Value("${server.port}")
    private int serverport;

    @PostConstruct
    public  void startThriftServer() {

        TProcessor processor = new UserService.Processor<>(userService);
        TNonblockingServerSocket socket = null;
        try {
             socket = new TNonblockingServerSocket(serverport);
        }catch (TTransportException ex) {
            ex.printStackTrace();
        }

        TNonblockingServer.Args agrs = new TNonblockingServer.Args(socket);
        agrs.processor(processor);
        agrs.transportFactory(new TFramedTransport.Factory());
        agrs.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new TNonblockingServer(agrs);
        server.serve();

    }



}

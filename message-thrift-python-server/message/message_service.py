#coding utf-8

from message.api import MessageService
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import smtplib
from email.mime.text import MIMEText
from  email.header import Header

send ='imooc@163.com'
authCode ='aA1111111'

class MessageServiceHandler:
    def sendMobileMessage(self, mobile, message):
        print ("sendMobileMessage")
        return True


    def sendEmailMessage(self, email, message):
     print ("sendEmailMessage ,email:"+email+", message:"+message)
     messageObj = MIMEText(message,"plain","utf-8")
     messageObj['from'] = send
     messageObj['To'] = email
     messageObj['Subject'] = Header('aaa','utf-8')

     smtpObj = smtplib.SMTP('smtp.163.com')
     smtpObj.login(send,authCode)
     smtpObj.sendmail(send,[email],messageObj.as_string());
     print ("send mail success")
     return True


if __name__ == '__main__':
    handler = MessageServiceHandler()
    processor = MessageService.Processor(handler)
    transport = TSocket.TServerSocket("127.0.0.1","9090")
    tfactory = TTransport.TFramedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor,transport,tfactory,pfactory)
    print ("pthon thrift server start")
    server.serve()
    print ("pthon thrift server end")
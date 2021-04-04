package handler;

import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import promt.Promter;
import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;

public class ClientHandler implements SOAPHandler<SOAPMessageContext>{

	private XWSSProcessor processor;


	public ClientHandler(){

		try {
			XWSSProcessorFactory factory = XWSSProcessorFactory.newInstance();

			processor = factory.createProcessorForSecurityConfiguration(getClass().getResourceAsStream("/xwss-config.xml"), new Promter());

			//processor = factory.createProcessorForSecurityConfiguration(getClass().getResourceAsStream("/encrypt-xwss-config.xml"), new Promter());


		} catch (Exception e) {
			throw new RuntimeException(e);
		}


	}

	 
	public boolean handleMessage(SOAPMessageContext context) {


		Boolean out = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
		SOAPMessage message = context.getMessage();

		//Insere as credenciais na sa�da da requisi�ao para o web service
		if(out){

			try {

				ProcessingContext processingContex = processor.createProcessingContext(message);
				processingContex.setSOAPMessage(message);

				SOAPMessage secureMessage = processor.secureOutboundMessage(processingContex);
				context.setMessage(secureMessage);

				System.out.println("SOAP Message saindo do cliente: ");
				secureMessage.writeTo(System.out);


			} catch (Exception e) {
				throw new RuntimeException(e);
			}

		}

		return true;
	}

	 
	public Set<QName> getHeaders() {
		String uri = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";
		QName hdr = new QName(uri,"Security","wsse");
		HashSet<QName> headers = new HashSet<QName>();
		headers.add(hdr);
		return headers;
	}

	 
	public void close(MessageContext arg0) {
		// TODO Auto-generated method stub
	}

	 
	public boolean handleFault(SOAPMessageContext arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}

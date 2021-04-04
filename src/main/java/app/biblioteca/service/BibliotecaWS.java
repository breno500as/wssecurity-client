
package app.biblioteca.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * @author breno
 */

@WebService(name = "BibliotecaWS", targetNamespace = "http://service.biblioteca.app/")
@XmlSeeAlso({ ObjectFactory.class })
public interface BibliotecaWS {

	/**
	 * @param arg0
	 */

	@WebMethod
	@RequestWrapper(localName = "hello", targetNamespace = "http://service.biblioteca.app/", className = "app.biblioteca.service.Hello")
	@ResponseWrapper(localName = "helloResponse", targetNamespace = "http://service.biblioteca.app/", className = "app.biblioteca.service.HelloResponse")
	public void hello(@WebParam(name = "arg0", targetNamespace = "") String arg0);

}

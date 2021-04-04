package client;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;

import handler.ClientHandlerResolver;
import app.biblioteca.service.BibliotecaWS;
import app.biblioteca.service.BibliotecaWSImplService;

public class ClientSoap {


	public static void main(String[] args) {


        // Configurações referentes ao HTTPS, passando o certificado exigido no servidor tomcat
		try {
			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			KeyStore keystore = KeyStore.getInstance("JKS");
			InputStream keystoreStream = ClassLoader.getSystemResourceAsStream("tomcat.jks");
			keystore.load(keystoreStream, "123456".toCharArray());
			trustManagerFactory.init(keystore);

			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			KeyStore keystore2 = KeyStore.getInstance("JKS");
			//KeyStore keystore2 = KeyStore.getInstance("PKCS12");
		//	InputStream keystoreStream2 = ClassLoader.getSystemResourceAsStream("tomcat.pfx");
			InputStream keystoreStream2 = ClassLoader.getSystemResourceAsStream("tomcat.jks");
			keystore2.load(keystoreStream2, "123456".toCharArray());
			kmf.init(keystore2, "123456".toCharArray());

			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(kmf.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
			SSLSocketFactory factory  = sc.getSocketFactory();




			HttpsURLConnection.setDefaultSSLSocketFactory(factory);



		} catch (Exception e) {
			e.printStackTrace();
		}

		BibliotecaWSImplService bService = new BibliotecaWSImplService();

		//Configurações referentes ao ws-security, assinar / criptografar a mensagem soap
		bService.setHandlerResolver(new ClientHandlerResolver());
		BibliotecaWS bibliotecaWS = bService.getBibliotecaWSImplPort();
		BindingProvider provider = (BindingProvider) bibliotecaWS;
		provider.getRequestContext().put("javax.xml.ws.client.connectionTimeout", 300000);
		provider.getRequestContext().put("javax.xml.ws.client.receiveTimeout", 300000);
		bibliotecaWS.hello("Tetas");
		System.out.println("finish");
	}

}

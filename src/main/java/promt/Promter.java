package promt;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.sun.xml.wss.impl.callback.EncryptionKeyCallback;
import com.sun.xml.wss.impl.callback.SignatureKeyCallback;

public class Promter implements CallbackHandler {

	public KeyStore getKeyStore()
			throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore keyStore = KeyStore.getInstance("JKS");
		keyStore.load(getClass().getResourceAsStream("/ws-security.jks"), "123456".toCharArray());
		return keyStore;
	}

	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			Callback callback = callbacks[i];

			// Callback que assina uma mensagem xwss-config.xml
			if (callback instanceof SignatureKeyCallback) {

				SignatureKeyCallback cb = (SignatureKeyCallback) callback;

				if (cb.getRequest() instanceof SignatureKeyCallback.AliasPrivKeyCertRequest) {

					SignatureKeyCallback.AliasPrivKeyCertRequest request = (SignatureKeyCallback.AliasPrivKeyCertRequest) cb
							.getRequest();
					String alias = request.getAlias();

					try {

						X509Certificate certificatePerson = (X509Certificate) getKeyStore().getCertificate(alias);
						request.setX509Certificate(certificatePerson);

						PrivateKey privKey = (PrivateKey) getKeyStore().getKey(alias, "123456".toCharArray());
						request.setPrivateKey(privKey);
					} catch (Exception e) {
						throw new IOException(e.getMessage());
					}

				}

				// Callback que criptografa uma mensagem com um certificado
				// encrypt-xwss-config.xml
			} else if (callback instanceof EncryptionKeyCallback) {
				EncryptionKeyCallback cb = (EncryptionKeyCallback) callback;

				if (cb.getRequest() instanceof EncryptionKeyCallback.AliasX509CertificateRequest) {

					EncryptionKeyCallback.AliasX509CertificateRequest request = (EncryptionKeyCallback.AliasX509CertificateRequest) cb
							.getRequest();
					String alias = request.getAlias();

					try {

						X509Certificate certificatePerson = (X509Certificate) getKeyStore().getCertificate(alias);
						request.setX509Certificate(certificatePerson);

					} catch (Exception e) {
						throw new IOException(e.getMessage());
					}

				}

			}

		}

	}
}

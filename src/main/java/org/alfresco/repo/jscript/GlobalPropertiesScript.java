/**
 * 
 */
package org.alfresco.repo.jscript;

import java.io.Serializable;
import java.util.Map;
import java.util.Properties;

import org.alfresco.repo.processor.BaseProcessorExtension;
import org.alfresco.service.namespace.QName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.alfresco.repo.node.encryption.MetadataEncryptor;

/**
 * @author Tahir
 *
 */
public class GlobalPropertiesScript extends BaseProcessorExtension {

	private Properties globalProperties;
	@Autowired
	@Qualifier("metadataEncryptor")
	private MetadataEncryptor encryptor;

	/**
	 * Returns the value of the property
	 * 
	 * @param key
	 * @return String
	 */

	public String get(String key) {
		return globalProperties.getProperty(key);
	}

	/**
	 * Returns all the keys
	 * 
	 * @return
	 */

	public Object[] getAll() {
		return globalProperties.keySet().toArray();
	}

	/**
	 * @param globalProperties the globalProperties to set
	 */
	public void setGlobalProperties(Properties globalProperties) {
		this.globalProperties = globalProperties;
	}

	/**
	 * Returns the value of the property encrypted
	 * 
	 * @param key
	 * @return String
	 */

	public Map<QName, Serializable> encrypt(Map<QName, Serializable> properties) {
		return encryptor.encrypt(properties);
	}

	/**
	 * Returns the value of the property decrypted
	 * 
	 * @param key
	 * @return String
	 */

	public Map<QName, Serializable> decrypt(Map<QName, Serializable> encryptedProperty) {
		return encryptor.decrypt(encryptedProperty);
	}

}

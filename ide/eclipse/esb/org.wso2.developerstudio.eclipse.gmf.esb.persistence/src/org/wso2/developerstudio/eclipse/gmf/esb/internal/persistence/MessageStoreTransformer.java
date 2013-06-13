/*
 * Copyright (c) WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.developerstudio.eclipse.gmf.esb.internal.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.commons.lang.StringUtils;
import org.apache.synapse.config.xml.MessageStoreSerializer;
import org.apache.synapse.message.store.InMemoryMessageStore;
import org.eclipse.emf.common.util.EList;
import org.wso2.developerstudio.eclipse.gmf.esb.MessageStore;
import org.wso2.developerstudio.eclipse.gmf.esb.MessageStoreParameter;
import org.wso2.developerstudio.eclipse.gmf.esb.MessageStoreType;

public class MessageStoreTransformer {

	public static OMElement createMessageStore(MessageStore model) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		String className = "org.apache.synapse.message.store.InMemoryMessageStore";
		org.apache.synapse.message.store.MessageStore messageStore = new InMemoryMessageStore();
		messageStore.setName(model.getStoreName());

		if (model.getStoreType() == MessageStoreType.CUSTOM) {
			className = model.getProviderClass();
			EList<MessageStoreParameter> parameters2 = model.getParameters();
			for (MessageStoreParameter param : parameters2) {
				if (!StringUtils.isBlank(param.getParameterName())
						&& !StringUtils.isBlank(param.getParameterValue())) {
					parameters.put(param.getParameterName(), param.getParameterValue());
				}
			}
		} else if (model.getStoreType() == MessageStoreType.JMS) {
			className = "org.wso2.carbon.message.store.persistence.jms.JMSMessageStore";
			if (!StringUtils.isBlank(model.getInitialContextFactory())) {
				parameters.put("java.naming.factory.initial",
						model.getInitialContextFactory());
			}
			if (!StringUtils.isBlank(model.getProviderURL())) {
				parameters.put("java.naming.provider.url", model.getProviderURL());
			}
			if (!StringUtils.isBlank(model.getJndiQueueName())) {
				parameters.put("store.jms.destination", model.getJndiQueueName());
			}
			if (!StringUtils.isBlank(model.getInitialContextFactory())) {
				parameters.put("store.jms.connection.factory",
						model.getInitialContextFactory());
			}
			if (!StringUtils.isBlank(model.getUserName())) {
				parameters.put("store.jms.username", model.getUserName());
			}
			if (!StringUtils.isBlank(model.getPassword())) {
				parameters.put("store.jms.password", model.getPassword());
			}
			parameters.put("store.jms.JMSSpecVersion", model.getJmsSpecVersion().getLiteral());
			parameters.put("store.jms.cache.connection",
					((Boolean) model.isEnableCaching()).toString());
			parameters.put("store.jms.ConsumerReceiveTimeOut",
					((Long) model.getTimeout()).toString());

		}
		messageStore.setParameters(parameters);

		OMElement messageStoreElement = MessageStoreSerializer.serializeMessageStore(null,
				messageStore);
		OMAttribute classAttr = messageStoreElement.getAttribute(new QName("class"));
		if (classAttr != null) {
			classAttr.setAttributeValue(className);
		} else {
			messageStoreElement.addAttribute("class", className, null);
		}

		return messageStoreElement;

	}

}

/*
 * Copyright 2012 WSO2, Inc. (http://wso2.com)
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

package org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.deserializer;

import java.util.Properties;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.synapse.Mediator;
import org.apache.synapse.SynapseConstants;
import org.apache.synapse.config.xml.AbstractMediatorFactory;
import org.wso2.carbon.mediator.bam.BamMediator;

public class BamMediatorExtFactory extends AbstractMediatorFactory {

	public static final QName BAM_Q = new QName(SynapseConstants.SYNAPSE_NAMESPACE, "bam");

	public static final String SERVER_PROFILE_LOCATION = "bamServerProfiles";

	public Mediator createSpecificMediator(OMElement omElement, Properties properties) {
		BamMediator bam = new BamMediator();

		String serverProfilePath = SERVER_PROFILE_LOCATION + "/"
				+ this.getServerProfileName(omElement);
		String streamName = this.getStreamName(omElement);
		String streamVersion = this.getStreamVersion(omElement);
		if (isNotNullOrEmpty(serverProfilePath) && isNotNullOrEmpty(streamName)
				&& isNotNullOrEmpty(streamVersion)) {
			bam.setServerProfile(serverProfilePath);
			bam.getStream().setStreamName(streamName);
			bam.getStream().setStreamVersion(streamVersion);
		}
		return bam;
	}

	public QName getTagQName() {
		return BAM_Q;
	}

	private String getServerProfileName(OMElement omElement) {
		OMElement serverProfileElement = omElement.getFirstChildWithName(new QName(
				SynapseConstants.SYNAPSE_NAMESPACE, "serverProfile"));

		if (serverProfileElement != null) {
			OMAttribute serverProfileAttr = serverProfileElement.getAttribute(new QName("name"));
			if (serverProfileAttr != null) {
				return serverProfileAttr.getAttributeValue();
			} else {
				return null;
			}
		}
		return null;
	}

	private String getStreamName(OMElement omElement) {

		OMElement serverProfileElement = omElement.getFirstChildWithName(new QName(
				SynapseConstants.SYNAPSE_NAMESPACE, "serverProfile"));

		if (serverProfileElement != null) {
			OMElement streamConfigElement = serverProfileElement.getFirstChildWithName(new QName(
					SynapseConstants.SYNAPSE_NAMESPACE, "streamConfig"));

			if (streamConfigElement != null) {
				OMAttribute streamNameAttr = streamConfigElement.getAttribute(new QName("name"));
				if (streamNameAttr != null) {
					return streamNameAttr.getAttributeValue();
				} else {
					return null;
				}
			}
			return null;
		}

		return null;
	}

	private String getStreamVersion(OMElement omElement) {

		OMElement serverProfileElement = omElement.getFirstChildWithName(new QName(
				SynapseConstants.SYNAPSE_NAMESPACE, "serverProfile"));

		if (serverProfileElement != null) {
			OMElement streamConfigElement = serverProfileElement.getFirstChildWithName(new QName(
					SynapseConstants.SYNAPSE_NAMESPACE, "streamConfig"));

			if (streamConfigElement != null) {
				OMAttribute streamVersionAttr = streamConfigElement.getAttribute(new QName(
						"version"));
				if (streamVersionAttr != null) {
					return streamVersionAttr.getAttributeValue();
				} else {
					return null;
				}
			}
			return null;
		}

		return null;
	}

	private boolean isNotNullOrEmpty(String string) {
		return string != null && !string.equals("");
	}

}

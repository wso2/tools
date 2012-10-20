package org.wso2.developerstudio.eclipse.gmf.esb.internal.persistence;

import java.util.List;

import org.apache.synapse.endpoints.Endpoint;
import org.apache.synapse.mediators.base.SequenceMediator;
import org.apache.synapse.rest.Resource;
import org.apache.synapse.rest.dispatch.URLMappingHelper;
import org.eclipse.core.runtime.Assert;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.wso2.developerstudio.eclipse.gmf.esb.AggregateMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.CacheMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.CallTemplateMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.CalloutMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.ClassMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.CloneMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.CommandMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.DBLookupMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.DBReportMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.DropMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.EnqueueMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.EnrichMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.EntitlementMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbElement;
import org.wso2.developerstudio.eclipse.gmf.esb.EsbNode;
import org.wso2.developerstudio.eclipse.gmf.esb.EventMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.FaultMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.FilterMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.HeaderMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.IterateMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.LogMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.OAuthMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.PayloadFactoryMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.PropertyMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.RMSequenceMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.RuleMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.ScriptMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.SendMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.Sequence;
import org.wso2.developerstudio.eclipse.gmf.esb.SmooksMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.SpringMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.StoreMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.SwitchMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.ThrottleMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.XQueryMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.XSLTMediator;
import org.wso2.developerstudio.eclipse.gmf.esb.persistence.TransformationInfo;

public class APIResourceTransformer extends AbstractEsbNodeTransformer {

	public void transform(TransformationInfo information, EsbNode subject)
			throws Exception {
		// Check subject.
		Assert.isTrue(
				subject instanceof org.wso2.developerstudio.eclipse.gmf.esb.APIResource,
				"Invalid subject.");
		org.wso2.developerstudio.eclipse.gmf.esb.APIResource visualResource = (org.wso2.developerstudio.eclipse.gmf.esb.APIResource) subject;

		if (information.getTraversalDirection() == TransformationInfo.TRAVERSAL_DIRECTION_IN) {
			Resource resource = new Resource();
			URLMappingHelper a = new URLMappingHelper("Test");
			resource.setDispatcherHelper(a);
			information.getCurrentAPI().addResource(resource);

			// In sequence.
			SequenceMediator inSequence = new SequenceMediator();
			resource.setInSequence(inSequence);

			// Out sequence.
			SequenceMediator outSequence = new SequenceMediator();
			resource.setOutSequence(outSequence);

			information.setOriginInSequence(inSequence);
			information.setOriginOutSequence(outSequence);
			information.setParentSequence(inSequence);

			// Transform output data flow.
			doTransform(information, visualResource.getOutputConnector());

			// Set Fault Sequence
			SequenceMediator faultSequence = new SequenceMediator();
			resource.setFaultSequence(faultSequence);
			TransformationInfo faultInfo = new TransformationInfo();
			faultInfo.setParentSequence(faultSequence);
			faultInfo.setSynapseConfiguration(information
					.getSynapseConfiguration());
			doTransformFaultSequence(faultInfo, getOriginNode(visualResource));
		}
	}

	public void createSynapseObject(TransformationInfo info, EObject subject,
			List<Endpoint> endPoints) {

	}

	public void transformWithinSequence(TransformationInfo information,
			EsbNode subject, SequenceMediator sequence) throws Exception {

	}

	/*
	 * Should be Reviewed and should be altered.
	 */
	private EsbNode getOriginNode(
			org.wso2.developerstudio.eclipse.gmf.esb.APIResource visualResource) {
		EList<EsbElement> children = visualResource.getContainer()
				.getFaultContainer().getMediatorFlow().getChildren();
		for (int i = 0; i < children.size(); ++i) {
			if (children.get(i) instanceof AggregateMediator) {
				if (((AggregateMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof CacheMediator) {
				if (((CacheMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof CalloutMediator) {
				if (((CalloutMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof CallTemplateMediator) {
				if (((CallTemplateMediator) children.get(i))
						.getInputConnector().getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof ClassMediator) {
				if (((ClassMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof CloneMediator) {
				if (((CloneMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof CommandMediator) {
				if (((CommandMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof DBLookupMediator) {
				if (((DBLookupMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof DBReportMediator) {
				if (((DBReportMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof DropMediator) {
				if (((DropMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof EnqueueMediator) {
				if (((EnqueueMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof EnrichMediator) {
				if (((EnrichMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof EntitlementMediator) {
				if (((EntitlementMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof EventMediator) {
				if (((EventMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof FaultMediator) {
				if (((FaultMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof FilterMediator) {
				if (((FilterMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof HeaderMediator) {
				if (((HeaderMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof IterateMediator) {
				if (((IterateMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof LogMediator) {
				if (((LogMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof OAuthMediator) {
				if (((OAuthMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof PayloadFactoryMediator) {
				if (((PayloadFactoryMediator) children.get(i))
						.getInputConnector().getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof PropertyMediator) {
				if (((PropertyMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof RMSequenceMediator) {
				if (((RMSequenceMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof RuleMediator) {
				if (((RuleMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof ScriptMediator) {
				if (((ScriptMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof SendMediator) {
				if (((SendMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof SmooksMediator) {
				if (((SmooksMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof SpringMediator) {
				if (((SpringMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof StoreMediator) {
				if (((StoreMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof SwitchMediator) {
				if (((SwitchMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof ThrottleMediator) {
				if (((ThrottleMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof XQueryMediator) {
				if (((XQueryMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof XSLTMediator) {
				if (((XSLTMediator) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			} else if (children.get(i) instanceof Sequence) {
				if (((Sequence) children.get(i)).getInputConnector()
						.getIncomingLinks().size() == 0)
					return children.get(i);
			}

		}
		return null;
	}

}

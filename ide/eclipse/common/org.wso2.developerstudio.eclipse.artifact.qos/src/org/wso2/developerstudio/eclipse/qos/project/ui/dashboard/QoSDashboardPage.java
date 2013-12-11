 /* Copyright (c) 2013, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wso2.developerstudio.eclipse.qos.project.ui.dashboard;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IExpansionListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.wizards.IWizardDescriptor;
import org.wso2.developerstudio.eclipse.logging.core.IDeveloperStudioLog;
import org.wso2.developerstudio.eclipse.logging.core.Logger;
import org.wso2.developerstudio.eclipse.platform.core.utils.SWTResourceManager;
import org.wso2.developerstudio.eclipse.qos.Activator;
import org.wso2.developerstudio.eclipse.qos.project.ui.wizard.QOSProjectWizard;


public class QoSDashboardPage extends FormPage {
	
	private static final String QOS_WIZARD_ID = "org.wso2.developerstudio.eclipse.artifact.newqosproject";

	private static IDeveloperStudioLog log=Logger.getLog(Activator.PLUGIN_ID);
	private static final String PROJECT_EXPLORER_PARTID = "org.eclipse.ui.navigator.ProjectExplorer";
	private static final String PACKAGE_EXPLORER_PARTID = "org.eclipse.jdt.ui.PackageExplorer";
	private ISelectionListener selectionListener = null;
	private ISelection selection = null;
	private IProject selecledProject;
	private String serviceName;
	
	/**
	 * Create the form page.
	 * @param id
	 * @param title
	 */
	public QoSDashboardPage(String id, String title) {
		super(id, title);
	}

	/**
	 * 
	 * @param editor
	 * @param id
	 * @param title
	 * @param project
	 */

	public QoSDashboardPage(FormEditor editor, String id, String title,IProject project) {
		super(editor, id, title);
		setSelecledProject(project);
		setServiceName("Test");
	}

	/**
	 * Create contents of the form.
	 * @param managedForm
	 */
	protected void createFormContent(IManagedForm managedForm) {
		
		//setting initial selection
		ISelection initialSelection = getSite().getWorkbenchWindow().getSelectionService()
				.getSelection(PROJECT_EXPLORER_PARTID);
		if (initialSelection != null) {
			selection = initialSelection;
		} else {
			initialSelection = getSite().getWorkbenchWindow().getSelectionService()
					.getSelection(PACKAGE_EXPLORER_PARTID);
			if (initialSelection != null) {
				selection = initialSelection;
			}
		}
		
		selectionListener = new ISelectionListener() {
			public void selectionChanged(IWorkbenchPart workbenchPart, ISelection sel) {
				selection = sel;
			}
		};
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(PROJECT_EXPLORER_PARTID,selectionListener);
		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(PACKAGE_EXPLORER_PARTID,selectionListener);

		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();
		form.setText("Quality of Service (QoS)");
		final Composite body = form.getBody();
		GridLayout gridParentLayout = new GridLayout(1,true);
		 
		body.setLayout(gridParentLayout);
		toolkit.decorateFormHeading(form.getForm());
		toolkit.paintBordersFor(body);
		
		Object[] result = CreateMainSection(managedForm, body,"Service Info",10, 10, 600, 30, true);
		Composite serviceInfoComposite = (Composite)result[1];
		GridLayout gridserviceLayout = new GridLayout(3,false);
		serviceInfoComposite.setLayout(gridserviceLayout);
		managedForm.getToolkit().createLabel(serviceInfoComposite, "Services List");
		Combo serviceName = new Combo(serviceInfoComposite, SWT.READ_ONLY);
		serviceName.setText("Service Name :");
		serviceName.add("Sample Service");
		
		 new Label(serviceInfoComposite, SWT.None);
	
		 managedForm.getToolkit().createLabel(serviceInfoComposite, "Project name");
		 managedForm.getToolkit().createText(serviceInfoComposite, "");
         managedForm.getToolkit().createButton(serviceInfoComposite, "browser",SWT.NONE);
		 
		new Label(serviceInfoComposite, SWT.None);
		new Label(serviceInfoComposite, SWT.None);
		
	    Hyperlink createHyperlink = managedForm.getToolkit().createHyperlink(serviceInfoComposite, "Create Project", SWT.NONE);
		createHyperlink.addHyperlinkListener(new HyperlinkAdapter(){
			  @Override
			public void linkActivated(HyperlinkEvent e) {
				   IWizard openWizard = openWizard(QOS_WIZARD_ID);
				   QOSProjectWizard qosProjectWizard = (QOSProjectWizard) openWizard; 
				    
			}		  
		  });
		
		result = CreateMainSection(managedForm, body,
				"Security for the service", 10, 20, 600, 30, false);
		final Composite seccomposite = (Composite) result[1];
		GridLayout gridSecLayout = new GridLayout(2, false);
		seccomposite.setLayout(gridSecLayout);

		String[] names = new String[] { "UsernameToken", "Non-repudiation",
				"Integrity", "Confidentiality" };
		createCategory(managedForm, seccomposite, "Basic Scenarios");
		createSecurityItems(seccomposite, names, managedForm);

		names = new String[] {
				"Sign and Encrypt - X509 Authentication",
				"Sign and Encrypt - Anonymous clients",
				"Encrypt only - Username Token Authentication",
				"Sign and Encrypt - Username Token Authentication",
				"SecureConversation - Sign only - Service as STS - Bootstrap policy - Sign and Encrypt , X509 Authentication",
				"SecureConversation - Encrypt only - Service as STS - Bootstrap policy - Sign and Encrypt , X509 Authentication",
				"SecureConversation - Sign and Encrypt - Service as STS - Bootstrap policy - Sign and Encrypt , X509 Authentication",
				"SecureConversation - Sign Only - Service as STS - Bootstrap policy - Sign and Encrypt , Anonymous clients",
				"SecureConversation - Sign and Encrypt - Service as STS - Bootstrap policy - Sign and Encrypt , Anonymous clients",
				"SecureConversation - Encrypt Only - Service as STS - Bootstrap policy - Sign and Encrypt , Username Token Authentication",
				"SecureConversation - Sign and Encrypt - Service as STS - Bootstrap policy - Sign and Encrypt , Username Token Authentication",
				"Kerberos Authentication - Sign - Sign based on a Kerberos Token.",
				"Sign and Encrypt - X509 Authentication - SAML 2.0 Token Required",
				"Sign and Encrypt - Anonymous clients - SAML 2.0 Token Required" };

		createCategory(managedForm, seccomposite, "Advanced Scenarios");
		createSecurityItems(seccomposite, names, managedForm);

		createCategory(managedForm, seccomposite, "Policy From Registry");
		names = new String[] { "registry" };
		createSecurityItems(seccomposite, names, managedForm);
 
			 
     /*  CreateMainSection(managedForm, body,"Policies",10, 30, 600, 30, false);
		 CreateMainSection(managedForm, body,"Response Caching",10, 50, 600, 30, false);
		 CreateMainSection(managedForm, body,"Access Throttling",10, 60, 600, 30, false);
		 CreateMainSection(managedForm, body,"MTOM",10, 70, 600, 30, false);
		 CreateMainSection(managedForm, body,"Transports",10, 80, 600, 30, false);
		 CreateMainSection(managedForm, body,"Modules",10, 90, 600, 30, false);
		 CreateMainSection(managedForm, body,"Operations",10, 100, 600, 30, false);
		 CreateMainSection(managedForm, body,"Parameters",10, 110, 600, 30, false);
		 
		 */
		
	}

	private void createSecurityItems(Composite seccomposite ,String[] names,IManagedForm managedForm) {
		
		for (String name : names) {
			 Button button1 = new Button(seccomposite, SWT.RADIO);
			 button1.setText("");
			 
			 final ToolTip tip = new ToolTip(seccomposite.getShell(), SWT.BALLOON | SWT.ICON_INFORMATION);
			 tip.setMessage("Here is a message for the user. When the message is too long it wraps. I should say something cool but nothing comes to my mind.");

			  Hyperlink createHyperlink = managedForm.getToolkit().createHyperlink(seccomposite, name, SWT.RADIO);
			  createHyperlink.addHyperlinkListener(new HyperlinkAdapter(){
				  @Override
				public void linkActivated(HyperlinkEvent e) {
					 tip.setVisible(true);
				}
				  
			  });
			  
		   } 
	}

	private Object[] CreateMainSection(IManagedForm managedForm,final Composite body,
			String sectionName,final int x, final int y, final int width, final int height, boolean expand) {
		 Object[] comp = new Object[2];
		final Section sctnCreate = managedForm.getToolkit().createSection(body, Section.TWISTIE | Section.TITLE_BAR);
		sctnCreate.setBounds(x, y, width, height);
		managedForm.getToolkit().paintBordersFor(sctnCreate);
		sctnCreate.setText(sectionName);
		sctnCreate.setExpanded(expand);
		GridData layoutData = new GridData();
		layoutData.minimumWidth = 600;
		layoutData.horizontalAlignment = SWT.FILL;
		layoutData.grabExcessHorizontalSpace = true;
		sctnCreate.setLayoutData(layoutData);
		sctnCreate.addExpansionListener(new IExpansionListener() {
			
			@Override
			public void expansionStateChanging(ExpansionEvent e) {
			}		
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				if(!e.getState()){
				sctnCreate.setBounds(x, y, width, height);	 
				GridData layoutData = (GridData)sctnCreate.getLayoutData();
				layoutData.minimumWidth = 600;
				layoutData.horizontalAlignment = SWT.FILL;
				layoutData.grabExcessHorizontalSpace = true;
				layoutData.heightHint = sctnCreate.getBounds().height;
				body.layout();
				 
				}else{
			    sctnCreate.setBounds(x, y, width, height*10);
			    GridData layoutData = (GridData)sctnCreate.getLayoutData();
			    layoutData.minimumWidth = 600;
			    layoutData.horizontalAlignment = SWT.FILL;
				layoutData.grabExcessHorizontalSpace = true;
				layoutData.heightHint = sctnCreate.getBounds().height;
			    body.layout();
				}
			}
		});	
		comp[0]=sctnCreate;
		Composite composite = managedForm.getToolkit().createComposite(sctnCreate, SWT.NULL);
		managedForm.getToolkit().paintBordersFor(composite);
		sctnCreate.setClient(composite);
		composite.setLayout(new GridLayout(1, false));
		comp[1] = composite;
		
		return comp;
	}
	
	
	/**
	 * Create contents of category
	 * @param managedForm
	 * @param composite
	 * @param category
	 */
	private void createCategory(IManagedForm managedForm,Composite composite, String category){
		Label lblcategory = managedForm.getToolkit().createLabel(composite, category, SWT.NONE);
		lblcategory.setFont(SWTResourceManager.getFont("Sans", 10, SWT.BOLD));
		GridData gd_category = new GridData(SWT.FILL, SWT.CENTER, true, false,2, 1);
		gd_category.verticalIndent=10;
		lblcategory.setLayoutData(gd_category);
		 
	}
 
	/**
	 * Open a project wizard
	 * @param id 
	 */
	private IWizard openWizard(String id) {
		 IWizardDescriptor descriptor = PlatformUI.getWorkbench()
		   .getNewWizardRegistry().findWizard(id);
		 
		 try {
		   if (null != descriptor) {
			 IWorkbenchWizard wizard = descriptor.createWizard();
			 wizard.init(PlatformUI.getWorkbench(), getCurrentSelection());
		     WizardDialog wd = new WizardDialog(PlatformUI.getWorkbench()
						.getActiveWorkbenchWindow().getShell(), wizard);
		     wd.setTitle(wizard.getWindowTitle());
		     QOSProjectWizard qosProjectWizard = (QOSProjectWizard) wizard; 
		     qosProjectWizard.setServiceName(getServiceName());
		     wd.open();
		     return wizard;
		   }
 
		 } catch (CoreException e) {
					   log.error("Cannot open wizard",e);
		 }
		 return null;
		}
	
	/**
	 * Get current selection
	 * @return
	 */
	private IStructuredSelection getCurrentSelection() {
		if (selection instanceof IStructuredSelection) {
			return (IStructuredSelection) selection;
		}
		return new StructuredSelection();
	}
	
	public void dispose() {
		ISelectionService selectionService = getSite().getWorkbenchWindow()
				.getSelectionService();
		selectionService.removeSelectionListener(selectionListener);
		super.dispose();
	}

	public IProject getSelecledProject() {
		return selecledProject;
	}

	public void setSelecledProject(IProject selecledProject) {
		this.selecledProject = selecledProject;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
}

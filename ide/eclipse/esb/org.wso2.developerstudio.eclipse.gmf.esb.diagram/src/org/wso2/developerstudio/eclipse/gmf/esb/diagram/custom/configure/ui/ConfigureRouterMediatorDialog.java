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

package org.wso2.developerstudio.eclipse.gmf.esb.diagram.custom.configure.ui;

import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.wso2.developerstudio.eclipse.gmf.esb.RouterMediator;

public class ConfigureRouterMediatorDialog extends Dialog {
	private Table tblRouters;
	private Text txtRouteExpression;
	private Text txtRoutePattern;
	private Text txtSequenceKey;
	private Text txtEndpointKey;
	private Label lblEndpointKey;
	private Composite comConfig;
	private Button cmdSetBreakAfterRoute;
	private Button cmdSetEndpointKey;
	private Combo cmbSequenceType;
	private Combo cmbEndpointType;
	private Label lblSequenceKey;
	private Button cmdSetSequenceKey;
	
	/**
	 * Router Mediator
	 */
	RouterMediator routerMediator;
	
	/**
	 * Editing domain.
	 */
	private TransactionalEditingDomain editingDomain;

	/**
	 * Command for recording user operations.
	 */
	private CompoundCommand resultCommand;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ConfigureRouterMediatorDialog(Shell parentShell,
			RouterMediator routerMediator, TransactionalEditingDomain editingDomain) {
		super(parentShell);
		this.routerMediator = routerMediator;
		this.editingDomain = editingDomain;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FormLayout());
		
		Label lblRouters = new Label(container, SWT.NONE);
		FormData fd_lblRouters = new FormData();
		fd_lblRouters.right = new FormAttachment(0, 75);
		fd_lblRouters.top = new FormAttachment(0, 10);
		fd_lblRouters.left = new FormAttachment(0, 10);
		lblRouters.setLayoutData(fd_lblRouters);
		lblRouters.setText("Routers");
		
		tblRouters = new Table(container, SWT.BORDER | SWT.FULL_SELECTION);
		FormData fd_tblRouters = new FormData();
		fd_tblRouters.top = new FormAttachment(lblRouters, 9);
		fd_tblRouters.left = new FormAttachment(0, 10);
		fd_tblRouters.bottom = new FormAttachment(0, 169);
		fd_tblRouters.right = new FormAttachment(0, 278);
		tblRouters.setLayoutData(fd_tblRouters);
		
		TableColumn tblclmnNewColumn = new TableColumn(tblRouters, SWT.NONE);
		tblclmnNewColumn.setWidth(100);
		tblclmnNewColumn.setText("New Column");
		
		Button btnAdd = new Button(container, SWT.NONE);
		FormData fd_btnAdd = new FormData();
		fd_btnAdd.right = new FormAttachment(0, 378);
		fd_btnAdd.top = new FormAttachment(0, 36);
		fd_btnAdd.left = new FormAttachment(0, 292);
		btnAdd.setLayoutData(fd_btnAdd);
		btnAdd.setText("Add");
		
		Button btnRemove = new Button(container, SWT.NONE);
		FormData fd_btnRemove = new FormData();
		fd_btnRemove.right = new FormAttachment(0, 378);
		fd_btnRemove.top = new FormAttachment(0, 81);
		fd_btnRemove.left = new FormAttachment(0, 292);
		btnRemove.setLayoutData(fd_btnRemove);
		btnRemove.setText("Remove");
		btnRemove.setEnabled(false);
		
		Label label = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_label = new FormData();
		fd_label.right = new FormAttachment(100, -22);
		fd_label.left = new FormAttachment(0, 10);
		fd_label.top = new FormAttachment(tblRouters, 6);
		label.setLayoutData(fd_label);
		
		comConfig = new Composite(container, SWT.NONE);
		FormData fd_comConfig = new FormData();
		fd_comConfig.bottom = new FormAttachment(100, -10);
		fd_comConfig.left = new FormAttachment(0, 11);
		fd_comConfig.right = new FormAttachment(100, -22);
		fd_comConfig.top = new FormAttachment(0, 177);
		comConfig.setLayoutData(fd_comConfig);
		comConfig.setLayout(new GridLayout(3, false));
		comConfig.setEnabled(false);
		
		cmdSetBreakAfterRoute = new Button(comConfig, SWT.CHECK);
		cmdSetBreakAfterRoute.setText("Break After Route");
		new Label(comConfig, SWT.NONE);
		new Label(comConfig, SWT.NONE);
		
		Label lblRouteExpression = new Label(comConfig, SWT.NONE);
		lblRouteExpression.setText("Route expression");
		
		txtRouteExpression = new Text(comConfig, SWT.BORDER);
		txtRouteExpression.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button cmdSetRouteExpression = new Button(comConfig, SWT.NONE);
		cmdSetRouteExpression.setText("..");
		
		Label lblRoutePattern = new Label(comConfig, SWT.NONE);
		lblRoutePattern.setText("Route pattern");
		
		txtRoutePattern = new Text(comConfig, SWT.BORDER);
		txtRoutePattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(comConfig, SWT.NONE);
		
		Label lblSequenceType = new Label(comConfig, SWT.NONE);
		lblSequenceType.setText("Sequence type");
		
		cmbSequenceType = new Combo(comConfig, SWT.READ_ONLY);
		cmbSequenceType.setItems(new String[] {"None", "Anonymous", "From Registry"});
		cmbSequenceType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbSequenceType.select(0);
		cmbSequenceType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cmbSequenceType.getSelectionIndex()==2){
					lblSequenceKey.setEnabled(true);
					txtSequenceKey.setEnabled(true);
					cmdSetSequenceKey.setEnabled(true);
				} else{
					lblSequenceKey.setEnabled(false);
					txtSequenceKey.setEnabled(false);
					cmdSetSequenceKey.setEnabled(false);
				}
			}
		});
		new Label(comConfig, SWT.NONE);
		
		lblSequenceKey = new Label(comConfig, SWT.NONE);
		lblSequenceKey.setText("Sequence key");
		lblSequenceKey.setEnabled(false);
		
		txtSequenceKey = new Text(comConfig, SWT.BORDER);
		txtSequenceKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtSequenceKey.setEnabled(false);
		
		cmdSetSequenceKey = new Button(comConfig, SWT.NONE);
		cmdSetSequenceKey.setText("..");
		cmdSetSequenceKey.setEnabled(false);
		
		Label lblEndpointType = new Label(comConfig, SWT.NONE);
		lblEndpointType.setText("Endpoint type");
		
		cmbEndpointType = new Combo(comConfig, SWT.READ_ONLY);
		cmbEndpointType.setItems(new String[] {"None", "Anonymous", "From Registry"});
		cmbEndpointType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		cmbEndpointType.select(0);
		cmbEndpointType.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(cmbEndpointType.getSelectionIndex()==2){
					lblEndpointKey.setEnabled(true);
					txtEndpointKey.setEnabled(true);
					cmdSetEndpointKey.setEnabled(true);
				} else{
					lblEndpointKey.setEnabled(false);
					txtEndpointKey.setEnabled(false);
					cmdSetEndpointKey.setEnabled(false);
				}
			}
		});
		new Label(comConfig, SWT.NONE);
		
		lblEndpointKey = new Label(comConfig, SWT.NONE);
		lblEndpointKey.setText("Endpoint key");
		lblEndpointKey.setEnabled(false);
		
		txtEndpointKey = new Text(comConfig, SWT.BORDER);
		txtEndpointKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		txtEndpointKey.setEnabled(false);
		
		cmdSetEndpointKey = new Button(comConfig, SWT.NONE);
		cmdSetEndpointKey.setText("..");
		cmdSetEndpointKey.setEnabled(false);		


		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 504);
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText("Router Mediator Configuration");
	}
	
	/**
	 * Utility method for retrieving the result {@link CompoundCommand} which is
	 * used to record user operations.
	 * 
	 * @return result command.
	 */
	private CompoundCommand getResultCommand() {
		if (null == resultCommand) {
			resultCommand = new CompoundCommand();
		}
		return resultCommand;
	}
}

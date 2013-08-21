/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RemoteServiceClientGenWizardPage extends WizardPage {
	private Text projectName;
	  private Composite container;

	  public RemoteServiceClientGenWizardPage() {
	    super("Service Details");
	    setTitle("Service Details Page");
	    setDescription("Client code Generating wizard");
	  }

	  @Override
	  public void createControl(Composite parent) {
	    container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 2;
	    Label label1 = new Label(container, SWT.NONE);
	    label1.setText("Project name ");

	    projectName = new Text(container, SWT.BORDER | SWT.SINGLE);
	    projectName.setText("");
	    projectName.addKeyListener(new KeyListener() {

	      @Override
	      public void keyPressed(KeyEvent e) {
	      }

	      @Override
	      public void keyReleased(KeyEvent e) {
	        if (!projectName.getText().isEmpty()) {
	          setPageComplete(true);

	        }
	      }

	    });
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    projectName.setLayoutData(gd);
	    setControl(container);
	    setPageComplete(true);
	  }

	  public String getProjectName() {
	    return projectName.getText();
	  }
}

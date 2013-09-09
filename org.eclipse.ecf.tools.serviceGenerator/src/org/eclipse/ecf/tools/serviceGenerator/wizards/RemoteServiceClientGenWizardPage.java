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
	private Text packageName;
	private Text className;
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

	    
	    projectName = createTextFeild("Project name:");
	    packageName = createTextFeild("Package:");
	    className = createTextFeild("Class:");
	    setControl(container);
	    setPageComplete(false);
	  }

	private Text createTextFeild(String name) {
		Label label2 = new Label(container, SWT.NONE);
	    label2.setText(name);
	    Text temp = new Text(container, SWT.BORDER | SWT.SINGLE);
	    temp.setText("");
	    temp.addKeyListener(new KeyListener() {
	      @Override
	      public void keyPressed(KeyEvent e) {
	      }

	      @Override
	      public void keyReleased(KeyEvent e) {
	    	  if(packageName!=null && projectName!=null && className!=null){
	        if (!packageName.getText().isEmpty()&&!projectName.getText().isEmpty()&&!className.getText().isEmpty()) {
	          setPageComplete(true);
	        }else{
	           setPageComplete(false);
	        }
	        }
	      }
	    });
	    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
	    temp.setLayoutData(gd);
	    
	    return temp;
	}

	  
	  
	  public String getProjectName() {
	    return projectName.getText();
	  }
	  
	  public String getPackageName() {
		    return packageName.getText();
       }
	  
	  public String getclassName() {
		    return className.getText();
     }
	  
}

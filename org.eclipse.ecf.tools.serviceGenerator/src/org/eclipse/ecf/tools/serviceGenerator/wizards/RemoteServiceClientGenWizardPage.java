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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class RemoteServiceClientGenWizardPage extends WizardPage {
	private Text projectName;
	private Text packageName;
	private Text className;
	private Text serviceName;
	private Text serviceURL;
	private Combo radioservieType;
	private Combo combocontainers;
    private Composite container;
    private String serviceContainerType;
    private String serviceType;
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

	    packageName = createTextFeild("Package:");
	    className = createTextFeild("Class:");
	   // GridLayout newlayout = new GridLayout();
	   // container.setLayout(newlayout);
/*	    layout.numColumns = 1;
	    Label line = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
	    GridData linegd = new GridData(GridData.FILL_HORIZONTAL);
	    line.setLayoutData(linegd);
	    layout.numColumns = 2;*/
	    serviceName = createTextFeild("Service Interface:");
	    Label label3 = new Label(container, SWT.NONE);
	    label3.setText("Select service type");
	    radioservieType = new Combo(container, SWT.READ_ONLY);
	    String items[] = {"sync", "Async"};
	    radioservieType.setItems(items);
	    radioservieType.select(0);
	    radioservieType.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	setServiceType(radioservieType.getText());
	        }
	      });
	    GridData gd1 = new GridData(GridData.BEGINNING);
	    radioservieType.setLayoutData(gd1);
	 //  new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
	    setServiceURL(createTextFeild("Service URL:"));
	    Label label4 = new Label(container, SWT.NONE);
	    label4.setText("Select container type");
	    combocontainers = new Combo(container, SWT.RADIO);
	    String containers[] = {"r-osgi", "generic"};
	    setControl(container);
	    combocontainers.setItems(containers);
	    combocontainers.select(0);
	    combocontainers.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	setServiceContainerType(combocontainers.getText());
	        }
	      });
	    GridData gd2 = new GridData(GridData.BEGINNING);
	    combocontainers.setLayoutData(gd2);
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
	    	  if(packageName!=null && serviceName!=null && className!=null){
	        if (!packageName.getText().isEmpty()&&!serviceName.getText().isEmpty()&&!className.getText().isEmpty()) {
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

	public Text getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(Text serviceURL) {
		this.serviceURL = serviceURL;
	}

	public String getServiceContainerType() {
		return serviceContainerType;
	}

	public void setServiceContainerType(String serviceContainerType) {
		this.serviceContainerType = serviceContainerType;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	  
}

/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.wizards;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.eclipse.ecf.tools.serviceGenerator.handler.ClientGenCommandHandler;
import org.eclipse.ecf.tools.serviceGenerator.utils.Logger;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class RemoteServiceClientGenWizardPage extends WizardPage {
	private static final String PDE_NEW_PROJECT_WIZARD = "org.eclipse.pde.ui.NewProjectWizard"; //TODO "org.eclipse.pde.ui." not ECF's namespace
	private static final String ORG_ECLIPSE_PDE_PLUGIN_NATURE = "org.eclipse.pde.PluginNature";
	private Combo projectName;
	private Text packageName;
	private Text className;
	private Text serviceName;
	private Text serviceURL;
	private Combo combocontainers;
    private Composite container;
    private Button newWizard;
    private String serviceContainerType;
    private String serviceType;
    private IProject iProject;
    private boolean isMenuClick;
    Logger log = new Logger(Activator.context);
	  public RemoteServiceClientGenWizardPage() {
	    super("Service Details");
	    setTitle("Service Details Page");
	    setDescription("Client code Generating wizard");
	  }
	  
	  private void loadProject(Combo items){
		   IProject project = ClientGenCommandHandler.getProject();
		   if(project!=null){
			   items.setData(project.getName(), project);
	           items.add(project.getName());
	           ClientGenCommandHandler.setProject(null);
	           isMenuClick = false;
		   }else{
		   IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
           items.removeAll();
           isMenuClick = true;
		   for (IProject prj : projects) {
				try {
		            if (!prj.isOpen() || !prj.hasNature(ORG_ECLIPSE_PDE_PLUGIN_NATURE) ){
		            	continue;
		            }
		            items.setData(prj.getName(), prj);
		            items.add(prj.getName());
	            } catch (CoreException e1) {
	            	log.log(1, e1.getMessage());
		            continue;
	            }
		   }
		   }
	  }
	  private Listener openPluginProjectWizard(final Combo items){
	  Listener openerListener = new Listener() {
	      public void handleEvent(Event event) {
	    	  openWizard(PDE_NEW_PROJECT_WIZARD);
	    	  loadProject(items);
	    	  items.select(0);
	      }
	    };
	    return openerListener;
	  }
	  
	  private void openWizard(String id) {
			 IWizardDescriptor descriptor = PlatformUI.getWorkbench()
			   .getNewWizardRegistry().findWizard(id);
			 if  (descriptor == null) {
			   descriptor = PlatformUI.getWorkbench().getImportWizardRegistry()
			   .findWizard(id);
			 }
			 if  (descriptor == null) {
			   descriptor = PlatformUI.getWorkbench().getExportWizardRegistry()
			   .findWizard(id);
			 }
			 try  {
			   if  (descriptor != null) {
			     IWizard wizard = descriptor.createWizard();
			     WizardDialog wd = new  WizardDialog(PlatformUI.getWorkbench().getDisplay()
			       .getActiveShell(), wizard);
			     wd.setTitle(wizard.getWindowTitle());
			     wd.open();
			   }
			 } catch  (CoreException e) {
				 log.log(1, e.getMessage());
	     }
	}

 
	  @Override
	  public void createControl(Composite parent) {
 
	    container = new Composite(parent, SWT.NONE);
	    GridLayout layout = new GridLayout();
	    container.setLayout(layout);
	    layout.numColumns = 3;
 
	    GridData projectGD =  new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    Label labelProjectName = new Label(container, SWT.NONE);
	    labelProjectName.setText("Select a Project");
	    labelProjectName.setLayoutData(projectGD);
	    labelProjectName.pack();
	    
	    GridData projectGD1 =  new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    projectName =new Combo(container, SWT.READ_ONLY | SWT.LEFT_TO_RIGHT);
	    projectName.setLayoutData(projectGD1);
	    loadProject(projectName);
	    projectName.select(0);
	    projectName.addSelectionListener(new SelectionAdapter() {
	    public void widgetSelected(SelectionEvent e) {
	          RemoteServiceClientGenWizard.setProject((IProject) projectName.getData(projectName.getText()));
	        }
	      });
	    projectName.pack();
	    
	    GridData projectGD2 =  new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    newWizard = new Button(container, SWT.PUSH);
	    newWizard.setText("New Project");
	    newWizard.addListener(SWT.Selection, openPluginProjectWizard(projectName));
	    newWizard.setLayoutData(projectGD2);
	    newWizard.setEnabled(isMenuClick);
	    newWizard.pack();
	     
	    packageName = createTextFeild("Package:");
	    
	    className = createTextFeild("Class:");
	    
	    drawVarticalLine();
	    
	    serviceName = createTextFeild("Service Interface:");
	   
	    drawVarticalLine();
	    
	    //TODO Indicate optional input fields
	    //TODO Pay attention to typos in methods,class,... names ("Feild" vs "Field")
	    setServiceURL(createTextFeild("Service URL:"));
	    
	    GridData containerGD = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    Label labelContainerType = new Label(container, SWT.NONE);
	    labelContainerType.setText("Select container type");
	    labelContainerType.setLayoutData(containerGD);
	    
	    GridData containerGD1 = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    containerGD1.horizontalSpan =2;
	    combocontainers = new Combo(container, SWT.NONE);
	    String containers[] = {"ecf.r_osgi.peer", "ecf.generic.client"};
	    setControl(container);
	    combocontainers.setItems(containers);
	    combocontainers.select(0);
	    setServiceContainerType(combocontainers.getText());
	    combocontainers.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent e) {
	        	setServiceContainerType(combocontainers.getText());
	        }
	      });
	    combocontainers.setLayoutData(containerGD1);
	    combocontainers.pack();   
	    setPageComplete(false);
	  }

	private void drawVarticalLine() {
		GridData seperatorV =  new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    seperatorV.horizontalSpan =3;
	    Label labelseperatorV = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
	    labelseperatorV.setLayoutData(seperatorV);
	}
     
	private Text createTextFeild(String name) {
		GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
		Label labelName = new Label(container, SWT.NONE);
	    labelName.setText(name);
	    labelName.setLayoutData(gd);
	    GridData gdlb = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	    gdlb.horizontalSpan = 2;
	    Text temp = new Text(container, SWT.BORDER | SWT.SINGLE);
	    temp.setText("");
	    temp.addKeyListener(new KeyListener() {
	      @Override
	      public void keyPressed(KeyEvent e) {
	    	  validate();
	      }

	      @Override
	      public void keyReleased(KeyEvent e) {
	    	  validate();
	      }

		private void validate() {
			if(packageName!=null && serviceName!=null && className!=null){
	        if (!packageName.getText().isEmpty()&&!serviceName.getText().isEmpty()&&!className.getText().isEmpty()) {
	          setPageComplete(true);
	        }else{
	           setPageComplete(false);
	        }
	        }
		}
	    });
	    temp.setLayoutData(gdlb);
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

	public String getServiceURL() {
		return serviceURL.getText();
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
	public Text getServiceName() {
		return serviceName;
	}

	public void setServiceName(Text serviceName) {
		this.serviceName = serviceName;
	}

	public IProject getiProject() {
		return iProject;
	}

	public void setiProject(IProject iProject) {
		this.iProject = iProject;
	}
	  
}

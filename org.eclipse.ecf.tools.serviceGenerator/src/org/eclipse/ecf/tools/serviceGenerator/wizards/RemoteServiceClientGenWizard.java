/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.eclipse.ecf.tools.serviceGenerator.handler.ClientGenCommandHandler;
import org.eclipse.ecf.tools.serviceGenerator.templates.ServiceClientTemplate;
import org.eclipse.ecf.tools.serviceGenerator.utils.DependanciesConst;
import org.eclipse.ecf.tools.serviceGenerator.utils.Logger;
import org.eclipse.ecf.tools.serviceGenerator.utils.ManifestEditor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;


public class RemoteServiceClientGenWizard extends Wizard implements INewWizard{

	protected  RemoteServiceClientGenWizardPage page;
	private static IProject project;
	private Logger log;
	public RemoteServiceClientGenWizard() {
		setWindowTitle("Client Code Gen Wizard");
		log = new Logger(Activator.context);
	}
	
	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		
	}

	@Override
	public void addPages() {
		page = new RemoteServiceClientGenWizardPage();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		 try{
 			IJavaProject iJavaProject = JavaCore.create(getProject());
			Properties prop = new Properties();
			File file = getProject().getFile("service.properties").getLocation().toFile();
			if(!file.exists()){
				file.createNewFile();
			}
			FileOutputStream outputStream = new FileOutputStream(file);
			prop.setProperty("serviceUrl", page.getServiceURL());
			prop.setProperty("container", page.getServiceContainerType());
			prop.store(outputStream, "remote service runtime properties");
			IFolder srcFolder = iJavaProject.getProject().getFolder("src");
	     	IPackageFragmentRoot rootpackage = iJavaProject.getPackageFragmentRoot(srcFolder);
	     	IPackageFragment sourcePackage = rootpackage.createPackageFragment(page.getPackageName(), false, null);
	     	String template = ServiceClientTemplate.createServiceConsumerClassTemplete(project.getName(),
	     			page.getPackageName(), page.getclassName(),page.getServiceName().toString());
			sourcePackage.createCompilationUnit(page.getclassName()+".java", template, false, null);
			String[] importsList = new String[]{DependanciesConst.ECF_REMOTE
					,DependanciesConst.ECF_CORE,DependanciesConst.ECF_IDENTITY};
				String rawPath =getProject().getFile("META-INF/MANIFEST.MF").getLocationURI().getRawPath();
				for (String pluigimport : importsList) {
					ManifestEditor.addPluginDependency("Import-Package",pluigimport,"version",null, false,rawPath);
				}
			doRefresh(iJavaProject);
			return true;
		 }catch(Exception e){
			// TODO Never ever swallow exceptions. A user does not pay attention
			// to the log and thus will simply be lost because nothing happens
			// in case of error.
			 log.log(1, "coudn't complete the wizard !"+e.getMessage(), e);
		 }
		 return false;
	}
	
	public static IProject getProject() {
		ClientGenCommandHandler.setProject(null);//Reset the static reference
		return project;
	}

	public static void setProject(IProject project) {
		RemoteServiceClientGenWizard.project = project;
	}
	
	public void doRefresh(IJavaProject javaProject) throws InvocationTargetException,
			InterruptedException {
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(
				Display.getDefault().getActiveShell());
		progressMonitorDialog.create();
		progressMonitorDialog.open();
		progressMonitorDialog.run(true, false, new ProjectRefreshJob(
				javaProject));
	}

	private class ProjectRefreshJob implements IRunnableWithProgress {

		private IJavaProject javaProject;

		public ProjectRefreshJob(IJavaProject javaProject) {
			this.javaProject = javaProject;
		}

		@Override
		public void run(IProgressMonitor monitor) {
			try {
				javaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,
						monitor);
			} catch (CoreException e) {
				log.log(1, "Code gen process stop " + e.getMessage(), e);
			}
		}
	}
}

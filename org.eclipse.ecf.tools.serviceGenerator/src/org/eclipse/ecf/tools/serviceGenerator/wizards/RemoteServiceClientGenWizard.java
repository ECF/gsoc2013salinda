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
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ecf.tools.serviceGenerator.handler.ClientGenCommandHandler;
import org.eclipse.ecf.tools.serviceGenerator.templates.ServiceClientTemplate;
import org.eclipse.ecf.tools.serviceGenerator.utils.JavaProjectUtils;
import org.eclipse.ecf.tools.serviceGenerator.utils.ManifestEditor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.IWizardDescriptor;


public class RemoteServiceClientGenWizard extends Wizard implements INewWizard{

	protected  RemoteServiceClientGenWizardPage page;
	private static IProject project;
	public RemoteServiceClientGenWizard() {
		setWindowTitle("Client Code Gen Wizard");
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
			File file = project.getFile("service.properties").getLocation().toFile();
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
			String imports = "org.eclipse.ecf.remoteservice,org.eclipse.ecf.core,org.eclipse.ecf.core.identity";
			String[] importsList = imports.split(",");
			String rawPath = project.getFile("META-INF/MANIFEST.MF").getLocationURI().getRawPath();
			for (String pluigimport : importsList) {
		    	ManifestEditor.addPluginDependency("Import-Package",pluigimport,"version",null, false,rawPath);
			}
 			project.refreshLocal(IResource.DEPTH_INFINITE,new NullProgressMonitor());
			return true;
		 }catch(Exception e){
			 e.printStackTrace();
		 }
		 return false;
	}
	
	

	@Override
	public void init(IWorkbench arg0, IStructuredSelection arg1) {
		// TODO Auto-generated method stub
		
	}

	public static IFolder getWorkspaceFolder(IProject project, String...folderNamePath){
		IFolder parentFolder = project.getFolder(folderNamePath[0]);
		List<String> folderPath = new ArrayList<String>(Arrays.asList(folderNamePath));
		folderPath.remove(0);
		String[] newFolderNamePath=folderPath.toArray(new String[]{});
		return getWorkspaceFolder(parentFolder, newFolderNamePath);
	}
	public static IFolder getWorkspaceFolder(IFolder parentFolder,
			String...newFolderNamePath) {
		IFolder leafFolder = parentFolder;
		for (String pathElement:newFolderNamePath) {
			leafFolder = parentFolder.getFolder(pathElement);
			parentFolder = leafFolder;
		}
		return leafFolder;
	}

	public static IProject getProject() {
		ClientGenCommandHandler.setProject(null);//Reset the static reference
		return project;
	}

	public static void setProject(IProject project) {
		RemoteServiceClientGenWizard.project = project;
	}
}

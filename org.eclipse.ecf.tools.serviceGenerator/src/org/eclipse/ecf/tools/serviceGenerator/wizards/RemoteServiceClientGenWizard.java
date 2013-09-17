/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.wizards;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ecf.tools.serviceGenerator.handler.ClientGenCommandHandler;
import org.eclipse.ecf.tools.serviceGenerator.templates.ServiceClientTemplate;
import org.eclipse.ecf.tools.serviceGenerator.utils.JavaProjectUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;


public class RemoteServiceClientGenWizard extends Wizard implements INewWizard{

	protected  RemoteServiceClientGenWizardPage page;
	
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
			IProject project = ClientGenCommandHandler.getProject();
		//	project.create(progressMonitor);
		//	project.open(progressMonitor);
			
			
			//IFolder  srcFolder= getWorkspaceFolder(project, "src");
		//	JavaProjectUtils.createFolder(srcFolder);
			IJavaProject iJavaProject = JavaCore.create(project);
			IFolder srcFolder = iJavaProject.getProject().getFolder("src");

	     	IPackageFragmentRoot rootpackage = iJavaProject.getPackageFragmentRoot(srcFolder);
	     	IPackageFragment sourcePackage = rootpackage.createPackageFragment(page.getPackageName(), false, null);
	     	String template = ServiceClientTemplate.createServiceConsumerClassTemplete(page.getPackageName(), page.getclassName());
			sourcePackage.createCompilationUnit(page.getclassName()+".java", template, false, null);
			/*IFolder  metaInf = getWorkspaceFolder(project, "META-INF");
			if (!metaInf.exists()) {
				metaInf.create(false, true, null);
			}
			String createManifestFileTemplate = ServiceClientTemplate.createManifestFileTemplate(page.getProjectName());
			File  menifest = new File(metaInf.getLocation().toFile(),"MANIFEST.MF");
			JavaProjectUtils.createFile(menifest, createManifestFileTemplate);
			JavaProjectUtils.addJavaSupportAndSourceFolder(project, srcFolder);*/
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
}

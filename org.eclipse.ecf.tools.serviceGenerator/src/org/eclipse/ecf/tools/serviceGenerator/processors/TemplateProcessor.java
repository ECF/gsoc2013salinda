/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.processors;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.eclipse.ecf.tools.serviceGenerator.utils.Logger;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;


public class TemplateProcessor {
	private IPackageFragmentRoot root;
	private IJavaProject javaProject;
	private  Logger logger;
	public TemplateProcessor(IJavaProject javaProject){
		 logger = new Logger(Activator.context);
		 this.javaProject = javaProject;
		 IFolder srcFolder = javaProject.getProject().getFolder("src");
		 root = javaProject.getPackageFragmentRoot(srcFolder);
	}
	
	public void generateAstTemplate(CompilationUnit unit,String packgeName,String newClazz) throws CoreException, InvocationTargetException, Exception {
		IPackageFragment sourcePackage = root.createPackageFragment(packgeName, false, null);
		
		try{
		    sourcePackage.createCompilationUnit(newClazz+".java",unit.toString() , false, null);
		}catch(Exception e){
			  logger.log(0, "file already exsits"+e.getMessage(), e);
			  ICompilationUnit compilationUnit = sourcePackage.getCompilationUnit(newClazz+".java");
			  compilationUnit.delete(false, new NullProgressMonitor());
			  doRefresh();//need to refresh after delete files before create again
			  sourcePackage.createCompilationUnit(newClazz+".java",unit.toString() , false, null);
		}
	}
	
	public void generateStrTemplate(String unit,String packgeName,String newClazz) throws CoreException, InvocationTargetException, InterruptedException {
		IPackageFragment sourcePackage = root.createPackageFragment(packgeName, false, null);
		
		try{
		    sourcePackage.createCompilationUnit(newClazz+".java",unit , false, null);
		}catch(Exception e){
			  logger.log(0, "file already exsits"+e.getMessage(), e);
			  ICompilationUnit compilationUnit = sourcePackage.getCompilationUnit(newClazz+".java");
			  compilationUnit.delete(false, new NullProgressMonitor());
			  doRefresh();//need to refresh after delete files before create again
			  sourcePackage.createCompilationUnit(newClazz+".java",unit , false, null);
		}
	}

	public void doRefresh() throws InvocationTargetException,
			InterruptedException {
		ProgressMonitorDialog progressMonitorDialog = new ProgressMonitorDialog(
				Display.getDefault().getActiveShell());
		progressMonitorDialog.create();
		progressMonitorDialog.open();
		progressMonitorDialog.run(true, false,new ProjectRefreshJob(javaProject));
	}


	private class ProjectRefreshJob implements IRunnableWithProgress {
        
		private IJavaProject javaProject;
		public ProjectRefreshJob(IJavaProject javaProject){
			this.javaProject =javaProject;
		}
		@Override
		public void run(IProgressMonitor monitor){
			try {
				javaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE,monitor);
			} catch (CoreException e) {
				logger.log(1, "Code gen process stop "+e.getMessage(), e);
			}
		}
	}
}

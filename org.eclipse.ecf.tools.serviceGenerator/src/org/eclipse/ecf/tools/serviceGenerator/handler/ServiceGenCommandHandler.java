/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.eclipse.ecf.tools.serviceGenerator.processors.AstProcessor;
import org.eclipse.ecf.tools.serviceGenerator.processors.ResourcesProcessor;
import org.eclipse.ecf.tools.serviceGenerator.processors.TemplateProcessor;
import org.eclipse.ecf.tools.serviceGenerator.templates.ServiceRegClazzTemplate;
import org.eclipse.ecf.tools.serviceGenerator.utils.Annotaions;
import org.eclipse.ecf.tools.serviceGenerator.utils.AsyncProperties;
import org.eclipse.ecf.tools.serviceGenerator.utils.DependanciesConst;
import org.eclipse.ecf.tools.serviceGenerator.utils.Logger;
import org.eclipse.ecf.tools.serviceGenerator.utils.ManifestEditor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.osgi.framework.BundleException;

public class ServiceGenCommandHandler extends AbstractHandler  {
	private Logger log;
	private static final String MSG_TITLE ="Generate R-Service";
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		log = new Logger(Activator.context);
		IStructuredSelection selection =
                (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(arg0);
		boolean result=false;
		List<ICompilationUnit> iCompilationUnits=null;
		try {
			iCompilationUnits = ResourcesProcessor.getICompilationUnits(selection);
		} catch (JavaModelException e) {
			log.log(1, "coudn't find the compilation units !"+e.getMessage(), e);
		}
		 for (ICompilationUnit iCompilationUnit : iCompilationUnits) {
		 boolean temp  = clazzGen(iCompilationUnit);
		 if(!result){
			 result = temp;
		 }
		}
		 if(!result){
		 Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		 MessageDialog.openError(shell, MSG_TITLE, AsyncProperties.Service_Gen_Error_Msg);
		 }
		return null;
	}
	 /**
     * control the new template generation flow
     * @param selection-selected file by the user
     */
	private boolean clazzGen(ICompilationUnit icompilationUnit) {
		try { 
		 /* getting java project associated with selected compilation unit, this need to create new units*/
		 IJavaProject javaProject = ResourcesProcessor.getJavaProject(icompilationUnit);
		 /*getting  the service type  1-Async Service and 2-sync service return 0 if this is not annotated using
		  * @remote Service */
         int serviceType = ResourcesProcessor.getServiceType(icompilationUnit);
		 
		 if(serviceType==Annotaions.RService.getCode()||serviceType==Annotaions.ARService.getCode()){
			 /*JSL4 based processor new units generate by this processor */
			 AstProcessor astProcessor = new AstProcessor(icompilationUnit,log);
			 /*This is used to serialize the new compilation units created by astprocessor */
			 TemplateProcessor templateProcessor = new TemplateProcessor(javaProject);
			 
			 /*getting selected icompilationUnit information*/
			 String pacKagename =ResourcesProcessor.getPackageName(icompilationUnit);
			 String interfaceName  = ResourcesProcessor.getInterfaceName(icompilationUnit);
			 
			 /*creating new names for interface, package and Impl-clazz*/
			 String generatedInterfaceName = createNewInterfaceName(serviceType, interfaceName);
			 String impleName = createNewImpleClazzName(generatedInterfaceName);
			 String implPackgeName = createImplePackageName(pacKagename);
			 String regClazzName = generatedInterfaceName +"ServiceRegister";
			 			 
			 /*create a new Type for new Interface*/
				TypeDeclaration createdType = astProcessor
						.createType(
								generatedInterfaceName,
								true,
								AsyncProperties.ASYNC_SERVICE_STR_IASYNC_REMOTE_SERVICEPROXY);
			 /*create a new Type for Impl-Clazz*/
			 TypeDeclaration createdImpleType = astProcessor.createType(impleName, false,generatedInterfaceName);
			 			 
			 /*create a new package for implClazz*/
			 astProcessor.cretaePackage(pacKagename,implPackgeName);
			 	
			 /*adding explicit imports */
			 List<String> exImports = new ArrayList<String>();
			 /*importing interface into impl-clazz*/
			 exImports.add(pacKagename+"."+generatedInterfaceName);
			 astProcessor.addImports(exImports,serviceType);
			 
			 /*adding methods signatures and methods for interface and impl-clazz*/
			 astProcessor.addMethods(createdType,createdImpleType,serviceType);
			 
			 String classTemplete = ServiceRegClazzTemplate.createServiceRegisterTemplete(implPackgeName, regClazzName, generatedInterfaceName, pacKagename,impleName,javaProject.getProject());
			 /*for a Async-service create both Async interface and imple-clazz*/
			 if(serviceType==Annotaions.ARService.getCode()){
			 templateProcessor.generateAstTemplate(astProcessor.getNewunit(), pacKagename, generatedInterfaceName);
			 templateProcessor.generateAstTemplate(astProcessor.getImpleunit(), implPackgeName, impleName);
			 templateProcessor.generateStrTemplate(classTemplete, implPackgeName, regClazzName);
			 
			 }else if(serviceType==Annotaions.RService.getCode()){
				 /*for a sync-service imple-clazz Only*/
				 templateProcessor.generateAstTemplate(astProcessor.getImpleunit(), implPackgeName, impleName);
				 templateProcessor.generateStrTemplate(classTemplete, implPackgeName, regClazzName);
			 }
			 addDependancies(javaProject);
			 templateProcessor.doRefresh();
			 return true;
		 }else{
			 return false;
		   }
		 } catch (Throwable e) {
				log.log(1, "Class generating process has faild !"+e.getMessage(), e);
				return false;
	    }
	}
	private void addDependancies(IJavaProject javaProject)
			throws BundleException, IOException {
		String[] importsList = new String[]{DependanciesConst.ECF_REMOTE,DependanciesConst.ANNOTATIONS
			,DependanciesConst.ECF_CORE,DependanciesConst.FUTURE,DependanciesConst.ECF_IDENTITY};
		String rawPath =javaProject.getProject().getFile("META-INF/MANIFEST.MF").getLocationURI().getRawPath();
		for (String pluigimport : importsList) {
			ManifestEditor.addPluginDependency("Import-Package",pluigimport,"version",null, false,rawPath);
		}
	}

	private String createImplePackageName(String pacKagename) {
		String implPackgeName = pacKagename+".Impl";
		return implPackgeName;
	}

	private String createNewImpleClazzName(String name) {
		String impleName = name + "Impl";
		return impleName;
	}

	private String createNewInterfaceName(int serviceType, String interfaceName) {
		if(serviceType==Annotaions.ARService.getCode()){
		      return interfaceName + "Async";
		 }else if(serviceType==Annotaions.RService.getCode()){
			 return interfaceName;
		 }
		return null;
	}

}

/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.processors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.ecf.tools.servicegenerator.utils.AnnotaionTypes;
import org.eclipse.ecf.tools.servicegenerator.utils.RServiceType;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class ResourcesProcessor {
	
 /*public static List<String> getImports(ICompilationUnit compilationUnit) throws JavaModelException {
		List<String>  importsList = new ArrayList<String>();
		IImportDeclaration[] imports = compilationUnit.getImports();
		 for (IImportDeclaration iImportDeclaration : imports) {
			if (!"org.eclipse.ecf.tools.serviceGenerator.annotaions.Async"
					.equals(iImportDeclaration.getElementName())
					&& !"import org.eclipse.ecf.tools.serviceGenerator.annotaions.RemoteService"
							.equals(iImportDeclaration.getElementName())) {
				 importsList.add("import "+iImportDeclaration.getElementName()+";");
			}
		}
		 return importsList;
	}*/
 
 public static String getInterfaceName(ICompilationUnit icompilationUnit){
	 String elementName = icompilationUnit.getElementName();
	 String[] split = elementName.split("\\.");
	 return split[0];
 }
 
 public static int getServiceType(ICompilationUnit icompilationUnit)
			throws JavaModelException {
		IType[] types = icompilationUnit.getTypes();
		for (IType iType : types) {
			if(iType instanceof IAnnotatable){
				IAnnotatable annotatable = (IAnnotatable)iType;
				IAnnotation[] annotations = annotatable.getAnnotations();
				for (IAnnotation iAnnotation : annotations) {
					if(AnnotaionTypes.RService.getStrCode().equals(iAnnotation.getElementName())){
						IMemberValuePair[] memberValuePairs = iAnnotation.getMemberValuePairs();
						 for (IMemberValuePair iMemberValuePair : memberValuePairs) {
							   if("type".equals(iMemberValuePair.getMemberName())){
							         if(RServiceType.ASYNC.getStrCode().equals(iMemberValuePair.getValue().toString())){
							        	 return RServiceType.ASYNC.getCode();
							         }else if (RServiceType.SYNC.getStrCode().equals(iMemberValuePair.getValue().toString())){
							        	 return RServiceType.SYNC.getCode();
							         }
							   }
						}
					}
				}
			}
		}
		return 0;
	} 
 
 public static String getClazzFQ(ICompilationUnit compilationUnit)
			throws JavaModelException {
	    String className =null;
		IType[] types = compilationUnit.getTypes();
		 for ( IType iType : types) {
			 className = iType.getFullyQualifiedName();
		}
		return className;
	}

 public static String getPackageName(ICompilationUnit compilationUnit)throws JavaModelException { 
	 String pacKname ="";
	 IPackageDeclaration[] packageDeclarations = compilationUnit.getPackageDeclarations();
	 	for (IPackageDeclaration iPackageDeclaration : packageDeclarations) {
	 		pacKname =iPackageDeclaration.getElementName();
	 	}
	 	return pacKname;
	}
 
   public static IJavaProject getJavaProject(ICompilationUnit compilationUnit) throws CoreException{
	     IJavaProject javaProject = compilationUnit.getJavaProject();
		 return javaProject;
   }
}

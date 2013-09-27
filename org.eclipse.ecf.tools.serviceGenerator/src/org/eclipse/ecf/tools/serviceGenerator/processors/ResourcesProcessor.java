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
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ecf.tools.serviceGenerator.utils.Annotations;
import org.eclipse.jdt.core.IAnnotatable;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ResourcesProcessor {
	
	
public static List<ICompilationUnit> getICompilationUnits(ISelection selection) throws JavaModelException{
	List<ICompilationUnit> units = new ArrayList<ICompilationUnit>();
	Object element = ((IStructuredSelection)selection).getFirstElement();
	 if (element instanceof IResource) {
       IProject  project= ((IResource)element).getProject();
       IJavaProject ijavaProject = JavaCore.create(project);
         IPackageFragment[] packageFragments = ijavaProject.getPackageFragments();
       for (IPackageFragment iPackageFragment : packageFragments) {
    		   ICompilationUnit[] compilationUnits = iPackageFragment.getCompilationUnits();
    		   for (ICompilationUnit iCompilationUnit : compilationUnits) {
    			   units.add(iCompilationUnit);
			}
    	 }
 
     }else if (element instanceof IPackageFragment) {
    	   IPackageFragment iPackageFragment = (IPackageFragment)element;
		  ICompilationUnit[] compilationUnits = iPackageFragment.getCompilationUnits();
		  for (ICompilationUnit iCompilationUnit : compilationUnits) {
			  units.add(iCompilationUnit);
			}
    	 

     } else if (element instanceof IJavaElement) {
    	 ICompilationUnit icompilationUnit =(ICompilationUnit) element;
    	 units.add(icompilationUnit);
     }
	return units;
	
}	
 
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
					if(Annotations.RService.getStrCode().equals(iAnnotation.getElementName())){
						return Annotations.RService.getCode();
					  }else if(Annotations.ARService.getStrCode().equals(iAnnotation.getElementName())){
						  return Annotations.ARService.getCode();
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

/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

public class JavaProjectUtils {
	public static final String JAVA_NATURE_ID="org.eclipse.jdt.core.javanature";
	
	public static void addJavaSupportAndSourceFolder(IProject project, IFolder sourceFolder) throws CoreException,
		JavaModelException {
			IJavaProject javaProject = addJavaNature(project,true);
				if (sourceFolder!=null){
						addJavaSourceFolder(sourceFolder, javaProject);
					} 
			}
	public static IJavaProject addJavaNature(IProject project, boolean rawClasspath) throws CoreException, JavaModelException {
		 
		 IProjectNature nature = project.getNature(JAVA_NATURE_ID);
		 if(nature==null){
		    addNatureToProject(project, true, JAVA_NATURE_ID);
		 }
		IJavaProject javaProject = JavaCore.create(project);
		IFolder targetFolder = project.getFolder("target");
		targetFolder.create(false, true, null);
		javaProject.setOutputLocation(targetFolder.getFullPath(), null);
		Set<IClasspathEntry> entries = new HashSet<IClasspathEntry>();
		if(rawClasspath){
			entries.addAll(Arrays.asList(getClasspathEntries(javaProject)));
		}
		if(nature==null){
			entries.add(JavaRuntime.getDefaultJREContainerEntry());
		 }
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);
		return javaProject;
	}

	public static IClasspathEntry[] getClasspathEntries(
			IJavaProject javaProject) throws JavaModelException {
		return javaProject.getRawClasspath();
	}

	public static IClasspathEntry[] getClasspathEntries(IProject project) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
		return javaProject.getRawClasspath();
	}

	public static void addJavaSourceFolder(IFolder sourceFolder,
			IJavaProject javaProject) throws CoreException, JavaModelException {
		createFolder(sourceFolder);
       int ClASSPATH_ENTRY_KIND=3;
		IPackageFragmentRoot root = javaProject.getPackageFragmentRoot(sourceFolder);
		IClasspathEntry[] oldEntries = getClasspathEntries(javaProject);
		List<IClasspathEntry> validEntries = new ArrayList<IClasspathEntry>();
		for (IClasspathEntry classpathEntry : oldEntries) {
			if (ClASSPATH_ENTRY_KIND!=classpathEntry.getEntryKind()) {
				validEntries.add(classpathEntry);
			} 
		}
		validEntries.add(JavaCore.newSourceEntry(root.getPath()));
		javaProject.setRawClasspath(validEntries.toArray(new IClasspathEntry[] {}),null);
	}
	
	public static boolean createFolder(IFolder folder) throws CoreException{
		if (folder.exists()){
			return true;
		}else{
			if (folder.getParent()!=null && folder.getParent().exists()){
				folder.create(true, true, null);
				return true;
			}else if (folder.getParent() instanceof IFolder && createFolder((IFolder)folder.getParent())){
				folder.create(true, true, null);
				return true;
			}
		}
		return false;
	}
	
	public static void addNatureToProject(IProject project, boolean addToEnd, String...natureId) throws CoreException{
		IProjectDescription description = project.getDescription();
		String[] natures = description.getNatureIds();
		ArrayList<String> arrayList = new ArrayList<String>();
		if (addToEnd){
    		arrayList.addAll(Arrays.asList(natures));
    		arrayList.addAll(Arrays.asList(natureId));
		}else{
    		arrayList.addAll(Arrays.asList(natureId));
    		arrayList.addAll(Arrays.asList(natures));
		}
		String[] list=new String[arrayList.size()];
		list = arrayList.toArray(list);
		description.setNatureIds(list);
		project.setDescription(description, null);
	}
	
}

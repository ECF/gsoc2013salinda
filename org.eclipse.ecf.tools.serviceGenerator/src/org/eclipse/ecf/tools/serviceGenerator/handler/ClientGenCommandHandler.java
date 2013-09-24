/*******************************************************************************
* Copyright (c) 2013 Salinda Jayawardana. All rights reserved. This
* program and the accompanying materials are made available under the terms of
* the Eclipse Public License v1.0 which accompanies this distribution, and is
* available at http://www.eclipse.org/legal/epl-v10.html
*
******************************************************************************/
package org.eclipse.ecf.tools.serviceGenerator.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ecf.tools.serviceGenerator.Activator;
import org.eclipse.ecf.tools.serviceGenerator.utils.Logger;
import org.eclipse.ecf.tools.serviceGenerator.wizards.RemoteServiceClientGenWizard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class ClientGenCommandHandler extends AbstractHandler{
    private static IProject  project;
    private static final String CLIENT_WIZARD_ID="org.eclipse.ecf.tools.serviceGenerator.wizards.RemoteServiceClientGenWizard";
    private Logger log;
    @Override
	public Object execute(ExecutionEvent evnt) throws ExecutionException {
		 IStructuredSelection selection =
                (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(evnt);
		 Object element = ((IStructuredSelection)selection).getFirstElement();
		 if (element instanceof IResource) {
			 setProject(((IResource)element).getProject());
			 RemoteServiceClientGenWizard.setProject(((IResource)element).getProject());
		 }
		log = new Logger(Activator.context);
		this.openWizard(CLIENT_WIZARD_ID);
		return null;
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
			 log.log(1, "Wizard searching error !"+e.getMessage(), e);
		 }
		}

	public static IProject getProject() {
		return project;
	}

	public static void setProject(IProject project) {
		ClientGenCommandHandler.project = project;
	}
}

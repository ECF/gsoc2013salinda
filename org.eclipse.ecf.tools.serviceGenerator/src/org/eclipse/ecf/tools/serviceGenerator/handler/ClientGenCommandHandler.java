package org.eclipse.ecf.tools.serviceGenerator.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;

public class ClientGenCommandHandler extends AbstractHandler{
    private static IProject  project;
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		 IStructuredSelection selection =
                (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(arg0);
		 Object element = ((IStructuredSelection)selection).getFirstElement();
		 if (element instanceof IResource) {
			 setProject(((IResource)element).getProject());
		 }
		this.openWizard("org.eclipse.ecf.tools.serviceGenerator.wizards.RemoteServiceClientGenWizard");
		return null;
	}

	public  void openWizard(String id) {
		 // First see if this is a "new wizard".
		 IWizardDescriptor descriptor = PlatformUI.getWorkbench()
		   .getNewWizardRegistry().findWizard(id);
		 // If not check if it is an "import wizard".
		 if  (descriptor == null) {
		   descriptor = PlatformUI.getWorkbench().getImportWizardRegistry()
		   .findWizard(id);
		 }
		 // Or maybe an export wizard
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
		   e.printStackTrace();
		 }
		}

	public static IProject getProject() {
		return project;
	}

	public static void setProject(IProject project) {
		ClientGenCommandHandler.project = project;
	}
}

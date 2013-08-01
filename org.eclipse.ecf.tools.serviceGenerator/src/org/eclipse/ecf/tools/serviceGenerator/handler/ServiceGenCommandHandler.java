package org.eclipse.ecf.tools.serviceGenerator.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

public class ServiceGenCommandHandler extends AbstractHandler  {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		IStructuredSelection selection =
                (IStructuredSelection) HandlerUtil.getCurrentSelectionChecked(arg0);
		System.out.println("This is CommandHandler");
		return null;
	}

}

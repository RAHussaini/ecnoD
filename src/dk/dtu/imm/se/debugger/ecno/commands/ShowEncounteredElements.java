package dk.dtu.imm.se.debugger.ecno.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import dk.dtu.imm.se.debugger.ecno.views.DebugView;

public class ShowEncounteredElements extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		
		boolean show = !HandlerUtil.toggleCommandState(event.getCommand());
		DebugView dView = getView();
		if(dView != null) dView.showEncounteredElements(show);
		
		return null;
	}

	private DebugView getView() {
		// TODO Auto-generated method stub
		
		IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(DebugView.ID);
		if(view != null && view instanceof DebugView) {
			return (DebugView) view;
		}
		
		return null;
	}

	

}

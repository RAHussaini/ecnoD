/**
 * 
 */
package dk.dtu.imm.se.debugger.ecno.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.RadioState;

import dk.dtu.imm.se.debugger.ecno.views.DebugView;
import dk.dtu.imm.se.debugger.ecno.views.LayoutType;


/**
 * @author s150962
 *
 */
public class ChangeLayout extends AbstractHandler {

	private String currentValue;

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		String parameter = event.getParameter(RadioState.PARAMETER_ID);
		if (parameter.contentEquals(currentValue)) {
			return null;
			
		}
		
		HandlerUtil.updateRadioState(event.getCommand(), parameter);
		currentValue = parameter;
		
		LayoutType layoutType = LayoutType.valueOf(currentValue);
		if(layoutType !=null) {
			IViewPart viewPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(DebugView.ID);  //Returns the specified view in this view's page or null if none.
		if(viewPart instanceof DebugView){
			((DebugView) viewPart).applyLayout(layoutType);
			
		}
		
		}
		ICommandService service = (ICommandService)HandlerUtil.getActiveWorkbenchWindowChecked(event).getService(ICommandService.class);
		service.refreshElements(event.getCommand().getId(), null);
		return null;
	}

}

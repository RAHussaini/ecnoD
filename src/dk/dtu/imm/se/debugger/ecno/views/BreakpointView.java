package dk.dtu.imm.se.debugger.ecno.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class BreakpointView extends ViewPart {
	
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.BreakpointView";

	public BreakpointView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		Label lbl = new Label (parent, 0);
		lbl.setText(ID);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

package dk.dtu.imm.se.debugger.ecno.views;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class BreakpointViewUtil {
	
	
	
	public static Button createButton(Composite parent, int style, String text, int colWidth, SelectionListener listener){	
		Button button = new Button(parent, style);
		applyGridLayoutData(button, colWidth);
		button.setText(text);
		button.addSelectionListener(listener);
		return button;
	}
	
	public static void applyGridLayoutData(Control control, int hspan){
		GridData data = new GridData();
		data.horizontalSpan = hspan;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		control.setLayoutData(data);
	}
	

}

package dk.dtu.imm.se.debugger.ecno.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.SWTResourceManager;

import dk.dtu.imm.se.debugger.ecno.controllers.ECNODebuggerEngineController;
import dk.dtu.imm.se.debugger.ecno.controllers.IRemoveControlListener;
import dk.dtu.imm.se.debugger.ecno.models.BreakpointModel;
import dk.dtu.imm.se.debugger.ecno.utils.ComboExt;
import dk.dtu.imm.se.ecno.core.IElementType;
import dk.dtu.imm.se.ecno.core.IEventType;

import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class BreakpointView extends ViewPart implements SelectionListener, IRemoveControlListener {
	
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.BreakpointView";

	private Composite breakpointContainer;
	public BreakpointView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		RowLayout parentLayout = new RowLayout(SWT.VERTICAL);	
		parent.setLayout(parentLayout);
		
		 breakpointContainer = new Composite(parent, SWT.BORDER | SWT.V_SCROLL); //
		breakpointContainer.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		breakpointContainer.setLayoutData(new RowData(188, 455));
		
		Label lblAddedBreakpoints = new Label(breakpointContainer, SWT.NONE);
		lblAddedBreakpoints.setBounds(10, 10, 149, 20);
		lblAddedBreakpoints.setText("Added Breakpoints");
		
		Composite BreakpointControllContainer = new Composite(parent, SWT.BORDER);  // this composite contain the add breakpoint button and combo
		BreakpointControllContainer.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
		BreakpointControllContainer.setLayoutData(new RowData(362, 456));
		
		Label lblSel = new Label(BreakpointControllContainer, SWT.NONE);
		lblSel.setText("Select an event and press add breakpoint button");
		lblSel.setBounds(10, 10, 342, 20);
		
		Label lblEventTypes = new Label(BreakpointControllContainer, SWT.NONE);
		lblEventTypes.setBounds(10, 59, 83, 20);
		lblEventTypes.setText("Event types:");
		
		Button btnAddBreakpoint = new Button(BreakpointControllContainer, SWT.PUSH);
		btnAddBreakpoint.setBounds(219, 93, 133, 30);
		btnAddBreakpoint.setText("Add Breakpoint");
		btnAddBreakpoint.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						addBreakpoint();
						
					}});
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}});
		
		
		
		
		Combo combo = new Combo(BreakpointControllContainer, SWT.NONE);
		combo.setBounds(10, 95, 203, 28);
		
		Button btnContinue = new Button(BreakpointControllContainer, SWT.PUSH);
		btnContinue.setBounds(219, 157, 133, 30);
		btnContinue.setText("Continue");
		btnContinue.addSelectionListener( new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				btnContinue.setEnabled(false);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						((ECNODebuggerEngineController) ECNODebuggerEngineController.getInstance()).continueFromBreakpoint();
						
					}
					}).start();
				
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {			
				
			}
			});
		
		btnContinue.setEnabled(false);
	

	}
	private static List<BreakpointModel> breakpoints = new ArrayList<>();
	private ComboExt<IEventType>eventTypes;
	private ComboExt<IElementType>elementTypes;
	private Combo andOrCombo;
	public void addBreakpoint() {
		
		final IEventType eventType = eventTypes.getSelectedValue();
		final IElementType elementType = elementTypes.getSelectedValue();
		final String operator = this.andOrCombo.getText();
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				BreakpointModel breakpoint = new BreakpointModel(BreakpointView.this,
						breakpointContainer, 
						eventType, elementType,
						operator);	
				breakpoints.add(breakpoint);
				 ((ECNODebuggerEngineController) ECNODebuggerEngineController.getInstance()).addBreakpoint(breakpoint);
				updateScrollSize();
			}
			});
	}
	
	protected void updateScrollSize() {
		// TODO Auto-generated method stub
		Point size = this.breakpointContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.breakpointContainer.setSize(size);
		this.breakpointContainer.layout();
		this.breakpointContainer.redraw();
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeControl(Object control) {
		// TODO Auto-generated method stub
		
	}
}

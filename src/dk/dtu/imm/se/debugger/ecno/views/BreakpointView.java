package dk.dtu.imm.se.debugger.ecno.views;

import static dk.dtu.imm.se.debugger.ecno.utils.CustomWidgetUtil.*;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.FillLayout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.wb.swt.SWTResourceManager;

import dk.dtu.imm.se.debugger.ecno.controllers.ECNODebuggerEngineController;
import dk.dtu.imm.se.debugger.ecno.controllers.IBreakpoint;
import dk.dtu.imm.se.debugger.ecno.controllers.IBreakpointListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IComboListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IElementTypeListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IEventTypeListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IGraphItemListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IRemoveControlListener;
import dk.dtu.imm.se.debugger.ecno.models.BreakpointModel;
import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.InteractionModel;
import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;
import dk.dtu.imm.se.debugger.ecno.models.ParameterModel;
import dk.dtu.imm.se.debugger.ecno.providers.InteractionContentProvider;
import dk.dtu.imm.se.debugger.ecno.providers.InteractionLabelProvider;
import dk.dtu.imm.se.debugger.ecno.utils.ComboExt;
import dk.dtu.imm.se.ecno.core.IElementType;
import dk.dtu.imm.se.ecno.core.IEventType;
import dk.dtu.imm.se.ecno.runtime.Interaction;



import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;

public class BreakpointView extends ViewPart implements SelectionListener, IRemoveControlListener, IBreakpointListener, 
IGraphItemListener,IEventTypeListener, IElementTypeListener {
	
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.BreakpointView";

	//**********************************************************************

	
	private Group parameterGroup;
	private List<ParameterModel> parameters = new ArrayList<>();
	private final List<Control> parameterControls = new ArrayList<>();
	
	private final List<ElementModel> elements = new ArrayList<>(); 

	private ComboExt<InteractionModel> interactionsCombo;
	private ComboExt<ObjectModel> graphItemsCombo;

	private final List<IComboListener<InteractionModel, ComboExt<InteractionModel>>> interactionsListeners = new ArrayList<>();
	private final List<IComboListener<ObjectModel, ComboExt<ObjectModel>>> graphItemsListener = new ArrayList<>();
	
	private ComboExt<IEventType> eventTypes;
	private ComboExt<IElementType> elementTypes;
	private Combo andOrCombo;
	
	private ComboExt<IEventType> interactionEventTypes;
	private ComboExt<ElementModel> interactionElements;
	private final List<Interaction> interactions = new ArrayList<>();
	private ListViewer interactionList;
	
	private Button continueBtn;
	private static List<BreakpointModel> breakpoints = new ArrayList<>();
	
	private ScrolledComposite breakpointScroll;
	private Composite breakpointContainer;

	
	public void createPartControl(Composite parent) {

		FillLayout parentLayout = new FillLayout();
		parentLayout.type= SWT.VERTICAL;
		parentLayout.marginHeight = 10;
		parentLayout.marginWidth = 5;
		parent.setLayout(parentLayout);

		breakpointScroll = new ScrolledComposite(parent, SWT.V_SCROLL);
		
		breakpointContainer = new Composite(breakpointScroll, SWT.NONE);		
		breakpointContainer.setLayout(new FillLayout(SWT.VERTICAL));
		
		breakpointScroll.setContent(breakpointContainer);
		breakpointScroll.setExpandVertical(true);

		
		Composite addBreakpointContainer = new Composite(parent, SWT.ALL);
		applyGridLayout(addBreakpointContainer, 5);
		
		createLabel(addBreakpointContainer, SWT.NONE, "Event types:", 3);
		createLabel(addBreakpointContainer, SWT.NONE, "Element types:", 2);
		
		eventTypes = createComboExt(addBreakpointContainer, SWT.NONE, 2, null, null);		
		andOrCombo = createCombo(addBreakpointContainer, SWT.READ_ONLY, 1, new String[]{"AND", "OR"});
		elementTypes = createComboExt(addBreakpointContainer, SWT.READ_ONLY, 2, null, null);
				
		//add breakpoint button
		createButton(addBreakpointContainer, SWT.PUSH, "Add breakpoint", 1, new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Display.getDefault().asyncExec(new Runnable() {	
					@Override
					public void run() {
						addBreakpoint();
					}
				});
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		
		continueBtn = createButton(addBreakpointContainer, SWT.PUSH, "Continue", 2, new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				continueBtn.setEnabled(false);
				new Thread(new Runnable() {
					@Override
					public void run() {
						ECNODebuggerEngineController.getInstance().continueFromBreakpoint();
					}
				}).start();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		continueBtn.setEnabled(false);
		
		Group interactionsGroup = new Group(addBreakpointContainer, SWT.NONE);
		interactionsGroup.setText("Interactions");
		applyGridLayoutData(interactionsGroup, 5);
		applyGridLayout(interactionsGroup, 2);
		
		createLabel(interactionsGroup, SWT.NONE, "Element:", 1);
		createLabel(interactionsGroup, SWT.NONE, "Event type:", 1);

		interactionElements = createComboExt(interactionsGroup, SWT.READ_ONLY, 1, null, null);
		interactionElements.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementModel element = interactionElements.getSelectedValue();
				IEventType[] eventTypes = ECNODebuggerEngineController.getInstance().getEventTypes(element.getNode());
				String[] keys = new String[eventTypes.length];
				for(int i = 0; i < keys.length; i++) keys[i] = eventTypes[i].getName();
				interactionEventTypes.setComboValues(eventTypes, keys);
				interactionEventTypes.setEnabled(true);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		interactionEventTypes = createComboExt(interactionsGroup, SWT.READ_ONLY, 1, null, null);
		interactionEventTypes.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ElementModel element = interactionElements.getSelectedValue();
				IEventType eventType = interactionEventTypes.getSelectedValue();
				List<Interaction> interactions = ECNODebuggerEngineController.getInstance().getInteractions(element, eventType);
				BreakpointView.this.interactions.clear();
				BreakpointView.this.interactions.addAll(interactions);
				interactionList.refresh();
//				interactions.get(0).getTriggerEvent().getParameter(interactions.get(0)..getType().getFormalParametersList().get(0)).getValue()
//				interactions.get(0).getTriggerEvent().getParameter(interactions.get(0).getTriggerEvent().getType().getFormalParametersList().get(0)).getValue()
//				((EventTypeImpl)interactions.get(0).getTriggerEvent()).getParameters()
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {}
		});
		
		interactionList = new ListViewer(interactionsGroup, SWT.V_SCROLL);
		applyGridLayoutData(interactionList.getControl(), 2);
		interactionList.setLabelProvider(new InteractionLabelProvider());
		interactionList.setContentProvider(new InteractionContentProvider());
		interactionList.setInput(interactions);
		
			
		
		ECNODebuggerEngineController.getInstance().addEventTypeListener(this);
		ECNODebuggerEngineController.getInstance().addElementTypeListener(this);
		ECNODebuggerEngineController.getInstance().addBreakpointListener(this);
		ECNODebuggerEngineController.getInstance().addElementListener(this);
		
		// if window has been closed we need to reinitialize breakpoint views
		for(BreakpointModel b : breakpoints){
			b.init(breakpointContainer, this);
		}
		updateScrollSize();
	}
	
	public void breakpointHit(InteractionModel interaction){
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				continueBtn.setEnabled(true);
			}
		});
	}

	public void addBreakpoint(){
		final IElementType elementType = elementTypes.getSelectedValue();
		final IEventType eventType = eventTypes.getSelectedValue();
		final String operator = this.andOrCombo.getText();
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
			
				BreakpointModel breakpoint = new BreakpointModel(BreakpointView.this,
						breakpointContainer, 
						eventType, 
						elementType, 
						operator);
				
				breakpoints.add(breakpoint);
				ECNODebuggerEngineController.getInstance().addBreakpoint(breakpoint);
				updateScrollSize();
			}
		});
		
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	}



	public void addParameters(List<ParameterModel> parameters, SelectionListener submitListener){

		clearParameters();

		if(parameters.size() > 0){
			parameterGroup.setVisible(true);
		}else{
			parameterGroup.setVisible(false);
			return;
		}

		this.parameters = parameters;

		for(ParameterModel parameter : parameters){
//			if(parameter.getOptionKeys() != null)
//				System.out.println("size: " + parameter.getOptionKeys().size());
			//adding label for parameter
			Label label = new Label(parameterGroup, SWT.NONE);
			parameterControls.add(label);
			applyGridLayoutData(label, 1);
			label.setText(parameter.getName());

			// assume we want a String argument if no values
			if(parameter.getSelectionOptions() == null){
				System.out.println("adding textbox");
				Text text = new Text(parameterGroup, SWT.SINGLE);
				applyGridLayoutData(text, 1);
				text.addModifyListener(parameter);
				parameterControls.add(text);
			}else{
				ComboExt<Object> options = new ComboExt<>(parameterGroup, SWT.READ_ONLY);
				applyGridLayoutData(options, 1);
				options.setComboValues(parameter.getSelectionOptions(), parameter.getOptionKeys().toArray(new String[0]));
				parameterControls.add(options);
				options.addSelectionListener(parameter);
			}
		}

		Button button = new Button(parameterGroup, SWT.PUSH);
		applyGridLayoutData(button, 2);
		button.setText("Select");
		button.addSelectionListener(submitListener);
		parameterControls.add(button);

		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				parameterGroup.layout();
				parameterGroup.getParent().layout();
			}
		});
	}

	public List<ParameterModel> getParameters(){
		return this.parameters;
	}
	public void clearParameters(){
		for(Control c : this.parameterControls){
			c.dispose();
		}
		parameterGroup.setVisible(false);
	}

	

	@Override
	public void widgetSelected(SelectionEvent e) {
//		System.out.println("widget selected: " + e.getSource());
		InteractionModel selectedInteraction = this.interactionsCombo.getComboValue(this.interactionsCombo.getText());
		if(selectedInteraction != null)
			for(IComboListener<InteractionModel, ComboExt<InteractionModel>> c : this.interactionsListeners) c.itemSelected(selectedInteraction, interactionsCombo);

		ObjectModel selectedGraphItem = this.graphItemsCombo.getComboValue(this.graphItemsCombo.getText());
		if(selectedGraphItem != null){
			for(IComboListener<ObjectModel, ComboExt<ObjectModel>> c : this.graphItemsListener) c.itemSelected(selectedGraphItem, this.graphItemsCombo);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
//		System.out.println("widget default selected: " + e.getSource());
		InteractionModel selected = this.interactionsCombo.getComboValue(this.interactionsCombo.getText());
		if(selected != null)
			for(IComboListener<InteractionModel, ComboExt<InteractionModel>> c : this.interactionsListeners) c.itemSelected(selected, interactionsCombo);

		ObjectModel selectedGraphItem = this.graphItemsCombo.getComboValue(this.graphItemsCombo.getText());
		if(selectedGraphItem != null){
			for(IComboListener<ObjectModel, ComboExt<ObjectModel>> c : this.graphItemsListener) c.itemSelected(selectedGraphItem, this.graphItemsCombo);
		}
	}

	@Override
	public synchronized void elementTypesChanged(final IElementType[] updatedList) {
		final String[] keys = new String[updatedList.length + 1];
		int index = 0;
		for(IElementType type : updatedList){
			if(type != null)
			keys[index++] = type.getName();
		}
		keys[index] = "(none)";
		
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if(updatedList.length > 0){
					elementTypes.setEnabled(true);
				}
				else {
					elementTypes.setEnabled(false);
				}
				elementTypes.setComboValues(updatedList, keys);
			}
		});
	}

	@Override
	public void eventTypesChanged(final IEventType[] updatedList) {
		final String[] keys = new String[updatedList.length];
		int index = 0;
		for(IEventType type : updatedList){
			keys[index++] = type.getName();
		}
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if(updatedList.length > 0) {
					eventTypes.setEnabled(true);
				}
				else {
					eventTypes.setEnabled(false);
				}

				eventTypes.setComboValues(updatedList, keys);
			}
		});
		
	}
	
	@Override
	public void dispose() {
		ECNODebuggerEngineController.getInstance().removeBreakpointListener(this);
		ECNODebuggerEngineController.getInstance().removeElementTypeListener(this);
		ECNODebuggerEngineController.getInstance().removeEventTypeListener(this);
		super.dispose();
	}

	@Override
	public void removeControl(Object breakpoint) {
		ECNODebuggerEngineController.getInstance().removeBreakpoint((IBreakpoint)breakpoint);
		breakpoints.remove(breakpoint);
		updateScrollSize();
		
	}
	
	private void updateScrollSize(){
		Point size = this.breakpointContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		this.breakpointScroll.setMinSize(size);
		this.breakpointContainer.setSize(size);
		this.breakpointContainer.layout();
		this.breakpointScroll.layout();
		this.breakpointContainer.redraw();
		this.breakpointScroll.redraw();
	}

	@Override
	public void elementAdded(ObjectModel item) {
		if(item instanceof ElementModel){
			elements.add((ElementModel)item);
			updateElementsCombo();
		}
	}

	@Override
	public void elementEncountered(ObjectModel item) {
		if(item instanceof ElementModel)
		elements.add((ElementModel)item);
		updateElementsCombo();
	}

	@Override
	public void elementRemoved(ObjectModel item) {
		elements.remove(item);
		updateElementsCombo();
	}

	@Override
	public void eventAdded(ObjectModel item) { //do nothing
	}
	
	private synchronized void updateElementsCombo(){
		final ElementModel[] elements = this.elements.toArray(new ElementModel[0]);
		final String[] keys = new String[elements.length];
		for(int i = 0; i < elements.length; i++) keys[i] = elements[i].getName();

		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				if(elements.length > 0) interactionElements.setEnabled(true);
				else interactionElements.setEnabled(false);
				
				interactionElements.setComboValues(elements, keys);
			}
		});
		
	}
}

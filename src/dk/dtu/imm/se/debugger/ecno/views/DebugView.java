package dk.dtu.imm.se.debugger.ecno.views;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.GridLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;


import dk.dtu.imm.se.debugger.ecno.controllers.DebuggerState;
import dk.dtu.imm.se.debugger.ecno.controllers.ECNODebuggerEngineController;
import dk.dtu.imm.se.debugger.ecno.controllers.IBreakpointListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IComboListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IDebuggerStateListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IECNODebuggerEngineController;
import dk.dtu.imm.se.debugger.ecno.controllers.IGraphItemListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IGraphNodeSelectionListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IInteractionListener;
import dk.dtu.imm.se.debugger.ecno.controllers.IViewPartListener;
import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.EventModel;
import dk.dtu.imm.se.debugger.ecno.models.InteractionModel;
import dk.dtu.imm.se.debugger.ecno.models.NodeModelContentProvider;
import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;
import dk.dtu.imm.se.debugger.ecno.providers.ECNODebuggerContentProvider;
import dk.dtu.imm.se.debugger.ecno.providers.ECNODebuggerLabelProvider;
import dk.dtu.imm.se.debugger.ecno.utils.ComboExt;
import dk.dtu.imm.se.debugger.ecno.utils.ElementsFilter;
import dk.dtu.imm.se.debugger.ecno.utils.EventsFilter;



public class DebugView extends ViewPart implements IZoomableWorkbenchPart, IGraphItemListener, 
IDebuggerStateListener, IBreakpointListener, IInteractionListener, ISelectionChangedListener,
IComboListener<InteractionModel, ComboExt<InteractionModel>> {
	
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.DebugView";
	
	//*********************************************************************
	private GraphViewer gViewer;  // zest  viewer
	private volatile List<ObjectModel> elements = new ArrayList();
	private volatile List<ObjectModel> addedElements = new ArrayList<>(); 
	private volatile List<ObjectModel> encounteredElements = new ArrayList<>();
	//private volatile List <EventModel> events;
	//private volatile List<ElementModel> elements;
	private List<IGraphNodeSelectionListener> listeners = new ArrayList<>();

	private boolean showEncounteredElements = false;

	private ElementsFilter filterEncounteredElements;
	private ElementsFilter filterAddedElements;

	private EventsFilter filterEvents;

	private static List<IViewPartListener <DebugView>> viewPartListeners = new ArrayList<>();
	
	//********************************************************************

	public DebugView() {
		filterEncounteredElements = new ElementsFilter(encounteredElements);
		filterAddedElements = new ElementsFilter(addedElements);		
		
	}

//Conceptually, all viewers perform two primary tasks:
//they help adapt your domain objects into viewable entities
//they provide notifications when the viewable entities are selected or 
	//changed through the UI
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		// here we create the view		
		gViewer = new GraphViewer(parent, SWT.BORDER);
		gViewer.setContentProvider(new ECNODebuggerContentProvider()); //  you need to provide the viewer with information on how to transform your domain object into an item in the UI
		gViewer.setLabelProvider(new ECNODebuggerLabelProvider());
		gViewer.setNodeStyle(ZestStyles.NODES_CACHE_LABEL);
		gViewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		gViewer.setUseHashlookup(true);
		gViewer.setInput(this.elements);
		
		MenuManager menuManager = new MenuManager();
		Menu menu = menuManager.createContextMenu(gViewer.getControl());
		gViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, gViewer);
		getSite().setSelectionProvider(gViewer);
		
		gViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				// TODO Auto-generated method stub
				System.out.println("double click");
				
				
			}});
		applyLayout(LayoutType.TREE);		
		//LayoutAlgorithm layout = setLayout();		
		//gViewer.setLayoutAlgorithm(layout, true);
		gViewer.applyLayout();
		
		IECNODebuggerEngineController debug = ECNODebuggerEngineController.getInstance();
		debug.addElementListener(this);  // register an element listener
		debug.addDebuggerStateListener(this);
		debug.addBreakpointListener(this);
		debug.addInteractionListener(this);
		
		gViewer.addSelectionChangedListener(this);
		
		fillToolBar();
		getSite().setSelectionProvider(gViewer);
		showEncounteredElements(false);
		
		for(IViewPartListener<DebugView> l: viewPartListeners )l.viewPartAdded(this);
		
		

	} //**********end createPartControl*******************
	
	
	public static void addViewPartListerner(IViewPartListener<DebugView> part) {
		
		if(!viewPartListeners.contains(part))viewPartListeners.add(part);		
	}
	
	public static void removeViewPartListener(IViewPartListener<DebugView> part) {
		viewPartListeners.remove(part);
	}
	
	
	@Override
	public void dispose() {
		IECNODebuggerEngineController debug = ECNODebuggerEngineController.getInstance();
		debug.removeElementListener(this);
		debug.removeDebuggerStateListener(this);
		super.dispose();
	}
	
	@Override
	public void itemSelected(InteractionModel interaction, ComboExt<InteractionModel> invoker) {
		List<ElementModel> elements = interaction.getElements();
		List<EventModel> events = interaction.getEvents();
		
		this.filterEvents.setExceptions(events);
		for (ObjectModel element : this.elements) {
		element.highlight(false);
		}
		for(ElementModel element : elements) {
			element.highlight(true);
		}
		for(EventModel event : events) {
			event.highlight(true);
		}
	}
	

	private void showEncounteredElements(boolean show) {
	// TODO Auto-generated method stub
		if(showEncounteredElements =show) {
			this.gViewer.removeFilter(filterEncounteredElements);
		}else {this.gViewer.addFilter(filterEncounteredElements);
		}
		update(true);
	
}
	
	public void showAddedElements(boolean show) {
		if(show) {
			this.gViewer.removeFilter(filterAddedElements);
		}else {
			this.gViewer.addFilter(filterAddedElements);
		}
		update(true);
	}
	

	
	private void fillToolBar() {
		// TODO Auto-generated method stub
		 ZoomContributionViewItem toolbarZoomContributionViewItem = new ZoomContributionViewItem(
	                this);
	        IActionBars bars = getViewSite().getActionBars();
	        bars.getMenuManager().add(toolbarZoomContributionViewItem);

		
		
	}

	private LayoutAlgorithm setLayout() {
		// TODO Auto-generated method stub
		
		LayoutAlgorithm layout;
		layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return layout;
	}

	@Override
	public void setFocus() {}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		// TODO Auto-generated method stub
		return gViewer;
	}
	

	public void applyLayout(LayoutType layoutType) {
		// TODO Auto-generated method stub
		LayoutAlgorithm layout;
		
		switch(layoutType){
			case GRID:
				layout = new GridLayoutAlgorithm(LayoutStyles.NONE);
				break;
			case HORIZONTAL_TREE:
				layout = new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				break;
			case RADIAL:
				layout = new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				break;				
			case SPRING:
				layout = new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
				break;
				
			case TREE:
				default:
					layout = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
					break;
				
		}
		this.gViewer.setLayoutAlgorithm(new CompositeLayoutAlgorithm(new LayoutAlgorithm[]{layout,
				new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING)}), true);
	}

	
	
	@Override
	public void elementAdded(ObjectModel element) {
		// TODO Auto-generated method stub
		addElement(element);
		
	}

	@Override
	public void elementEncountered(ObjectModel element) {
		// TODO Auto-generated method stub
		addElement(element);
	}

	

	@Override
	public void eventAdded(ObjectModel event) {
		// TODO Auto-generated method stub
		addElement(event);
		
	}

	@Override
	public void stateChanged(DebuggerState state) {
		// TODO Auto-generated method stub
		switch(state){
		case INITIALIZED:
		case STARTED:
			update(true);
			break;
		case STOPPED:
			this.elements.clear();
			this.encounteredElements.clear();
			this.addedElements.clear();
			break;
		}
		
	}

	public void update(final boolean forceLayout, final Object ... elements) {
		// TODO Auto-generated method stub
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(forceLayout) {
					gViewer.applyLayout();
					gViewer.refresh();
					gViewer.update(elements, null);
				}
				
			}});
		
	}
	
	private boolean applyLayout = true;
	public void update() {
		update(applyLayout);
	}

	@Override
	public void breakpointHit(final InteractionModel interaction) {
		// TODO Auto-generated method stub
		elements.clear();
		interaction.getTriggerElement().highlight(true);
		interaction.getTriggerEvent().highlight(true);
		
		for(ObjectModel o : interaction.getElements()) addElement(o);
		for(ObjectModel o : interaction.getEvents()) addElement(o);
		
		addElement(interaction.getTriggerElement());
		addElement(interaction.getTriggerEvent());
		update(true);			
		
	}
	

	//A lock is a thread synchronization mechanism like synchronized blocks except locks can be more sophisticated than Java's synchronized blocks
	private Lock elementLock = new ReentrantLock();

	
	private void addElement(ObjectModel element) {
		// TODO Auto-generated method stub
		elementLock.lock();
		if(element.getName().contains("Rustam")) {
			System.err.println(" Rustam Test: " + element.getNode() + ";" + element.hashCode() + ";");
		}
		System.out.println("contains ? : " + this.elements.contains(element) + ";" + element);
		if(!this.elements.contains(element)) {
			this.elements.add(element);
		}
		elementLock.unlock();
		
	}

	@Override
	public void interactionsUpdated(List<InteractionModel> interactions) {
		// TODO Auto-generated method stub
		update(true, this.elements);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		// TODO Auto-generated method stub
		if(!(event.getSelection() instanceof IStructuredSelection))return;
		
		Object selection = ((IStructuredSelection) event.getSelection()).getFirstElement();
		if(selection == null)return;
		
		if(selection instanceof ObjectModel) {
			for(IGraphNodeSelectionListener listener : this.listeners) {
				listener.nodeSelected((ObjectModel)selection);
				
			}
		}
		
	}
	

	public void setFilters(ViewerFilter[] filters) {
		ViewerFilter[] newList = new ViewerFilter[filters.length+(showEncounteredElements ? 2:1)];
		int index = 0;
		for(ViewerFilter f: filters) {
			newList[index++] = f;
		}
		newList[index++] = filterEvents;
		if(showEncounteredElements) newList[index] = filterEncounteredElements;
		this.gViewer.setFilters(filters);
	}
	
	
	public void removeFilter(ViewerFilter filter) {
		this.gViewer.removeFilter(filter);
	}
	
	
	private boolean addingFilter = false;
	public void addFilter(ViewerFilter... filter ) {
		
		ViewerFilter[] filters = this.gViewer.getFilters();
		final ViewerFilter[] newFilters = new ViewerFilter[filters.length+filter.length];
		for(int i = 0; i < filters.length; i++){
			newFilters[i] = filters[i];
		}
		for(int i = 0; i < filter.length; i++){
			newFilters[i+filters.length] = filter[i];
		}
		addingFilter = true;
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				gViewer.setFilters(newFilters);
				addingFilter = false;
				updateAddFilterButton();
				// TODO Auto-generated method stub
				
			}
		}).start();
		updateAddFilterButton();
		
		
	}

	protected void updateAddFilterButton() {
		// TODO Auto-generated method stub
		if(addingFilter) {}
		
	}
	
	public void removeFilter(final ViewerFilter... filter) {
		for(ViewerFilter f: filter)
			gViewer.removeFilter(f);
	}

	@Override
	public void elementRemoved(ObjectModel item) {
		
		
	}
	


} //*******class end**********

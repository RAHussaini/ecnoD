package dk.dtu.imm.se.debugger.ecno.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
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

import dk.dtu.imm.se.debugger.ecno.controllers.ECNODebuggerEngineController;
import dk.dtu.imm.se.debugger.ecno.controllers.IECNODebuggerEngineController;
import dk.dtu.imm.se.debugger.ecno.controllers.IGraphItemListener;
import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.EventModel;
import dk.dtu.imm.se.debugger.ecno.models.NodeModelContentProvider;
import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;
import dk.dtu.imm.se.debugger.ecno.providers.ECNODebuggerContentProvider;
import dk.dtu.imm.se.debugger.ecno.providers.ECNODebuggerLabelProvider;



public class DebugView extends ViewPart implements IZoomableWorkbenchPart, IGraphItemListener {
	
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.DebugView";
	private GraphViewer gViewer;  // zest  viewer
	private volatile List<ObjectModel> elements = new ArrayList();
	//private volatile List <EventModel> events;
	//private volatile List<ElementModel> elements;

	public DebugView() {
		// TODO Auto-generated constructor stub
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
		debug.addElementListener(this);
		
		fillToolBar();
		
		
		
		
		
//		Text text = new Text(parent, SWT.BORDER);
//		text.setText(ID);

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
	public void setFocus() {
		// TODO Auto-generated method stub

	}

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
	public void elementAdded(ObjectModel item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementEncountered(ObjectModel item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementRemove(ObjectModel item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eventAdded(ObjectModel item) {
		// TODO Auto-generated method stub
		
	}


} //*******class end**********

package dk.dtu.imm.se.debugger.ecno.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
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
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.EventModel;
import dk.dtu.imm.se.debugger.ecno.models.NodeModelContentProvider;
import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;
import dk.dtu.imm.se.debugger.ecno.providers.ECNODebuggerContentProvider;
import dk.dtu.imm.se.debugger.ecno.providers.ECNODebuggerLabelProvider;
import dk.dtu.imm.se.debugger.views.LayoutType;


public class DebugView extends ViewPart implements IZoomableWorkbenchPart {
	
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
		gViewer.setInput(this.getElements());
		//gViewer.setInput(this.elements); // set the initial input of the viewer
//		NodeModelContentProvider model = new NodeModelContentProvider();
//		gViewer.setInput(model.getNodes());
//		
//		NodeModelContentProvider model = new NodeModelContentProvider();
//		gViewer.setInput(model.getNodes());
		LayoutAlgorithm layout = setLayout();
		
		gViewer.setLayoutAlgorithm(layout, true);
		gViewer.applyLayout();
		fillToolBar();
		
		
		
		
		
//		Text text = new Text(parent, SWT.BORDER);
//		text.setText(ID);

	}
	
	public List <ObjectModel> getElements(){
		
		return elements;
	}

//	public ObjectModel getInitialInput() {
//	// TODO Auto-generated method stub
//		events = new ArrayList<>();
//		events.add(new ElementModel(" element 1"));
//	elements = new ArrayList<>();	
//	elements.add("element 2");
//	return  (ObjectModel) events;
//}

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
		
		
	}


} //*******class end**********

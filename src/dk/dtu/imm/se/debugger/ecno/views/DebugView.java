package dk.dtu.imm.se.debugger.ecno.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.viewers.ZoomContributionViewItem;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import dk.dtu.imm.se.debugger.ecno.models.NodeModelContentProvider;
import dk.dtu.imm.se.debugger.ecno.providers.DebuggerContentProvider;
import dk.dtu.imm.se.debugger.ecno.providers.DebuggerLabelProvider;

public class DebugView extends ViewPart implements IZoomableWorkbenchPart {
	
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.DebugView";
	private GraphViewer gViewer;  // zest  viewer

	public DebugView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		gViewer = new GraphViewer(parent, SWT.BORDER);
		gViewer.setContentProvider(new DebuggerContentProvider());
		gViewer.setLabelProvider(new DebuggerLabelProvider());
		
		NodeModelContentProvider model = new NodeModelContentProvider();
		gViewer.setInput(model.getNodes());
		LayoutAlgorithm layout = setLayout();
		
		gViewer.setLayoutAlgorithm(layout, true);
		gViewer.applyLayout();
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

}

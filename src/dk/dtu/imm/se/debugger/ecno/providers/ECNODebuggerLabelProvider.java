package dk.dtu.imm.se.debugger.ecno.providers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import dk.dtu.imm.se.debugger.ecno.figures.EFigure;
import dk.dtu.imm.se.debugger.ecno.figures.ElementFigure;
import dk.dtu.imm.se.debugger.ecno.figures.EventFigure;
import dk.dtu.imm.se.debugger.ecno.figures.Shapeable;
import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.EventModel;

import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;

//The label provider is responsible for providing an image and text for each item contained in the  viewer.
//As with the content provider, the label provider accepts domain objects as its arguments.
//It is important that instances of label providers are not shared between  viewers because the label provider will be disposed when the viewer is disposed.
public class ECNODebuggerLabelProvider extends LabelProvider implements IFigureProvider, ISelfStyleProvider {

	//only for test
	
			protected ECNODebuggerLabelProvider(){

			}
		
		public ECNODebuggerLabelProvider(GraphViewer viewer){
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					GraphViewer g = (GraphViewer) event.getSource();
					for(Object o : g.getNodeElements()){
						if(o instanceof ObjectModel){
							ObjectModel node = (ObjectModel) o;
							if(node.isHighlighted()) node.getShape().highlight();
							else node.getShape().unHighlight();
						}
					}
					Object element = g.getStructuredSelection().getFirstElement();

					if(element instanceof ObjectModel){
						ObjectModel obj = (ObjectModel) element;
						obj.getShape().highlight();
					}
				}
			});
		}
		public void run(){
			Display display = Display.getDefault();
			Shell shell = new Shell(display);
			shell.setLayout(new FillLayout(SWT.VERTICAL));
			shell.setSize(700, 500);

			Composite composite = new Composite(shell, SWT.NULL);
			FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
			fillLayout.spacing = 2;
			composite.setLayout(fillLayout);

			GraphViewer viewer = new GraphViewer(composite, SWT.BORDER);


			viewer.setContentProvider(new ECNODebuggerContentProvider());
			viewer.setLabelProvider(new ECNODebuggerLabelProvider());


			viewer.setNodeStyle(ZestStyles.NODES_NO_ANIMATION | ZestStyles.NODES_CACHE_LABEL);
			viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);


			List<ObjectModel> list = new ArrayList<>();
			list.add(new ElementModel(shell, null));
			list.add(new ElementModel(display, null));
			
			viewer.setInput(list);

			viewer.setLayoutAlgorithm(new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);

			viewer.applyLayout();

			//highlighting does not work when providing your own figures. we have to handle it in our own way
			viewer.addSelectionChangedListener(new ISelectionChangedListener() {

				@Override
				public void selectionChanged(SelectionChangedEvent event) {
					GraphViewer g = (GraphViewer) event.getSource();
					for(Object o : g.getNodeElements()){
						if(o instanceof ObjectModel){
							((ObjectModel) o).getShape().unHighlight();
						}
					}
					Object element = g.getStructuredSelection().getFirstElement();

					if(element instanceof ObjectModel){
						ObjectModel obj = (ObjectModel) element;
						obj.getShape().highlight();
					}
				}
			});


			shell.open();
			//		shell.pack();
			//		viewer.applyLayout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					// If no more entries in event queue
					display.sleep();
				}
			}	
		}
		public static void main(String[] args) {
			new ECNODebuggerLabelProvider().run();
		}
		@Override
		public String getText(Object element) {
			if (element instanceof ObjectModel) {
				ObjectModel node = (ObjectModel) element;
				return node.getName();
			}


			if (element instanceof EntityConnectionData) {
//				EntityConnectionData test = (EntityConnectionData) element;
//				System.err.println("text for connection: " + test);
				return "";
			}
			return "";
		}

		@Override
		public IFigure getFigure(Object element) {
			EFigure shape = null;
			if(element instanceof ElementModel){
				shape = new ElementFigure(getText(element));
			}else if(element instanceof EventModel){
				shape = new EventFigure(getText(element));
			}else{
				System.out.println("unknown figure: " + element);
			}

			if(element instanceof Shapeable)
				((Shapeable) element).setShape(shape);

//			System.err.println("get figure and is shapeable: " + (element instanceof Shapeable));
			return shape;
		}
		@Override
		public void selfStyleConnection(Object element, GraphConnection connection) {
//			connection.getSource().getData()
//			System.err.println("labelprovider: selfstyle");
		}
		@Override
		public void selfStyleNode(Object element, GraphNode node) {
			if(element instanceof ObjectModel){
				//System.out.println("checking highlight on node");
				ObjectModel viewModel = (ObjectModel) element;
				if(viewModel.isHighlighted()) node.highlight();
				else node.unhighlight();
			}
//			Object data = node.getLayoutEntity().getLayoutInformation();
//			System.out.println(data);
//			int width = node.getBorderWidth();
//			
//			node.setBorderWidth(70);
//			Dimension size = node.getSize();
//			System.out.println("size: " + size);
//			if(element instanceof ObjectViewModel){
//				ObjectViewModel el = (ObjectViewModel) element;
//				Dimension s = el.getShape().getSize();
//				System.out.println("el size: " + s);
//				noOverlap(node);
//				node.setSize(s.width, s.height);
//			}
//			double width = node.getLayoutEntity().getWidthInLayout();
//			double height = node.getLayoutEntity().getHeightInLayout();
//			double x = node.getLayoutEntity().getXInLayout();
//			double y = node.getLayoutEntity().getYInLayout();
//			org.eclipse.draw2d.geometry.Point point = node.getLocation();
//			System.out.println("layout entity: " + x + "," + y + "--" + width + "," + height);

			//		System.out.println("border width: " + width);
//			System.err.println("labelprovider: selfstylenode");
			
		}
		
		public void noOverlap(GraphNode node){
			node.setLocation(node.getSize().width+30, node.getSize().height+30);
		}
	

}

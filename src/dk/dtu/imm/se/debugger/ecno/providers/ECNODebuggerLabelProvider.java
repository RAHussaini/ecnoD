package dk.dtu.imm.se.debugger.ecno.providers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.BaseLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.viewers.ISelfStyleProvider;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;

import dk.dtu.imm.se.debugger.ecno.figures.EFigure;
import dk.dtu.imm.se.debugger.ecno.figures.ElementFigure;
import dk.dtu.imm.se.debugger.ecno.figures.EventFigure;
import dk.dtu.imm.se.debugger.ecno.figures.Shapeable;
import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.EventModel;
import dk.dtu.imm.se.debugger.ecno.models.MyConnection;
import dk.dtu.imm.se.debugger.ecno.models.MyNode;
import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;

//The label provider is responsible for providing an image and text for each item contained in the  viewer.
//As with the content provider, the label provider accepts domain objects as its arguments.
//It is important that instances of label providers are not shared between  viewers because the label provider will be disposed when the viewer is disposed.
public class ECNODebuggerLabelProvider extends LabelProvider implements IFigureProvider, ISelfStyleProvider {

	@Override
	public void selfStyleConnection(Object element, GraphConnection connection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void selfStyleNode(Object element, GraphNode node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IFigure getFigure(Object element) {
		// TODO Auto-generated method stub
		EFigure shape = null;  // EShape is used to load the shape for the corresponding domain object if shape has not yet been loaded, the EShape is asked to create shape
		if (element instanceof ElementModel) {
			shape = new ElementFigure (getText(element));
		}else if (element instanceof EventModel) {
			shape = new EventFigure(getText(element));
		}else {
			//System.out.println("unknown figure: " + element);
			//throw unknownElement(element);
		}
		
		if(element instanceof Shapeable)
			((Shapeable)element).setShape(shape);
		
		return shape;
	}

		
//	private RuntimeException unknownElement(Object element) {
//		// TODO Auto-generated method stub
//		//return new RuntimeException ("Unknows types of element" + element.getClass().getName());
//	}
	
	//The getText method answers a string that represents the label for the domain object, element.
	//If there is no label for the element, answer null.
	@Override
	public String getText(Object element) {
		if(element instanceof MyNode) {
			MyNode myNode = (MyNode)element;
			return myNode.getName();
			
		}
		
		  // Not called with the IGraphEntityContentProvider
        if (element instanceof MyConnection) {
            MyConnection myConnection = (MyConnection) element;
            return myConnection.getLabel();
        }

        if (element instanceof EntityConnectionData) {
            EntityConnectionData test = (EntityConnectionData) element;
            return "";
        }
//        throw new RuntimeException("Wrong type: "
//                + element.getClass().toString());
//    }
	
		
		//''''''''''''''''''''''''''''''''''''
		
//		if (element instanceof ObjectModel) {
//			//< -- ObjectModel node = (ObjectModel) element; // other way around
//			//return node.getName(); // other way around -->
//			if(((ObjectModel)element).getName() == null) {
//				return "Node";
//			} else {
//				return ((ObjectModel)element).getName();
//			}
//	} else if (element instanceof EntityConnectionData) {
//		//return (EntityConnectionData)element).getText(null));
//		return "";
//	
//		} else {
//			throw unknownElement(element);
//		}
		
		return "";
		}

	//The dispose method is called when the  viewer that contains the label provider is disposed. 
	//This method is often used to dispose of the cached images managed by the receiver.
	//This dispose method makes sure the label provider correctly disposes of the images it has created. This is very important to insure that operating system resources are not “leaked.”
	private Map imageCache = new HashMap(11);
	public void dispose() {
		
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((ECNODebuggerLabelProvider)i.next()).dispose();
		}
		imageCache.clear();
	}
	
	

//		 @Override
//		    public String getText(Object element) {
//		        if (element instanceof ElementModel) {
//		            ElementModel myNode = (ElementModel) element;
//		            return myNode.getName();
//		        }
//		        // Not called with the IGraphEntityContentProvider
//		        if (element instanceof MyConnection) {
//		            MyConnection myConnection = (MyConnection) element;
//		            return myConnection.getLabel();
//		        }
//
//		        if (element instanceof EntityConnectionData) {
//		            EntityConnectionData test = (EntityConnectionData) element;
//		            return "";
//		        }
//		        throw new RuntimeException("Wrong type: "
//		                + element.getClass().toString());
//		    }
		
	

}

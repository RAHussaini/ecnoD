package dk.dtu.imm.se.debugger.ecno.providers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.EventModel;
import dk.dtu.imm.se.debugger.ecno.models.MyNode;

public class ECNODebuggerContentProvider implements IGraphEntityContentProvider {
	
	private List <MyNode> nodes;
	

	public ECNODebuggerContentProvider() {
	nodes = new ArrayList<>();
	
	MyNode node = new MyNode(null, "Ali");
	nodes.add(node);
	node = new MyNode(null, "hasib");
	//ElementModel elm = new ElementModel();
	}
	
	//When working with a viewer, you need to provide the viewer with information on 
	//how to transform your domain object into an item in the UI . That is the purpose of an
	//IContentProvider. One of your domain classes could implement this interface. 
	//Instead of "polluting" your domain objects with user interface code, you may want 
	//to create another object to fulfill the  content provider requirements

	
	//The  viewer calls its content providerís getChildren method 
	//when it needs to create or display the child elements of the domain object, parent.
	//This method should answer an array 
	//of domain objects that represent the unfiltered children of parent (more on filtering later).
	
	
	protected GraphViewer viewer;

	//This is the method invoked by calling the setInput method on the  viewer. 
	//In fact, the getElements method is called only in response to the  viewer's setInput method and should answer
	//with the appropriate domain objects of the inputElement. The getElements and getChildren methods operate in a similar way.
	//Depending on your domain objects, you may have the getElements simply return the result of calling getChildren. 
	//The two methods are kept distinct because it provides a clean way
	//to differentiate between the root domain object and all other domain objects.
	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		//The getElements method is used to obtain the root elements for the  viewer.
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		}
		if (inputElement instanceof Collection) {
			return ((Collection) inputElement).toArray();
		}
		return new Object[0];
	}

	
	//@SuppressWarnings("rawtypes")//A raw type is the name of a generic class or interface without any type arguments
	@Override
	public Object[] getConnectedTo(Object entity) {
		// TODO Auto-generated method stub
		 
		if (entity instanceof MyNode) {
			MyNode node = (MyNode) entity;
			return node.getConnectedTo().toArray();
		}
		
		 if (entity instanceof ElementModel) {
	            ElementModel elNode = (ElementModel) entity;
	            Object[] references = elNode.getReferences();
	            //return node.getConnectedTo().toArray();
	            return references;
	            
	        }
		 
		 if (entity instanceof EventModel) {
			 EventModel evNode = (EventModel) entity;
			 Object[] references = evNode.getReferences();
			 
		 }
	        throw new RuntimeException("Type not supported");
	    }
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		this.viewer = (GraphViewer) viewer;
		if(oldInput != null) {
			removeListenerFrom((Object) oldInput);
		}
		if(newInput !=null) {
			addListenerTo((Object) newInput);
		}
	}


	private void addListenerTo(Object newInput) {
//		// TODO Auto-generated method stub
//		newInput.adaddListener(this);
//		for(Iterator iterator = newInput.getElements().iterator(); iterator.hasNext();){
//			Object aObj = (Object) iterator.next();
//			addListenerTo(aObj);
//		}
	}


	private void removeListenerFrom(Object object) {
		// TODO Auto-generated method stub
//		object.addListener(this);
//		for(Iterator iterator = newInput.getElements().iterator(); iterator.hasNext();){
//		Object aObj = (Object) iterator.next();
//			addListenerTo(aObj);
//		}
		
	}
	
	@Override
	public void dispose() {	
		
	}
		
		
	

}

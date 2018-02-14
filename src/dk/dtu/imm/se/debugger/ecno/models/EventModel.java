package dk.dtu.imm.se.debugger.ecno.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertySource;

import dk.dtu.imm.se.debugger.ecno.properties.EventProperties;
import dk.dtu.imm.se.ecno.core.IEventType;
import dk.dtu.imm.se.ecno.runtime.Event;

public class EventModel extends ObjectModel {
	
	public EventModel(IEventType event) {
		this.node = event;
		this.name = event.getName() + ":" + event.getClass().getSimpleName();
	}
	
	
	public EventModel(Event event) {
		this.node = event;
		this.name = event.getType().getName() + ":" + event.getClass().getSimpleName();
	}

	private List<ObjectModel> references;
	
	public void addReference(ObjectModel element) {
		if(references == null) references = new ArrayList<>();
		this.references.add(element);
	}
	
	
	public Object[] getReferences() {
		// TODO Auto-generated method stub
		if(this.references == null) return null;
		return this.references.toArray();
	}

	
	//getAdapter(java.lang.Class<T> adapter) 
    //Returns an object which is an instance of the given class parameter associated with this object or null if no association exists.
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		// TODO Auto-generated method stub
		if(adapter == IWorkbenchAdapter.class)
			return (T) this;
		if(adapter == IPropertySource.class)
			return (T) new EventProperties(this);
		return null;
	}

}

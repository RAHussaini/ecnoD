package dk.dtu.imm.se.debugger.ecno.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import dk.dtu.imm.se.debugger.ecno.models.EventModel;
import dk.dtu.imm.se.ecno.core.IFormalParameter;
import dk.dtu.imm.se.ecno.runtime.Event;
import dk.dtu.imm.se.ecno.runtime.Parameter;

public class EventProperties implements IPropertySource {
	
	private final Event event;
	private final EventModel eventModel;
	private final List<Parameter> parameters;
	

	public EventProperties(EventModel event) {
		super();
		this.event = (Event) event.getNode();
		this.eventModel = event;
		this.parameters = new ArrayList<>();
		
		for(IFormalParameter p: this.event.getType().getFormalParametersList()) {
			parameters.add(this.event.getParameter(p));
			
		}
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		IPropertyDescriptor[] propertyDescriptors = new IPropertyDescriptor[parameters.size()];
		
		for(int i=0; i<parameters.size(); i++) {
			PropertyDescriptor descriptor;
			Parameter param = parameters.get(i);
			
			
			descriptor = new ObjectPropertyDescriptor(i, param.getType().getName());
			propertyDescriptors[i] = (IPropertyDescriptor) descriptor;
			descriptor.setCategory(eventModel.getName());
		}
		return propertyDescriptors;
	}

	@Override
	public Object getPropertyValue(Object name) {
		// TODO Auto-generated method stub
		for (Parameter param : this.parameters) {
			if(name.equals(param)) {
				Object value = param.getValue();
				if(value instanceof String) {
					return value;
				}else {
					return value;
				}
			}
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public String toString() {
		return this.event.getType().getName();}

}

package dk.dtu.imm.se.debugger.ecno.models;

import java.util.Collection;

import org.eclipse.swt.widgets.Composite;

import dk.dtu.imm.se.debugger.ecno.controllers.ECNODebuggerEngineController;
import dk.dtu.imm.se.debugger.ecno.controllers.IBreakpoint;
import dk.dtu.imm.se.debugger.ecno.controllers.IRemoveControlListener;
import dk.dtu.imm.se.ecno.core.IElementType;
import dk.dtu.imm.se.ecno.core.IEventType;
import dk.dtu.imm.se.ecno.runtime.Event;
import dk.dtu.imm.se.ecno.runtime.Interaction;
import dk.dtu.imm.se.ecno.runtime.Link;

public class BreakpointModel implements IBreakpoint{

	private IEventType eventType;
	private IElementType elementType;
	private String operator;
	
	public BreakpointModel(IRemoveControlListener removeListener, Composite parent, IEventType eventType, IElementType elementType, String operator) {
	this.eventType = eventType;
	this.elementType = elementType;
	this.operator = operator;
	init(parent, removeListener);
		
	}

	private void init(Composite parent, IRemoveControlListener removeListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isBreakpoint(Interaction interaction) {
		// TODO Auto-generated method stub
		
		boolean checkEventType = false;
		boolean checkElementType = false;

		boolean hasElementType = false;
		boolean hasEventType = false;
		if(eventType != null) {
			checkEventType = true;
			for(Link link : interaction.getLinks()){
				Collection<Event> events = interaction.getEvents(link);
				for(Event e : events){
					if(e.getType().getName().equals(eventType.getName())){
						hasEventType = true;
						break;
					}
				}
				if(hasEventType) break;
			}
		}
		if(interaction.getTriggerEvent().getType().getName().equalsIgnoreCase(eventType.getName())){
			hasEventType = true;
		}
		if(elementType != null){
			checkElementType = true;
			for(Object o : interaction.getElements()){
				if(((ECNODebuggerEngineController) ECNODebuggerEngineController.getInstance()).isElementType(o, elementType)){
					hasElementType = true;
					break;
				}
			}
			if(((ECNODebuggerEngineController) ECNODebuggerEngineController.getInstance()).isElementType(interaction.getTriggerElement(), elementType)){
				hasElementType = true;
			}
		}
		

		if(checkElementType && checkEventType){
			if(operator.equalsIgnoreCase("and"))
				return hasElementType && hasEventType;
			if(operator.equalsIgnoreCase("or"))
				return hasElementType || hasEventType; 
		}
		if(checkElementType && !checkEventType){
			return hasElementType;
		}
		if(checkEventType && !checkElementType){
			return hasEventType;
		}
		return false;
	}

}

package dk.dtu.imm.se.debugger.ecno.controllers;

import java.util.List;

import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;
import dk.dtu.imm.se.debugger.ecno.views.DebugView;
import dk.dtu.imm.se.ecno.core.IElementType;
import dk.dtu.imm.se.ecno.core.IEventType;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine;
import dk.dtu.imm.se.ecno.runtime.Interaction;

public interface IECNODebuggerEngineController {
	
	void setEngine(ExecutionEngine engine);
	boolean isElementType(Object element, IElementType type);
	void calculateInteractions();
	
	
	void addBreakpoint(IBreakpoint breakpoint);
	void continueFromBreakpoint();	
	
	void addElementListener(IGraphItemListener listener);
	void addDebuggerStateListener(IDebuggerStateListener listener);
	void addBreakpointListener(IBreakpointListener listener);
	void addInteractionListener(IInteractionListener listener);

	void addEventTypeListener(IEventTypeListener listener);
	void addElementTypeListener(IElementTypeListener listener);
	
	void removeBreakpoint(IBreakpoint breakpoint);
	void removeEventTypeListener(IEventTypeListener listener);
	void removeElementTypeListener(IElementTypeListener listener);	
	
	void removeInteractionListener(IInteractionListener listener);
	void removeBreakpointListener(IBreakpointListener listener);
	
	void removeElementListener(IGraphItemListener listener);
	void removeDebuggerStateListener(IDebuggerStateListener listener);
	
	List<IEventType> getEventTypes();
	IEventType[] getEventTypes(Object element);
	List<IElementType> getElementTypes();
	
	List<ObjectModel> getElements();
	List<Interaction> getInteractions(Object element, IEventType eventType);
	

}

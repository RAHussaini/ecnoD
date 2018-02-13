package dk.dtu.imm.se.debugger.ecno.controllers;

import dk.dtu.imm.se.ecno.core.IElementType;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine;

public interface IECNODebuggerEngineController {
	
	void setEngine(ExecutionEngine engine);
	boolean isElementType(Object element, IElementType type);
	
	
	void addBreakpoint(IBreakpoint breakpoint);
	void continueFromBreakpoint();
	
	void addElementListener(IGraphItemListener listener);

}

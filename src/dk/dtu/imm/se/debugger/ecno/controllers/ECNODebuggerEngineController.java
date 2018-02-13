package dk.dtu.imm.se.debugger.ecno.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;
import dk.dtu.imm.se.ecno.core.IElementType;
import dk.dtu.imm.se.ecno.engine.ECNODebugger;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine.InvalidChoiceType;
import dk.dtu.imm.se.ecno.engine.IController;
import dk.dtu.imm.se.ecno.runtime.IChoice;
import dk.dtu.imm.se.ecno.runtime.Interaction;

public class ECNODebuggerEngineController implements IECNODebuggerEngineController,ECNODebugger, IController {

	private static ECNODebuggerEngineController instance;

	public static IECNODebuggerEngineController getInstance() {
		// TODO Auto-generated method stub		
		if (instance == null) {
			return (instance = new ECNODebuggerEngineController());
		}
		return instance;
	}

	//*********************************************
	private ExecutionEngine engine;
	private CountDownLatch latch = null;
	private DebuggerState state = DebuggerState.INITIALIZED; //  intial state
	private volatile List<IDebuggerStateListener> debuggerStateListeners = new ArrayList<>();
	
	private List<IBreakpoint> breakpoints = new ArrayList<>();
	private List<IGraphItemListener> elementListener = new ArrayList<>();
	private List<ObjectModel> encounteredElements = new ArrayList<>();
	private List <ObjectModel> addedElements = new ArrayList<>();
	
	//******************************************************
	
	public void setEngine(ExecutionEngine engine) {
		if(this.engine != null) {			
			destroy();
			
		}
		this.engine = engine;
		this.engine.addController(this);
		this.engine.attachDebugger(this);
		changeState(DebuggerState.STARTED);
	}

	
	
	private void changeState(DebuggerState state) {
		// TODO Auto-generated method stub		
		this.state  = state;
		for (IDebuggerStateListener l : this.debuggerStateListeners) {
			l.stateChanged(state);
		}		
	}
	
	

	private void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void debug(Interaction interaction) {
		// TODO Auto-generated method stub
		// this method is the implementation of dk.dtu.imm.se.ecno.engine.ECNODebugger; interface
		try {
			if(latch != null)
				latch.await();
			System.err.println("Contenue from the breakpoint ...");
			
		}catch(InterruptedException e) {
			
			System.err.println("Latch Error" + e.getMessage());
		}
		
		
	}

	@Override
	public void invalidChoice(IChoice choice, Interaction interaction, InvalidChoiceType type) {
		// TODO Auto-generated method stub
		// this method is the implementation of dk.dtu.imm.se.ecno.engine.ECNODebugger; interface
		
	}

	@Override
	public void addElement(Object element) {
		// TODO Auto-generated method stub
		
		
	}

	@Override
	public void removeElement(Object element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void elementEncountered(Object element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean isElementType(Object element, IElementType elementType) {
		// TODO Auto-generated method stub
		IElementType typeToMatch = engine.getElementType(element);
		if(typeToMatch == null) return false;
		if(elementType == null) return false;
		if(typeToMatch.getName().equals(elementType.getName())) return true;
		return false;
	}



	@Override
	public void addBreakpoint(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		this.breakpoints.add(breakpoint);
		
	}



	@Override
	public void continueFromBreakpoint() {
		// TODO Auto-generated method stub
		latch.countDown();
		
	}



	@Override
	public void addElementListener(IGraphItemListener listener) {
		// TODO Auto-generated method stub
		this.elementListener.add(listener);
		for (ObjectModel o: this.encounteredElements)listener.elementEncountered(o);
		for (ObjectModel o: this.addedElements) listener.elementAdded(o);
		
	}

}

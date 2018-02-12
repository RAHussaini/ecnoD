package dk.dtu.imm.se.debugger.ecno.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import dk.dtu.imm.se.ecno.engine.ECNODebugger;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine.InvalidChoiceType;
import dk.dtu.imm.se.ecno.engine.IController;
import dk.dtu.imm.se.ecno.runtime.IChoice;
import dk.dtu.imm.se.ecno.runtime.Interaction;

public class ECNODebuggerEngineController implements IECNODebuggerEngineController,ECNODebugger, IController {

	private static ECNODebuggerEngineController instance;

	public static Object getInstance() {
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

}

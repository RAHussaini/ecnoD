package dk.dtu.imm.se.debugger.ecno.controllers;

import dk.dtu.imm.se.ecno.core.IEventType;

public interface IEventTypeListener {
	public void eventTypesChanged(IEventType[] updatedList);

}

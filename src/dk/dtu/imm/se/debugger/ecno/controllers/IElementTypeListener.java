package dk.dtu.imm.se.debugger.ecno.controllers;

import dk.dtu.imm.se.ecno.core.IElementType;

public interface IElementTypeListener {

	public void elementTypesChanged(IElementType[] updatedList);
}

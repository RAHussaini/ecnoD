package dk.dtu.imm.se.debugger.ecno.controllers;

import dk.dtu.imm.se.debugger.ecno.models.InteractionModel;

public interface IBreakpointListener {

	void breakpointHit(InteractionModel interaction);
}

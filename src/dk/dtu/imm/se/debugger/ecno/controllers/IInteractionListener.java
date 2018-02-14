package dk.dtu.imm.se.debugger.ecno.controllers;

import java.util.List;

import dk.dtu.imm.se.debugger.ecno.models.InteractionModel;

public interface IInteractionListener {

	void interactionsUpdated(List<InteractionModel> interactions);
}

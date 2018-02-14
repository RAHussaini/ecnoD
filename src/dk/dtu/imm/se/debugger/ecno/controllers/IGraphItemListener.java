package dk.dtu.imm.se.debugger.ecno.controllers;

import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;

public interface IGraphItemListener {
	void elementAdded(ObjectModel item);
	void elementEncountered(ObjectModel item);
	void elementRemoved(ObjectModel item);
	void eventAdded(ObjectModel item);

}

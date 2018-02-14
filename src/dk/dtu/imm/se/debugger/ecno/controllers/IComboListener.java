package dk.dtu.imm.se.debugger.ecno.controllers;

import org.eclipse.swt.widgets.Combo;

public interface IComboListener<T, C extends Combo> {
	public void itemSelected(T item, C invoker);

}

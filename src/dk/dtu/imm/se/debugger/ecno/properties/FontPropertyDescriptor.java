package dk.dtu.imm.se.debugger.ecno.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.*;

public class FontPropertyDescriptor extends PropertyDescriptor {
	
	/**
	 * Creates an property descriptor with the given id and display name.
	 * 
	 * @param id the id of the property
	 * @param displayName the name to display for the property
	 */
	public FontPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}
	
	/**
	 * @see org.eclipse.ui.views.properties.IPropertyDescriptor#createPropertyEditor(Composite)
	 */
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new FontDialogCellEditor(parent);
		if (getValidator() != null)
			editor.setValidator(getValidator());
		return editor;
	}

}

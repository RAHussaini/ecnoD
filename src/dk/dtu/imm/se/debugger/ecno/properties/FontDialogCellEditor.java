package dk.dtu.imm.se.debugger.ecno.properties;

import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FontDialog;

public class FontDialogCellEditor extends DialogCellEditor{
	

	/**
	 * Creates a new Font dialog cell editor parented under the given control.
	 * The cell editor value is <code>null</code> initially, and has no 
	 * validator.
	 *
	 * @param parent the parent control
	 */
	public FontDialogCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * @see org.eclipse.jface.viewers.DialogCellEditor#openDialogBox(Control)
	 */
	protected Object openDialogBox(Control cellEditorWindow) {
		FontDialog ftDialog = new FontDialog(MViewPlugin.getActiveWorkbenchShell());
		
		String value = (String) getValue();
		
		if ((value != null) && (value.length() > 0)) {
			ftDialog.setFontData(new FontData(value));
		}
		FontData fData = ftDialog.open();
			
		if (fData != null) {
			value = fData.toString();
		}
		return value;
	}


}

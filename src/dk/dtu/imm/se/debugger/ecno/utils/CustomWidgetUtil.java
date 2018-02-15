/**
 * 
 */
package dk.dtu.imm.se.debugger.ecno.utils;

import static dk.dtu.imm.se.debugger.ecno.utils.CustomWidgetUtil.applyGridLayoutData;
import java.util.List;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;



/**
 * 
 *custom widgets
 *in SWT constractor have two arguments, parent and style
 *style is an integer  used to control the look of widgets e.g SWT.BORDER  will cause a canvas to have a border
 *widgets without parent can not be created
 *the parent of any widget must be composite
 */
public class CustomWidgetUtil {

	
	public static void applyGridLayout(Composite composite, int numColumns){
		GridLayout gridLayout = new GridLayout(numColumns, false);
		gridLayout.marginWidth = 5;
		gridLayout.marginHeight = 5;
		gridLayout.verticalSpacing = 5;
		gridLayout.horizontalSpacing = 10;
		composite.setLayout(gridLayout);
	}

	public static void applyGridLayoutData(Control control, int hspan){
		GridData data = new GridData();
		data.horizontalSpan = hspan;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		control.setLayoutData(data);
	}
	
	public static Label createLabel(Composite parent, int style, String text, int colWidth){
		Label label = new Label(parent, style);
		applyGridLayoutData(label, colWidth);
		label.setText(text);
		return label;
	}
	
	public static Button createButton(Composite parent, int style, String text, int colWidth, SelectionListener listener){	
		Button button = new Button(parent, style);
		applyGridLayoutData(button, colWidth);
		button.setText(text);
		button.addSelectionListener(listener);
		return button;
	}
	
	public static <T> ComboExt<T> createComboExt(Composite parent, int style, int colWidth, List<T> values, String[] keys){
		ComboExt<T> combo = new ComboExt<>(parent, style);
		if(values != null){
			combo.setComboValues(values, keys);
		}else {
			combo.setEnabled(false);
		}
		applyGridLayoutData(combo, colWidth);
		return combo;
	}
	
	public static Combo createCombo(Composite parent, int style, int colWidth, String[] values){
		Combo combo = new Combo(parent, style);
		combo.setItems(values);
		applyGridLayoutData(combo, colWidth);
		return combo;
	}
	
	

}

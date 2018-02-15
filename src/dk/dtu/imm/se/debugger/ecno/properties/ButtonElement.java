package dk.dtu.imm.se.debugger.ecno.properties;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertySource;

public class ButtonElement implements IWorkbenchAdapter,IAdaptable {
	private String headingName;
	private Button ctl;
	
	/**
	 * Creates a new MarkElement and stores parent element and
	 * location in the text.
	 *
	 * @param parent  the parent of this element
	 * @param heading text corresponding to the heading
	 * @param offset  the offset into the Readme text
	 * @param length  the length of the element
	 */
	public ButtonElement(Button initBtn, String heading) {
		this.headingName = heading;
		this.ctl = initBtn;
	}
	
	/* (non-Javadoc)
	 * Method declared on IAdaptable
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == IWorkbenchAdapter.class)
			return this;
		if (adapter == IPropertySource.class)
			return new ButtonElementProperties(this);
		return null;
	}
	/* (non-Javadoc)
	 * Method declared on IWorkbenchAdapter
	 */
	public ImageDescriptor getImageDescriptor(Object object) {
		return null;
	}
	/* (non-Javadoc)
	 * Method declared on IWorkbenchAdapter
	 */
	public String getLabel(Object o) {
		return headingName;
	}
	/* (non-Javadoc)
	 * Method declared on IWorkbenchAdapter
	 */
	public Object getParent(Object o) {
		return null;
	}
	
	public Button getControl() {
		return ctl;
	}
	/**
	 * @see org.eclipse.ui.model.IWorkbenchAdapter#getChildren(Object)
	 */
	public Object[] getChildren(Object o) {
		return null;
	}

}

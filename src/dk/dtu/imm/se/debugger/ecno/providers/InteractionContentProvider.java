package dk.dtu.imm.se.debugger.ecno.providers;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;

public class InteractionContentProvider implements IStructuredContentProvider{

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		if (inputElement instanceof List<?>) {
			return ((List<?>) inputElement).toArray();
		}
		return null;
	}

}

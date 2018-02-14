package dk.dtu.imm.se.debugger.ecno.utils;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;

public class ElementsFilter extends ViewerFilter {
	
	private final List<ObjectModel> elements;
	

	public ElementsFilter(List<ObjectModel> elements) {
		//super();
		this.elements = elements;
	}


	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		// TODO Auto-generated method stub
		return !elements.contains(element);
	}

}

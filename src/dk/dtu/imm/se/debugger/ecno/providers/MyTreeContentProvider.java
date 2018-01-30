package dk.dtu.imm.se.debugger.ecno.providers;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import notPartOfProject.Company;
import notPartOfProject.Department;


public class MyTreeContentProvider implements ITreeContentProvider {
	
	private final Object[] EMPTY_ARRAY = new Object[0];

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		
		if (inputElement instanceof List) 
			return ((List) inputElement).toArray();
			else
		
		return EMPTY_ARRAY;
		
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Company) {
	         Company company = (Company) parentElement;
	         return company.getListDepartment().toArray();
	       } else
	       if (parentElement instanceof Department) {
	         Department department = (Department) parentElement;
	         return department.getListEmployee().toArray();
	       }
	       return EMPTY_ARRAY;
		
	}
		// TODO Auto-generated method stub
	

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		
		if(element instanceof Company || element instanceof Department) {
			return true;
		}
		return false;
	
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	
	}
	
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}
	
	

}

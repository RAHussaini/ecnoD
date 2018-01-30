package dk.dtu.imm.se.debugger.ecno.providers;

import java.util.Collection;

import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import dk.dtu.imm.se.debugger.ecno.models.ElementModel;

public class DebuggerContentProvider implements IGraphEntityContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		if (inputElement instanceof Object[]) {
			return (Object[]) inputElement;
		}
		if (inputElement instanceof Collection) {
			return ((Collection) inputElement).toArray();
		}
		return null;
	}

	@Override
	public Object[] getConnectedTo(Object entity) {
		// TODO Auto-generated method stub
		 if (entity instanceof ElementModel) {
	            ElementModel node = (ElementModel) entity;
	            return node.getConnectedTo().toArray();
	        }
	        throw new RuntimeException("Type not supported");
	    }
		
		
	

}

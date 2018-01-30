package dk.dtu.imm.se.debugger.ecno.providers;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.MyConnection;

public class DebuggerLabelProvider extends LabelProvider {
	

		 @Override
		    public String getText(Object element) {
		        if (element instanceof ElementModel) {
		            ElementModel myNode = (ElementModel) element;
		            return myNode.getName();
		        }
		        // Not called with the IGraphEntityContentProvider
		        if (element instanceof MyConnection) {
		            MyConnection myConnection = (MyConnection) element;
		            return myConnection.getLabel();
		        }

		        if (element instanceof EntityConnectionData) {
		            EntityConnectionData test = (EntityConnectionData) element;
		            return "";
		        }
		        throw new RuntimeException("Wrong type: "
		                + element.getClass().toString());
		    }
		
	

}

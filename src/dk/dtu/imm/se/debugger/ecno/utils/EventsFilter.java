package dk.dtu.imm.se.debugger.ecno.utils;

import java.util.List;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

import dk.dtu.imm.se.debugger.ecno.models.EventModel;

public class EventsFilter extends ViewerFilter {

	private List<EventModel> eventExceptions = null;
	public void setExceptions(List<EventModel> events) {
		this.eventExceptions = events;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		// TODO Auto-generated method stub
		if(element instanceof EventModel) {
			if(eventExceptions  == null)return false;
			for(Object o : eventExceptions) {
				if(o.equals(element))return true;
			}
			return false;
			
		}
		
		return true;
	}

}

//package dk.dtu.imm.se.debugger.ecno.views;
package notPartOfProject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;


public class SenderView extends ViewPart {
	
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.SenderView";
	private Button btnS;

	public SenderView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		parent.setLayout(new GridLayout());
		btnS = new Button(parent, SWT.PUSH);
		btnS.setText("Send Event");
		btnS.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				BundleContext ctx = FrameworkUtil.getBundle(SenderView.class).getBundleContext();
				 ServiceReference<EventAdmin> ref = ctx.getServiceReference(EventAdmin.class);
				 EventAdmin eventAdmin = ctx.getService(ref);
				 Map<String, Object> properties = new HashMap<String, Object> ();
				 properties.put("DATA", new Date());
				 
				 Event event = new Event("viewcommunication/syncEvent", properties);
	
				 eventAdmin.sendEvent(event);
				 event = new Event ("viewcommunication/syncEvent", properties);
				 eventAdmin.postEvent(event);
				 
			}
		});
		

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		btnS.setFocus();

	}

}

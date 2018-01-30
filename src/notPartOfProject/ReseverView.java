//package dk.dtu.imm.se.debugger.ecno.views;
package notPartOfProject;

import java.text.DateFormat;
import java.util.Dictionary;
import java.util.Hashtable;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;



public class ReseverView extends ViewPart {
	public static final String ID = "dk.dtu.imm.se.debugger.ecno.views.ReseverView";
	private TableViewer tViewer;

	public ReseverView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		parent.setLayout(new FillLayout());
		tViewer = new TableViewer(parent);
		tViewer.getTable().setHeaderVisible(true);
		tViewer.getTable().setLinesVisible(true);
		tViewer.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return DateFormat.getDateInstance().format(element);
			}
		});
		
		BundleContext ctx = FrameworkUtil.getBundle(ReseverView.class).getBundleContext();
		EventHandler handler = new EventHandler() {
			public void handleEvent(final Event event) {
				if(parent.getDisplay().getThread() == Thread.currentThread()) {
					tViewer.add(event.getProperty("DATA"));
				}else {
					parent.getDisplay().asyncExec(new Runnable() {
						public void run() {
							tViewer.add(event.getProperty("DATA"));
						}
					});
				}
				
			}
		};

		 Dictionary<String,String> properties = new Hashtable<String, String>();
		    properties.put(EventConstants.EVENT_TOPIC, "viewcommunication/*");
		    ctx.registerService(EventHandler.class, handler, properties);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
		tViewer.getTable().setFocus();

	}

}

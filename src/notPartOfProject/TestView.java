//package dk.dtu.imm.se.debugger.ecno.views;
package notPartOfProject;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import dk.dtu.imm.se.debugger.ecno.providers.MyTreeContentProvider;

public class TestView extends ViewPart {

	
	TreeViewer viewer;
	public TestView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
		Text t = new Text(parent, 0);
		t.setText("I am a test view");
		
		viewer = new TreeViewer(parent);
		viewer.setContentProvider(new MyTreeContentProvider());
		viewer.setLabelProvider(new MyTreeLableProvider());
		//viewer.setInput(listCompany);

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

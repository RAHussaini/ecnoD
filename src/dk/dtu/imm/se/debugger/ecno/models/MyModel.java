package dk.dtu.imm.se.debugger.ecno.models;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class MyModel extends ViewPart {
	
	public int counter;
	
	public MyModel(int counter) {
		this.counter = counter;
	}
	
	@Override
	public String toString() {
		
		System.out.println("Test ");
		return "item" + this.counter;
	}
	
	

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
	
			Label lbl = new Label(parent, counter);
			
			lbl.setText("Hasib test");
		
		
	}
	
	
	

}

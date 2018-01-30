package dk.dtu.imm.se.debugger.ecno.perspectives;

import org.eclipse.swt.widgets.Layout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	@Override
	public void createInitialLayout(IPageLayout layout) {
		// TODO Auto-generated method stub
		
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		layout.addStandaloneView("dk.dtu.imm.se.debugger.ecno.views.TestView", false, IPageLayout.LEFT, 1.0f, editorArea);
		
		//layout.addView(" dk.dtu.imm.se.debugger.ecno.views.TestView", IPageLayout.TOP, IPageLayout.RATIO_MAX, IPageLayout.ID_EDITOR_AREA);

	}

}

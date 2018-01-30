package dk.dtu.imm.se.debugger.ecno.perspectives;

import java.util.Random;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class ECNODebuggerApplicationWindow extends ApplicationWindow {
	






public ECNODebuggerApplicationWindow() {
	this(null);
}
	public ECNODebuggerApplicationWindow(Shell parentShell) {
		super(parentShell);
		addMenuBar();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addStatusLine();
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		ECNODebuggerApplicationWindow window = new ECNODebuggerApplicationWindow();
		window.setBlockOnOpen(true);
		window.open();
		Display.getCurrent().dispose();
	}
	
	protected void initializeBounds() {
		getShell().setSize(640,480);
		getShell().setLocation(0,0);
	}
	
	protected void configureShell(Shell shell) {
		
		super.configureShell(shell);
		shell.setText("ECNO Debugger Application");
		
	}
	
	protected MenuManager createMeuManager() {
		MenuManager menuManager = new MenuManager();
		MenuManager fileMenu = new MenuManager("&File");
		fileMenu.add(_changeColorAction);
		fileMenu.add(_exitAction);
		menuManager.add(fileMenu);
		
		return menuManager;
		}
	
	private ChangeColorAction _changeColorAction =new ChangeColorAction();
	private class ChangeColorAction extends Action {
	   private Color [] colors;;
	   public ChangeColorAction(){
	      Display d =Display.getDefault();
	      setImageDescriptor(redImageDesc);
	      setText("Change Color Alt+C");
	      setToolTipText("Change Color");
	      colors =new Color [ ] {
	      d.getSystemColor(SWT.COLOR_BLACK),
	      d.getSystemColor(SWT.COLOR_BLUE),
	      d.getSystemColor(SWT.COLOR_RED),
	      d.getSystemColor(SWT.COLOR_YELLOW),
	      d.getSystemColor(SWT.COLOR_GREEN)};
	   } 
	   public void run(){
	      Random generator =new Random();
	      int index =generator.nextInt(4);
	      Color color =colors [index ];
	      _composite.setBackground(color);
	      setStatus(color.toString());
	   }
	}
	
	private ExitAction _exitAction =new ExitAction(this);
	private class ExitAction extends Action {
	   ApplicationWindow _window;
	public ExitAction(ApplicationWindow window){
	   _window =window;
	   setText("Exit Ctrl+X");
	   setToolTipText("Exit Application");
	   setImageDescriptor(greenImageDesc);
	}
	   public void run(){
	      _window.close();
	   }
	}
	
	private Composite _composite;
	ImageDescriptor greenImageDesc =ImageDescriptor.createFromFile(
			ECNODebuggerApplicationWindow.class,
	      "green.gif"
	   );
	ImageDescriptor redImageDesc =ImageDescriptor.createFromFile(
			ECNODebuggerApplicationWindow.class,
	      "red.gif"
	   );
	
	
	
	protected ToolBarManager createToolBarManager(int style){
		   ToolBarManager toolBarManager =new ToolBarManager(style);
		   toolBarManager.add(_changeColorAction);
		   toolBarManager.add(_exitAction);
		   return toolBarManager;
		}
	
	protected Control createContents(Composite parent) {
		_composite = new Composite(parent, SWT.NONE);
		return _composite;}
	
	
}

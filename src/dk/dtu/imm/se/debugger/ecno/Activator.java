package dk.dtu.imm.se.debugger.ecno;

import org.eclipse.core.commands.Command;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.commands.ICommandService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.progress.UIJob;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "dk.dtu.imm.se.debugger.ecno"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		
		UIJob job = new UIJob("workaround") { // Create a new instance of the receiver with the supplied name. 
			//The display used will be the one from the workbench if this is available. UIJobs with this constructor will determine their display at runtime.
			 
		    public IStatus runInUIThread(IProgressMonitor monitor) { // run the job in the UI thread
		 
		        ICommandService commandService = (ICommandService) PlatformUI
		            .getWorkbench().getActiveWorkbenchWindow().getService(
		                ICommandService.class);
		       
		        Command command = commandService.getCommand("dk.dtu.imm.se.debugger.ecno.commands.ChangeLayout");
		        command.isEnabled();
		        return new Status(IStatus.OK,
		            PLUGIN_ID,
		            "Init commands workaround performed succesfully");
		    }
		 
		};
		job.schedule();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}

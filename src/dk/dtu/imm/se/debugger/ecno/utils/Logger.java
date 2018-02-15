package dk.dtu.imm.se.debugger.ecno.utils;

import java.io.IOException;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

public class Logger {
	private static java.util.logging.Logger logger;
	public Logger() {
		// This block configure the logger with handler and formatter 
		logger = java.util.logging.Logger.getLogger("dk.dtu.imm.se.debugger.ecno");
        FileHandler fh;
		try {
			Date d = new Date();
			fh = new FileHandler("logs/ecnoDebugger-" +
			d.getDay() + "." + d.getMonth() + "." + (d.getYear()-100)+ ".log");
			 logger.addHandler(fh);
		        SimpleFormatter formatter = new SimpleFormatter();  
		        fh.setFormatter(formatter);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	public static java.util.logging.Logger getLog(){
		if(logger == null) new Logger();
		return logger;
	}
	
	public void run(){
		logger.info("test");
		logger.warning("warning");
		logger.severe("severe");
	}
	public static void main(String[] args) {
		new Logger().run();
	}

}

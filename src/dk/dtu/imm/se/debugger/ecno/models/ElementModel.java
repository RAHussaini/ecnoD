package dk.dtu.imm.se.debugger.ecno.models;

import java.util.ArrayList;
import java.util.List;

public class ElementModel {
	
	
	 private final String id;
	    private final String name;
	    private List<ElementModel> connections;

	    public ElementModel(String id, String name) {
	        this.id = id;
	        this.name = name;
	        this.connections = new ArrayList<ElementModel>();
	    }

	    public String getId() {
	        return id;
	    }

	    public String getName() {
	        return name;
	    }

	    public List<ElementModel> getConnectedTo() {
	        return connections;
	    }
	

}

package dk.dtu.imm.se.debugger.ecno.models;

public class MyConnection {
	 final String id;
	    final String label;
	    final ElementModel source;
	    final ElementModel destination;

	    public MyConnection(String id, String label, ElementModel source, ElementModel destination) {
	        this.id = id;
	        this.label = label;
	        this.source = source;
	        this.destination = destination;
	    }

	    public String getLabel() {
	        return label;
	    }

	    public ElementModel getSource() {
	        return source;
	    }
	    public ElementModel getDestination() {
	        return destination;
	    }

}

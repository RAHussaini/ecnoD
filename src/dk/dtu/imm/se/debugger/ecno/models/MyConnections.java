package dk.dtu.imm.se.debugger.ecno.models;

public class MyConnections {
	 final String id;
	    final String label;
	    final MyNode source;
	    final MyNode destination;

	    public MyConnections(String id, String label, MyNode source, MyNode destination) {
	        this.id = id;
	        this.label = label;
	        this.source = source;
	        this.destination = destination;
	    }

	    public String getLabel() {
	        return label;
	    }

	    public MyNode getSource() {
	        return source;
	    }
	    public MyNode getDestination() {
	        return destination;
	    }

}

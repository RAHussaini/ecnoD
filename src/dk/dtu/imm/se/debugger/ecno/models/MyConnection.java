package dk.dtu.imm.se.debugger.ecno.models;

public class MyConnection {
	 final String id;
	    final String label;
	    final MyNode source;
	    final MyNode destination;

	    public MyConnection(String id, String label, MyNode source, MyNode destination) {
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

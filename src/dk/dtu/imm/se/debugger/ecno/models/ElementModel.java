package dk.dtu.imm.se.debugger.ecno.models;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import dk.dtu.imm.se.ecno.core.IElementType;

public class ElementModel extends ObjectModel {
	
	private static Hashtable<String, Integer> counter = new Hashtable<>();
	private final List<ObjectModel> references;
	private IElementType elementType;
	
	public ElementModel(Object element, IElementType elementType){
		this.references = new ArrayList();
		this.elementType = elementType;
		
		try {
			Method nameMethod = element.getClass().getMethod("getName");
			if(nameMethod != null)
				this.name = nameMethod.invoke(element).toString();
			
		}catch (Exception e){
			System.err.println("no getName method found for :" + element.getClass().getSimpleName());
			
		}
		if (this.name == null) {
			this.name = getDefaultElementName(element);
		}
		
		this.name += " : " + (elementType != null ? elementType.getName() : element.getClass().getSimpleName());
		this.node = element;
		
		
		
	}

private String getDefaultElementName(Object element) {
		// TODO Auto-generated method stub
	String name = element.getClass().getSimpleName();
	int no = 0;
	if(counter.contains(name)) {
		no = counter.get(name)+1;
	}
	counter.put(name, no);
		return name + no;
	}

//		public ElementModel(String string) {
//		// TODO Auto-generated constructor stub
//	}

		public Object[] getReferences() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <T> T getAdapter(Class<T> adapter) {
			// TODO Auto-generated method stub
			return null;
		}
	
		

}

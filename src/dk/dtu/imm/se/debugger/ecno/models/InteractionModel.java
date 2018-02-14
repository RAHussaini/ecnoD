package dk.dtu.imm.se.debugger.ecno.models;

import java.util.List;

import dk.dtu.imm.se.ecno.runtime.Interaction;

public class InteractionModel {
	
	private Interaction interaction;
	private List<ElementModel> elements;
	private List<EventModel> events;
	private ElementModel triggerElement;
	private EventModel triggerEvent;
	private String name;
	
	
	public InteractionModel(Interaction interaction, List<ElementModel> elements, List<EventModel> events,
			ElementModel triggerElement, EventModel triggerEvent, String name) {
		super();
		this.interaction = interaction;
		this.elements = elements;
		this.events = events;
		this.triggerElement = triggerElement;
		this.triggerEvent = triggerEvent;
		this.name = name;
	}


	public Interaction getInteraction() {
		return this.interaction;
	}


	public List<ElementModel> getElements() {
		return elements;
	}


	public List<EventModel> getEvents() {
		return events;
	}


	public ElementModel getTriggerElement() {
		return triggerElement;
	}


	public EventModel getTriggerEvent() {
		return triggerEvent;
	}


	public String getName() {
		return this.name;
	}
	
	
	
	
	

}

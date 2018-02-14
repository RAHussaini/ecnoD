package dk.dtu.imm.se.debugger.ecno.controllers;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.Assert;

import dk.dtu.imm.se.debugger.ecno.models.ElementModel;
import dk.dtu.imm.se.debugger.ecno.models.EventModel;
import dk.dtu.imm.se.debugger.ecno.models.InteractionModel;
import dk.dtu.imm.se.debugger.ecno.models.InvalidChoice;
import dk.dtu.imm.se.debugger.ecno.models.ObjectModel;
import dk.dtu.imm.se.debugger.ecno.utils.ElementHelper;
import dk.dtu.imm.se.ecno.core.IElementBehaviour;
import dk.dtu.imm.se.ecno.core.IElementType;
import dk.dtu.imm.se.ecno.core.IEventType;
import dk.dtu.imm.se.ecno.engine.ECNODebugger;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine;
import dk.dtu.imm.se.ecno.engine.ExecutionEngine.InvalidChoiceType;
import dk.dtu.imm.se.ecno.engine.IController;
import dk.dtu.imm.se.ecno.runtime.Event;
import dk.dtu.imm.se.ecno.runtime.IChoice;
import dk.dtu.imm.se.ecno.runtime.Interaction;
import dk.dtu.imm.se.ecno.runtime.InteractionIterator;
import dk.dtu.imm.se.ecno.runtime.InvalidStateException;
import dk.dtu.imm.se.ecno.runtime.Link;

public class ECNODebuggerEngineController implements IECNODebuggerEngineController,ECNODebugger, IController {

	private static ECNODebuggerEngineController instance;	

	//*********************************************
	private ExecutionEngine engine;
	private CountDownLatch latch = null;
	private DebuggerState state = DebuggerState.INITIALIZED; //  intial state
	private volatile List<IDebuggerStateListener> debuggerStateListeners = new ArrayList<>();
	
	private List<IBreakpoint> breakpoints = new ArrayList<>();
	private List<IGraphItemListener> elementListener = new ArrayList<>();
	private List<ObjectModel> encounteredElements = new ArrayList<>();
	private List <ObjectModel> addedElements = new ArrayList<>();
	private List<IBreakpointListener> breakpointListener = new ArrayList<>();
	private List<IInteractionListener> interactionListeners = new ArrayList<>();

	private volatile ArrayList<ObjectModel> elements = new ArrayList<>();

	private volatile List<IElementType> elementTypes = new ArrayList<>();

	private volatile List<IEventType> eventTypes = new ArrayList<>();

	private volatile List<InteractionModel> interactions = new ArrayList<>();

	private volatile List<IElementTypeListener> elementTypeListeners = new ArrayList<>();

	private volatile List<IEventTypeListener> eventTypeListeners = new ArrayList<>();
	private volatile List<EventModel> events = new ArrayList<>();
	
	//******************************************************
	
	public static IECNODebuggerEngineController getInstance() {
		// TODO Auto-generated method stub		
		if (instance == null) {
			System.out.println("new instance");
			return (instance = new ECNODebuggerEngineController());
		}
		return instance;
	}
	
	private ECNODebuggerEngineController() {
		
	}
	
	public void setEngine(ExecutionEngine engine) {
		if(this.engine != null) {			
			destroy();
			
		}
		this.engine = engine;
		this.engine.addController(this);
		this.engine.attachDebugger(this);
		changeState(DebuggerState.STARTED);
	}

	
	
	private void changeState(DebuggerState state) {
		// TODO Auto-generated method stub		
		this.state  = state;
		for (IDebuggerStateListener l : this.debuggerStateListeners) {
			l.stateChanged(state);
		}		
	}
	
	

	private void destroy() {
		// TODO Auto-generated method stub
		elements.clear();
		addedElements.clear();
		encounteredElements.clear();
		elementTypes.clear();
		eventTypes.clear();
		interactions.clear();
		this.engine.removeController(this);
		this.engine = null;
		notifyElementTypesChanged();
		notifyEventTypesChanged();
		changeState(DebuggerState.STOPPED);
		
		
	}

	private void notifyEventTypesChanged() {
		for(IEventTypeListener l : this.eventTypeListeners ) {
			l.eventTypesChanged(this.eventTypes.toArray(new IEventType[0]));
		}
		
		
	}

	private void notifyElementTypesChanged() {
		// TODO Auto-generated method stub
		for(IElementTypeListener l : this.elementTypeListeners ) {
			l.elementTypesChanged(this.elementTypes.toArray(new IElementType[0]));
		}
		
	}

	private Lock elementLock = new ReentrantLock();

	private volatile Hashtable<Object, ElementModel> elementLookupTable = new Hashtable<>();

	private Hashtable<Object, List<Object>> elementLinks = new Hashtable<>();

	
	
	private synchronized ElementModel generateElementNodes(Object element) {		
		elementLock.lock();
		
		if(element.toString().contains("Anton")) System.err.println("Anton node: " + element);
		boolean lookupTest = false;
		if(lookupTest = elementLookupTable.containsKey(element) ) {
			if(element.toString().contains("Anton"))System.err.println("element already exists");
			elementLock.unlock();
			return elementLookupTable.get(element);
		}
		ObjectModel test = elementLookupTable.get(element);
		if(element.toString().contains("Anton")){
			System.err.println("lookup: " + test + ";" + lookupTest);
		}

		List<IEventType> eventTypes = ElementHelper.getEventTypes(engine, element);

		addEventTypes(eventTypes);
		IElementType elementType = engine.getElementType(element);
		addElementTypes(elementType);

		ElementModel newElement = new ElementModel(element, engine.getElementType(element));
		elementLookupTable.put(element, newElement);
		if(element.toString().contains("Anton")) System.err.println("Anton's viewmodel has been created");
		//		List<Object> newElementReferences = ElementHelper.getReferences(engine, eventTypes, element);
		//		newElement.setReferences(newElementReferences, elements);
		elementLock.unlock();
		return newElement;
		
	}
	
	
	
	
	
	private void addElementTypes(IElementType... list) {
		// TODO Auto-generated method stub
		boolean isDirty = false;
		for(IElementType e : list){
			if(!this.elementTypes.contains(e) && e != null){
				this.elementTypes.add(e);
				isDirty = true;
			}
		}
		if(isDirty){
			notifyElementTypesChanged();
		}
	}

	private void addEventTypes(List<IEventType> list) {
		// TODO Auto-generated method stub
		boolean isDirty = false;
		for(IEventType e : list){
			if(!this.eventTypes.contains(e)){
				this.eventTypes.add(e);
				isDirty = true;
			}
		}
		if(isDirty){
			notifyEventTypesChanged();
		}
		
	}
	
	
	private void clearInteractionsAndRelatedItems(){
		for(InteractionModel interaction : this.interactions){
			for(EventModel event : interaction.getEvents()){
				this.elements.remove(event);
				for(IGraphItemListener l : this.elementListener){
					l.eventAdded(event);
				}
			}
		}
		for(EventModel event : this.events){
			this.elements.remove(event);
			for(IGraphItemListener l : this.elementListener) l.elementRemoved(event);
		}
		this.interactions.clear();
		this.events.clear();


	}
	
	
	
	private InteractionModel generateInteractionModel(Interaction interaction){
		List<ElementModel> elementModels = new ArrayList<>();
		List<EventModel> eventModels = new ArrayList<>();
		Hashtable<String, EventModel> eventLookup = new Hashtable<>();
		ElementModel triggerElementModel = generateElementNodes(interaction.getTriggerElement());
		EventModel triggerEventModel = new EventModel(interaction.getTriggerEvent());
		for(Object el : interaction.getElements()){
			elementModels.add(generateElementNodes(el));			
		}
		if(interaction.getLinks() != null){
			for(Link l : interaction.getLinks()){
				Event[] events = interaction.getEvents(l).toArray(new Event[0]);
				for(Event e : events){
					EventModel eventModel = eventLookup.get(e.getType().getName());
					if(eventModel == null) {
						eventModel = new EventModel(e);
						eventLookup.put(e.getType().getName(), eventModel);
						eventModels.add(eventModel);
					}
					ElementModel source = elementLookupTable.get(l.source);
					ElementModel target = elementLookupTable.get(l.target);
					if(!elementModels.contains(source)) elementModels.add(source);
					if(!elementModels.contains(target)) elementModels.add(target);

					eventModel.addReference(source);
					eventModel.addReference(target);

				}
			}
		}

		return new InteractionModel(interaction, elementModels, eventModels, triggerElementModel, triggerEventModel, interaction.getLabel());
	}
	
	
	

	public synchronized void calculateInteractions(){
		String intents1 = "\t";
		String intents2 = intents1 + "\t";
		String intents3 = intents2 + "\t";
		String intents4 = intents3 + "\t";
		long started = System.currentTimeMillis();
		System.err.println("calculating interactions");
		clearInteractionsAndRelatedItems();
		List<EventModel> eventViewModelsToAdd = new ArrayList<>();
		List<ObjectModel> copyElements = new ArrayList<>();
		copyElements.addAll(elements);
//		System.out.println("copied elements: " + copyElements.size());
		ElementHelper.resetNames();

		//		engine.getBehaviours().iterator().next().
		for(ObjectModel oViewModel : copyElements){
			Object element = oViewModel.getNode();
			Set<IEventType> eventTypesSet =  engine.getEventTypes(element);
//			System.out.println("element: " + element);
			if(eventTypesSet == null) continue;

			Iterator<IEventType> eventTypes = eventTypesSet.iterator();
			while(eventTypes.hasNext()){
				IEventType eventType = eventTypes.next();
				InteractionIterator interactionIterator = engine.getInteractions(element, eventType);
//				System.out.println(intents1 + "event type: " + eventType.getName());
				try {
					while(interactionIterator.hasNext()){
						Interaction interaction = interactionIterator.next();


						Collection<Link> linksSet = interaction.getLinks();

						List<EventModel> interactionEventModels = new ArrayList<>();
						List<ElementModel> interactionElementModels = new ArrayList<>();

//						System.out.println(intents2 + "interaction: " + interaction.getLabel());
						Collection<Object> interactionElements = interaction.getElements();


						if(interactionElements != null){
							for(Object o : interactionElements){
								ObjectModel ovm = elementLookupTable.get(o);

								//								for(ObjectViewModel ovm : this.elements){
								//									if(ovm.getNode().equals(o)){
								//										interactionElementViewModels.add((ElementViewModel) ovm);
								//
								//										break;
								//									}
								//								}
								if(ovm != null){
//									System.out.println(intents3 + "element: " + o);
									interactionElementModels.add((ElementModel) ovm);
								}else{
									System.err.println("element lookup failed");
								}

							}
						}
						if(linksSet != null){
							Iterator<Link> links = linksSet.iterator();
							while(links.hasNext()){
								Link link = links.next();

								if(!elementLinks .containsKey(link.source)){
									elementLinks.put(link.source, new ArrayList<>());									
								}
								if(!elementLinks.get(link.source).contains(link.target)){
									elementLinks.get(link.source).add(link.target);
								}

								Collection<Event> eventsSet = interaction.getEvents(link);
								if(eventsSet != null){
									Iterator<Event> events = eventsSet.iterator();
//									System.out.println(intents3 + "link: " + link.source + "-->" + link.target);
									while(events.hasNext()){
										Event event = events.next();

										EventModel eventViewModel = new EventModel(event);

										eventViewModel.addReference(elementLookupTable.get(link.source));
										eventViewModel.addReference(elementLookupTable.get(link.target));
										//										interaction.get
										//										engine.getev


										//										this.elements.add(eventViewModel);
										this.events.add(eventViewModel);
										eventViewModelsToAdd.add(eventViewModel);
										interactionEventModels.add(eventViewModel);
//										System.out.println(intents4 + "event: " + event);
									}
								}
							}
						}

						InteractionModel interactionViewModel = generateInteractionModel(interaction);
//								new InteractionViewModel(interaction, 
//										interactionElementViewModels, 
//										interactionEventViewModels,
//										generateElementNodes(interaction.getTriggerElement()),
//										interaction.getTriggerEvent(),
//										ElementHelper.getName(interaction));
						this.interactions.add(interactionViewModel);
					}
				} catch (InvalidStateException e) {
					System.err.println("invalid state exception: " + e.getMessage());
				}

			}
		}
		//		for(EventViewModel evm : eventViewModelsToAdd){
		//			this.elements.add(evm);
		//			for(GraphItemListener l : this.elementListener){
		//				l.eventAdded(evm);
		//			}
		//		}


		for(IInteractionListener l : this.interactionListeners){
			l.interactionsUpdated(Collections.unmodifiableList(interactions));
		}

		for(ObjectModel elem : this.elements){
			if(elem instanceof ElementModel){
				((ElementModel) elem).setReferences(elementLinks.get(elem.getNode()), this.elements);
			}
		}


		System.err.println("finished calculating: " + (System.currentTimeMillis()-started) + "ms");

	}
	
	
	
	
	

	@Override
	public void debug(Interaction interaction) {
		// TODO Auto-generated method stub
		// this method is the implementation of dk.dtu.imm.se.ecno.engine.ECNODebugger; interface
		try {
			System.err.println(" .... ");
			if(latch != null)
				latch.await();
			System.err.println("Contenue from the breakpoint ...");
			
		}catch(InterruptedException e) {
			
			System.err.println("Latch Error" + e.getMessage());
		}
		boolean isBreakpoint = false;
		for(IBreakpoint breakpoint : breakpoints){
			if(breakpoint.isBreakpoint(interaction)){
				isBreakpoint = true;
				break;
			}
		}

		if(isBreakpoint){
			if(breakpointListener != null){
				for(IBreakpointListener l : breakpointListener){

					l.breakpointHit(generateInteractionModel(interaction));
				}
			}

			latch = new CountDownLatch(1);
		}
		
		
	}
	
	
	
	
	
private Hashtable<ElementModel, InvalidChoice> invalidChoices = new Hashtable<>();


	@Override
	public void invalidChoice(IChoice choice, Interaction interaction, InvalidChoiceType type) {
		// TODO Auto-generated method stub
		// this method is the implementation of dk.dtu.imm.se.ecno.engine.ECNODebugger; interface
		
		Object element = choice.getOwner().getElement();
		ElementModel key = elementLookupTable.get(element);
		invalidChoices.put(key, new InvalidChoice(key, choice.getEventTypes(), type));
	}

	@Override
	public void addElement(Object element) {
		// TODO Auto-generated method stub
		ElementModel node = generateElementNodes(element);
		addedElements.add(node);
		elements.add(node);

		for(IGraphItemListener l : this.elementListener){
			l.elementAdded(node);
		}
		
	}

	@Override
	public void removeElement(Object element) {
		// TODO Auto-generated method stub
		ObjectModel toRemove = elementLookupTable.remove(element);
		if(toRemove == null) return;


		if(addedElements.remove(toRemove))
			System.out.println("added element removed: " + toRemove);
		if(encounteredElements.remove(toRemove))
			System.out.println("encountered element removed: " + toRemove);

		elements.remove(toRemove);
		
	}

	@Override
	public void elementEncountered(Object element) {
		// TODO Auto-generated method stub
		ElementModel node = generateElementNodes(element);
		encounteredElements.add(node);
		elements.add(node);
		for(IGraphItemListener l : elementListener){
			l.elementEncountered(node);
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		System.err.println("disposing engine listener");
		this.engine.removeController(this);
	}



	@Override
	public boolean isElementType(Object element, IElementType elementType) {
		// TODO Auto-generated method stub
		IElementType typeToMatch = engine.getElementType(element);
		if(typeToMatch == null) return false;
		if(elementType == null) return false;
		if(typeToMatch.getName().equals(elementType.getName())) return true;
		return false;
	}



	@Override
	public void addBreakpoint(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		this.breakpoints.add(breakpoint);
		
	}



	@Override
	public void continueFromBreakpoint() {
		// TODO Auto-generated method stub
		latch.countDown();
		
	}



	@Override
	public void addElementListener(IGraphItemListener listener) {
		// TODO Auto-generated method stub
		this.elementListener.add(listener);
		for (ObjectModel o: this.encounteredElements)listener.elementEncountered(o);
		for (ObjectModel o: this.addedElements) listener.elementAdded(o);
		
	}



	@Override
	public void addDebuggerStateListener(IDebuggerStateListener listener) {
		// TODO Auto-generated method stub
		this.debuggerStateListeners.add(listener);
		listener.stateChanged(state);
		
		
	}



	@Override
	public void addBreakpointListener(IBreakpointListener listener) {
		// TODO Auto-generated method stub
		Assert.isNotNull(listener);
		breakpointListener .add(listener);
		
	}



	@Override
	public void addInteractionListener(IInteractionListener listener) {
		// TODO Auto-generated method stub
		this.interactionListeners.add(listener);
		listener.interactionsUpdated(interactions);
		
	}



	@Override
	public void removeElementListener(IGraphItemListener listener) {
		// TODO Auto-generated method stub
		this.elementListener.remove(listener);
		
	}



	@Override
	public void removeDebuggerStateListener(IDebuggerStateListener listener) {
		// TODO Auto-generated method stub
		this.debuggerStateListeners.add(listener);
		
	}

//	@Override
//	public void calculateInteractions() {
//		// TODO Auto-generated method stub
//		
//	}

	@Override
	public void addEventTypeListener(IEventTypeListener listener) {
		// TODO Auto-generated method stub
		Assert.isNotNull(listener, "IEventTypeListener in " + this.getClass().getName() + " should not be null");
		this.eventTypeListeners.add(listener);
		if(!this.eventTypes.isEmpty()){
			listener.eventTypesChanged(eventTypes.toArray(new IEventType[0]));
		}
		
	}

	@Override
	public void addElementTypeListener(IElementTypeListener listener) {
		// TODO Auto-generated method stub
		Assert.isNotNull(listener, "IElementTypeListener in " + this.getClass().getName() + " should not be null");
		this.elementTypeListeners.add(listener);
		if(!this.elementTypes.isEmpty()){
			listener.elementTypesChanged(elementTypes.toArray(new IElementType[0]));
		}
		
	}

	@Override
	public void removeBreakpoint(IBreakpoint breakpoint) {
		// TODO Auto-generated method stub
		this.breakpoints.remove(breakpoint);
		
	}

	@Override
	public void removeEventTypeListener(IEventTypeListener listener) {
		// TODO Auto-generated method stub
		this.eventTypeListeners.remove(listener);
	}

	@Override
	public void removeElementTypeListener(IElementTypeListener listener) {
		// TODO Auto-generated method stub
		this.elementTypeListeners.remove(listener);
	}

	@Override
	public void removeInteractionListener(IInteractionListener listener) {
		// TODO Auto-generated method stub
		this.interactionListeners.remove(listener);
	}

	@Override
	public void removeBreakpointListener(IBreakpointListener listener) {
		// TODO Auto-generated method stub
		this.elementListener.remove(listener);
	}

	@Override
	public List<IEventType> getEventTypes() {
		// TODO Auto-generated method stub
		List<IEventType> eventTypes = new ArrayList<>();
		for(IElementType e : getElementTypes()){
			Set<IEventType> types = this.engine.getEventTypes(e);
			Iterator<IEventType> iterator = types.iterator();
			while(iterator.hasNext()){
				eventTypes.add(iterator.next());
			}
		}
		return eventTypes;


	}

	@Override
	public IEventType[] getEventTypes(Object element) {
		// TODO Auto-generated method stub
		IElementType elType = engine.getElementType(element);
		Set<IEventType> set = engine.getEventTypes(elType);
		return set.toArray(new IEventType[0]);
	}

	@Override
	public List<IElementType> getElementTypes() {
		// TODO Auto-generated method stub
		Collection<IElementBehaviour> behaviours = engine.getBehaviours();
		Iterator<IElementBehaviour> iterator = behaviours.iterator();
		List<IElementType> items = new ArrayList<>();
		while(iterator.hasNext()){
			IElementType type = iterator.next().getElementType();
			items.add(type);
		}
		return items;
	}

	@Override
	public List<ObjectModel> getElements() {
		// TODO Auto-generated method stub
		return  Collections.unmodifiableList(this.elements);
	}

	@Override
	public List<Interaction> getInteractions(Object element, IEventType eventType) {
		// TODO Auto-generated method stub
		
		List<Interaction> list = new ArrayList<>();
		InteractionIterator iterator = engine.getInteractions(element, eventType);
		try {
		while(iterator.hasNext())list.add(iterator.next());
			} catch (InvalidStateException e) {
				System.err.println("invalid state exception");
			}

		return 	iterator.getDebugInteractions();


	}

}

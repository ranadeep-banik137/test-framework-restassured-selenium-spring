package com.app.testautomation.pojos;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.app.testautomation.exceptions.MultipleValuesReturnedException;

@Component(value = "locations")
public class Locations {
	
	private List<State> states;

	public List<State> getStates() {
		return states;
	}

	public void setStates(List<State> states) {
		this.states = states;
	}
	
	public void addState(State state) {
		setStates(getStates() == null ? new ArrayList<>() : getStates());
		this.states.add(state);
	}
	
	public void addState(String state) {
		setStates(getStates() == null ? new ArrayList<>() : getStates());
		State stateInstance = new State();
		stateInstance.setStateName(state);
		this.states.add(stateInstance);
	}

	public State getState(String state) {
		State stateInstance = null;
		Iterator<State> stateIterator = getStates().iterator();
		while (stateIterator.hasNext()) {
			stateInstance = stateIterator.next();
			if (stateInstance.getStateName().equals(state)) {
				break;
			}
		}
		return stateInstance;
	}
	
	public State getState() throws MultipleValuesReturnedException {
		if (getStates().size() > 1) {
			throw new MultipleValuesReturnedException();
		}
		return getStates().get(0);
	}

}

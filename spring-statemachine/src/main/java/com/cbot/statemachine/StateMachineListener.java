package com.cbot.statemachine;

import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateContext.Stage;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StateMachineListener extends StateMachineListenerAdapter<States, Events>{
	@Override
	public void stateContext(StateContext<States, Events> stateContext) {
		if(stateContext.getStage()==Stage.TRANSITION_START) {
			log.info("State start: {}, {}",stateContext.getStateMachine().getId(), stateContext.getTarget().getId());
			if(stateContext.getMessage()!=null) {
				for(var header: stateContext.getMessage().getHeaders().entrySet()) {
					log.info("Header: {}, {}", header.getKey(), header.getValue());
				}
			}
		}
	}
}

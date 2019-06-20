package com.cbot.statemachine;

import java.util.EnumSet;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.config.StateMachineBuilder.Builder;
import org.springframework.stereotype.Component;

@Component
public class MyApp implements ApplicationRunner {

	void doSignals() throws Exception {
		StateMachine<States, Events> stateMachine = buildMachine();
		StateMachineListener listener = new StateMachineListener();
		stateMachine.addStateListener(listener);
		stateMachine.start();
		//event not accepted 
		stateMachine.sendEvent(Events.EVENT3);
		//start machine and state 1
		stateMachine.sendEvent(Events.EVENT1);
		//start state 2
		stateMachine.sendEvent(Events.EVENT2);
		//start start 3 and end
		stateMachine.sendEvent(Events.EVENT3);
	}

	public void run(ApplicationArguments args) throws Exception {
		doSignals();
	}

	public StateMachine<States, Events> buildMachine() throws Exception {
		Builder<States, Events> builder = StateMachineBuilder.builder();
		builder.configureConfiguration().withConfiguration()
		.machineId(UUID.randomUUID().toString());
		builder.configureStates().withStates().initial(States.STATE1).end(States.END).states(EnumSet.allOf(States.class));
		builder.configureTransitions()
				.withExternal().source(States.STATE1).target(States.STATE2).event(Events.EVENT1).and()
				.withExternal().source(States.STATE2).target(States.STATE3).event(Events.EVENT2).and()
				.withExternal().source(States.STATE3).target(States.END).event(Events.EVENT3);
		return builder.build();
	}

}

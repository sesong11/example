package com.cbot.statemachine.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.data.RepositoryState;
import org.springframework.statemachine.data.RepositoryStateMachineModelFactory;
import org.springframework.statemachine.data.RepositoryTransition;
import org.springframework.statemachine.data.StateRepository;
import org.springframework.statemachine.data.TransitionRepository;
import org.springframework.statemachine.data.support.StateMachineJackson2RepositoryPopulatorFactoryBean;

@Configuration
public class StateMachineConfig {

	@Bean
	public StateMachineJackson2RepositoryPopulatorFactoryBean jackson2RepositoryPopulatorFactoryBean() {
		StateMachineJackson2RepositoryPopulatorFactoryBean factoryBean = new StateMachineJackson2RepositoryPopulatorFactoryBean();
		factoryBean.setResources(new Resource[]{new ClassPathResource("data.json")});
		return factoryBean;
	}

	@Configuration
	@EnableStateMachineFactory
	public static class Config extends StateMachineConfigurerAdapter<String, String> {

		@Autowired
		private StateRepository<? extends RepositoryState> stateRepository;

		@Autowired
		private TransitionRepository<? extends RepositoryTransition> transitionRepository;

		@Override
		public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
			model
				.withModel()
					.factory(modelFactory());
		}

		@Bean
		public StateMachineModelFactory<String, String> modelFactory() {
			return new RepositoryStateMachineModelFactory(stateRepository, transitionRepository);
		}
	}
}

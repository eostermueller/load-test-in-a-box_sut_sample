package com.github.eostermueller.tjp2.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.github.eostermueller.snail4j.workload.AliasManager;
import com.github.eostermueller.snail4j.workload.DefaultFactory;

//Hmmmm how is this app startup approach different?
//public class SpringBootSnail4J implements ApplicationListener<ApplicationReadyEvent> {

import com.github.eostermueller.snail4j.workload.Snail4jWorkloadException;


@Component
public class StartupHousekeeper {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
  @EventListener(ContextRefreshedEvent.class)
  public void contextRefreshedEvent() throws Snail4jWorkloadException {
	  AliasManager aliasManager = DefaultFactory.getFactory().getAliasManager();
	  int count = aliasManager.load();
	  LOGGER.info( String.format("loaded [%d] aliases", count));
	  
	  
  }
}
package com.github.eostermueller.tjp2.rest;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StartupHousekeeper {

  @EventListener(ContextRefreshedEvent.class)
  public void contextRefreshedEvent() {
     
  }
}
package com.github.eostermueller.tjp2.dataaccess;

import org.springframework.context.ApplicationContext;

import com.github.eostermueller.tjp2.AppContext;

public interface BaseManager {
	String APP_CONTEXT = "appContext";
	AppContext getAppContext();
}

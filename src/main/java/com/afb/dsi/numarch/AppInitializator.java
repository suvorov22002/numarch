package com.afb.dsi.numarch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.afb.dsi.numarch.controllers.PropertiesController;
import com.afb.dsi.numarch.entities.Params;
import com.afb.dsi.numarch.services.IParamsService;
import com.afb.dsi.numarch.tools.AutoWorker;

@Component
public class AppInitializator {
	
	@Autowired
	IParamsService paramservice;
	
	private List<Params> parameter;
	public static Map<String, Params> params = new HashMap<String, Params>(); 
	public static final Logger logger = LoggerFactory.getLogger(PropertiesController.class);
	
	@PostConstruct
	public void loadConfig() {

		logger.info("******************** Initialisation des parametres ********************");
		parameter = paramservice.getAllParams();
		for(Params p : parameter) {
			logger.info(p.getCode()+": "+p.getValeur());
			params.put(p.getCode(), p);
		}
		
		if (params.containsKey("ROBOT")) {
			
			if ("ON".equals(params.get("ROBOT").getValeur())) {
				logger.info("Lancement Robot");
				AutoWorker.initChecking();
			}
			else {
				AutoWorker.cancelChecking();
			}
			
		}
		else {
			AutoWorker.cancelChecking();
		}
		
	}
}

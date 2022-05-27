package com.piesat.busiclogic.busic.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class ImportWeaConfig {

	private String id;
	
	private String weaElement;
	
	private String weaElementName;
	
	private String levelId;
	
	private String macthMon;
	
	private boolean enabled;
	
	private String imagePath;
	
	private List<WeaDiscernConfig> weaDiscernConfigs;
	
	private HashMap<String, String> params;
	
	private String targetInterface;
	
	private InterfaceType interfaceType;
	
}

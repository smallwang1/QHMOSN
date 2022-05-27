package com.piesat.busiclogic.busic.supports;

import lombok.Data;

@Data
public class ValueConfig {

	private String key;
	
	private JsonLikeType type;
	
	private Class collectionClass;
	
	private Class elementClass;
	
	private Class mapClass;
	
	private Class keyClass;
	
	private Class valueClass;
}

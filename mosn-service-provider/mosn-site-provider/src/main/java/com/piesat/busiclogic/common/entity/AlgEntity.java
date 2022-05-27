package com.piesat.busiclogic.common.entity;

import java.util.List;

import lombok.Data;

@Data
public class AlgEntity {

	private String id;
	
	private String name;
	
	private String  description;
	
	private List<ElementEntity> elements;
}

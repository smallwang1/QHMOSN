package com.piesat.busiclogic.common.entity;

import java.util.List;

import lombok.Data;

@Data
public class TabEntity {

	private String id;
	
	private String name;
	
	private String tableName;
	
	private List<AlgEntity> algs;
}

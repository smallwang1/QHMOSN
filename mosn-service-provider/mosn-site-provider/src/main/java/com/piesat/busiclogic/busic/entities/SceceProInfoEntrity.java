package com.piesat.busiclogic.busic.entities;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SceceProInfoEntrity {

	private String proId;
	
	private String proName;
	
	private String proColName;
	
	private String proTableName;
	
	private String dataSourceName;
	
	private BigDecimal maxValue;
	
	private BigDecimal minValue;
	
	private List<BigDecimal> excludeValue;
	
	private String aggType;
	
	private int beginTimeOffset;
	
	private int endTimeOffset;
	
	private String timeCols;
	
}

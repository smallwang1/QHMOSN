package com.piesat.busiclogic.busic.entities;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class WeaDiscernConfig {

	private String id;

	private String impWeaId;

	private String targetElement;

	private Integer beginTimeOffset;

	private Integer endTimeOffset;

	private BigDecimal minValue;

	private BigDecimal maxValue;


	private MashupType mashupType;

}

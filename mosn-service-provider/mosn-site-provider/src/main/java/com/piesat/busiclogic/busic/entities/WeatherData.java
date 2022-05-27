package com.piesat.busiclogic.busic.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeatherData {

    private List halfDayData;

    private List<Weather> hourData = new ArrayList<>();
}

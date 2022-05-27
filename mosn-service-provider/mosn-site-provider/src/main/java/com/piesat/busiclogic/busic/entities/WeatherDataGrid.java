package com.piesat.busiclogic.busic.entities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class WeatherDataGrid {

    private List<DayWeather> halfDayData = new ArrayList<>();

    private List<Weather> hourData = new ArrayList<>();
}

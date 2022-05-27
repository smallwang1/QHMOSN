package com.piesat.busiclogic.busic.entities;

import lombok.Data;

@Data
public class DayWeather {

    private String time;

    private String dayOfWeek;

    private String wep_u;

    private String wep_d;

    private String win_d_u;

    private String win_d_d;

    private String win_level_u;

    private String win_level_d;

    private String win_s_u;

    private String win_s_d;

    private String tem_u;

    private String tem_d;

    private String pre_sum;

}

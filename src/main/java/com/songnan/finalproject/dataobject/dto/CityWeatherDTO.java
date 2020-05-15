package com.songnan.finalproject.dataobject.dto;

import lombok.Data;

@Data
public class CityWeatherDTO {
    private String ymd;
    private String high;
    private String low;
    private String type;
    private String notice;
}

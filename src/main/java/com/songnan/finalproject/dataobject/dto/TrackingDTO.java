package com.songnan.finalproject.dataobject.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class TrackingDTO {
    public static final Map<String,String> TRACKING_CODE_MAP = new HashMap<>();
    static{
        TRACKING_CODE_MAP.put("中通","zhongtong");
        TRACKING_CODE_MAP.put("申通","shentong");
        TRACKING_CODE_MAP.put("顺丰","shunfeng");
        TRACKING_CODE_MAP.put("圆通","yuantong");
        TRACKING_CODE_MAP.put("韵达","yunda");
        TRACKING_CODE_MAP.put("宅急送","zhaijisong");
        TRACKING_CODE_MAP.put("天天","tiantian");
        TRACKING_CODE_MAP.put("德邦","debangwuliu");
        TRACKING_CODE_MAP.put("ems","ems");
    }


    private String time;
    private String fTime;
    private String context;
    private String location;

}

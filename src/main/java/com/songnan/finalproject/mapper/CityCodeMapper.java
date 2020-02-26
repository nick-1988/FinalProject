package com.walking.meeting.Mapper;

import com.walking.meeting.dataobject.dao.CityCode;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CityCodeMapper extends tk.mybatis.mapper.common.Mapper<CityCode> {
    @Select({
            "select cc.city_code",
            "from city_code cc",
            "where city_name= #{cityName}"
    })
    @Results({
            @Result(column = "city_code", property = "Integer"),
    })
    Integer getCodeByName(@Param("cityName") String cityName);
}

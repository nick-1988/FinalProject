package com.walking.meeting.Service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.walking.meeting.Service.WeatherService;
import com.walking.meeting.Mapper.CityCodeMapper;
import com.walking.meeting.dataobject.dto.CityWeatherDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.walking.meeting.utils.HttpUtils.httpRequest;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private CityCodeMapper cityCodeMapper;

    @Override
    public String getWeather(String city) {
        // 先根据city来取出cityNumber

        Integer cityNumber = cityCodeMapper.getCodeByName(city);
        if (cityNumber == null) {
            return "所在城市暂不提供天气服务支持哦";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("http://t.weather.sojson.com/api/weather/city/");
        stringBuffer.append(cityNumber);
        JSONObject json = httpRequest(stringBuffer.toString(), "GET", null);
        JSONArray daysData = json.getJSONObject("data").getJSONArray("forecast");
        List<CityWeatherDTO> cityWeatherDTOList = new ArrayList<>();
        for (int i = 0; i < daysData.size(); i++) {
            JSONObject jsonObject = daysData.getJSONObject(i);
            CityWeatherDTO cityWeatherDTO = new CityWeatherDTO();
            cityWeatherDTO.setHigh(jsonObject.getString("high"));
            cityWeatherDTO.setLow(jsonObject.getString("low"));
            cityWeatherDTO.setNotice(jsonObject.getString("notice"));
            cityWeatherDTO.setType(jsonObject.getString("type"));
            cityWeatherDTO.setYmd(jsonObject.getString("ymd"));
            cityWeatherDTOList.add(cityWeatherDTO);
        }
        StringBuffer result = new StringBuffer();
        result.append(city + "所在地区的天气如下:");
        cityWeatherDTOList.forEach(ele->{
            result.append(ele.getYmd()).append("\t" + ele.getType()).append("\t最" + ele.getLow()).append("\t最" + ele.getHigh())
                    .append("\r" + ele.getNotice()+"\n");
        });

        return result.toString();
    }

//    public static void main(String[] args) {
//        String path = "/Users/walking/Downloads/sss";
//        File file = new File(path);
//        StringBuilder result = new StringBuilder();
//        try{
//            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));//构造一个BufferedReader类来读取文件
//            String s = null;
//            while((s = br.readLine())!=null){//使用readLine方法，一次读一行
//                s = s.replace("=", ",\"");
//                result.append(System.lineSeparator() +"("+ s + "\"),");
//            }
//            br.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//        System.out.println(result.toString());
//    }


}

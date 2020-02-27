package com.walking.meeting.Service.impl;

import static com.walking.meeting.utils.HttpUtils.httpRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.walking.meeting.Service.TrackingService;
import com.walking.meeting.dataobject.dto.TrackingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.walking.meeting.Mapper.CityCodeMapper;
import com.walking.meeting.Service.WeatherService;
import com.walking.meeting.dataobject.dto.CityWeatherDTO;

@Service
public class TrackingServiceImpl implements TrackingService {


    @Override
    public String getTracking(String company,String trackingNumber){
        // 先根据company来取出bianma
        String companyCode = TrackingDTO.TRACKING_CODE_MAP.get(company);
        if (companyCode == null) {
            return "该快递公司暂不提供查询服务哦";
        }
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("http://tool.bitefu.net/kuaidi/?sn=");
        stringBuffer.append(trackingNumber).append("&code=").append(companyCode);
        JSONObject json = httpRequest(stringBuffer.toString(), "GET", null);
        JSONArray daysData = json.getJSONArray("data");
        List<TrackingDTO> trackingDTOList = new ArrayList<>();
        for (int i = 0; i < daysData.size(); i++) {
            JSONObject jsonObject = daysData.getJSONObject(i);
            TrackingDTO trackingDTO = new TrackingDTO();
            trackingDTO.setTime(jsonObject.getString("time"));
            trackingDTO.setFTime(jsonObject.getString("ftime"));
            trackingDTO.setContext(jsonObject.getString("context"));
            trackingDTO.setLocation(jsonObject.getString("location"));
            trackingDTOList.add(trackingDTO);
        }
        StringBuffer result = new StringBuffer();
        trackingDTOList.forEach(ele->{
            result.append(ele.getTime()).append("\t" + ele.getFTime()).append("\t" + ele.getContext())
                    .append("\t" + ele.getLocation()+"\n");
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

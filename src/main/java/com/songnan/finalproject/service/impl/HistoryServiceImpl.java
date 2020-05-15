package com.songnan.finalproject.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.songnan.finalproject.service.HistoryService;
import com.songnan.finalproject.dataobject.dto.HistoryDTO;
import com.songnan.finalproject.dataobject.dto.TrackingDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.songnan.finalproject.utils.HttpUtils.httpRequest;

@Service
public class HistoryServiceImpl implements HistoryService {

    @Override
    public String getHistory(String month, String day) {
        StringBuffer sb = new StringBuffer();
        sb.append("http://www.jiahengfei.cn:33550/port/history?dispose=easy&key=jiahengfei&month=");
        sb.append(month).append("&day=").append(day);
        JSONObject json = httpRequest(sb.toString(), "GET", null);
        JSONArray daysData = json.getJSONArray("data");
        List<HistoryDTO> historyDTOList = new ArrayList<>();
        for (int i = 0; i < daysData.size(); i++) {
            JSONObject jsonObject = daysData.getJSONObject(i);
            HistoryDTO historyDTO = new HistoryDTO();
            historyDTO.setYear(jsonObject.getString("year"));
            historyDTO.setLunar(jsonObject.getString("lunar"));
            historyDTO.setPic(jsonObject.getString("pic"));
            historyDTO.setTitle(jsonObject.getString("title"));
            historyDTOList.add(historyDTO);
        }
        StringBuffer result = new StringBuffer();
        historyDTOList.forEach(ele->{
            result.append(ele.getYear());
            if (ele.getLunar() != null) {
                result.append("(阴历 ").append(ele.getLunar()).append(")");
            }
            result.append(ele.getTitle());
            if (ele.getPic() != null) {
                result.append("\t详细图片可查看 ").append(ele.getPic());
            }
            result.append("\n");
        });
        return result.toString();
    }
}

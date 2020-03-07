package com.walking.meeting.Service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.walking.meeting.Service.TrackingService;
import com.walking.meeting.Service.WXService;
import com.walking.meeting.Service.WeatherService;
import com.walking.meeting.dataobject.entity.TextMessage;
import com.walking.meeting.utils.MessageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class WXServiceImpl implements WXService {

    @Autowired
    WeatherService weatherService;
    @Autowired
    TrackingService trackingService;

    @Override
    public String newMessageRequest(HttpServletRequest request) {
        String respMessage = null;
        try {
            // xml请求解析
            Map<String, String> requestMap = MessageUtils.xmlToMap(request);
            // 发送方帐号（open_id）
            String fromUserName = requestMap.get("FromUserName");
            // 公众帐号
            String toUserName = requestMap.get("ToUserName");
            // 消息类型
            String msgType = requestMap.get("MsgType");
            // 消息内容
            String content = requestMap.get("Content");

            String resultContent = "";
            // 文本消息
            if (msgType.equals(MessageUtils.REQ_MESSAGE_TYPE_TEXT)) {
                if (content.contains("天气")) {
                    List<String> stringList =Arrays.asList(content.split("天气"));
                    String city = stringList.get(0);
                    resultContent = weatherService.getWeather(city);
                } else if (content.contains("快递")) {
                    List<String> stringList =Arrays.asList(content.split("快递"));
                    String trackingCompany = stringList.get(0);
                    String trackingNumber = stringList.get(1);
                    resultContent = TRACKING_STRING(trackingCompany,trackingNumber);
                } else if (content.contains("音乐")) {
                    List<String> stringList =Arrays.asList(content.split("音乐"));
                    String music = stringList.get(1);
                    // 需要对音乐名字进行url加密
                    String encodeMusic = URLEncoder.encode(music, "UTF-8");
                    resultContent = WXServiceImpl.MUSIC_STRING(music, encodeMusic);
                } else if (content.contains("电影")) {
                    List<String> stringList =Arrays.asList(content.split("电影"));
                    String movie = stringList.get(1);
                    // 需要对电影名字进行url加密
                    String encodeMovie = URLEncoder.encode(movie, "UTF-8");
                    resultContent = WXServiceImpl.MOVIE_STRING(movie, encodeMovie);
                } else if (StringUtils.equals(content,"历史上的今天")) {
                    resultContent = HISTORY_STRING();
                }else {
                    resultContent = WXServiceImpl.MENU_STRING();
                }
                TextMessage text = new TextMessage();
                text.setContent(resultContent);
                text.setToUserName(fromUserName);
                text.setFromUserName(toUserName);
                text.setCreateTime(System.currentTimeMillis());
                text.setMsgType(msgType);
                respMessage = MessageUtils.textMessageToXml(text);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respMessage;
    }

    public static String MENU_STRING() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("欢迎关注我的微信公众号\n");
        stringBuffer.append("回复如下关键字，可获取不一样的信息\n");
        stringBuffer.append("1.城市+天气 例如杭州天气,可获取当天杭州天气\n");
        stringBuffer.append("2.快递公司名+快递+单号 例如中通快递123,可查询该快递状态\n");
        stringBuffer.append("3.音乐+名字 例如七里香,可获取该歌曲信息\n");
        stringBuffer.append("4.电影+名字 例如泰坦尼克号，可获取该电影信息");
        return stringBuffer.toString();
    }

    public String TRACKING_STRING(String trackingCompany,String trackingNumber) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("您查询的"+trackingCompany+"快递单号是\n");
        stringBuffer.append(trackingService.getTracking(trackingCompany,trackingNumber));
        stringBuffer.append("为您查到如下信息:");
        return stringBuffer.toString();
    }

    public static String MUSIC_STRING(String music, String encodeMusic) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("您查询的音乐是\t");
        stringBuffer.append(music + "\n");
        stringBuffer.append("由于版权原因无法提供歌曲，为您查到qq音乐中的如下信息:\n");
        stringBuffer.append("https://y.qq.com/portal/search.html#page=1&searchid=1&remoteplace=txt.yqq.top&t=song&w=" + encodeMusic);
        return stringBuffer.toString();
    }

    public static String MOVIE_STRING(String movie, String encodeMovie) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("您查询的电影是\t");
        stringBuffer.append(movie + "\n");
        stringBuffer.append("为您查到如下信息:");
        stringBuffer.append("https://search.douban.com/movie/subject_search?search_text=" + encodeMovie + "&cat=1002");
        return stringBuffer.toString();
    }
    public String HISTORY_STRING() {
        Date date = new Date(System.currentTimeMillis());
        String time = DateUtils.formatDate(date, HISTORY_DATE);
        time.split(",");
        List<String> list = Arrays.asList(time.split(","));
        String month = list.get(0);
        String day = list.get(1);
        return historyService.getHistory(month, day);
    }

    public static void main(String[] args) {
        Date date = new Date(System.currentTimeMillis());
        ArrayList;
        String time = DateUtils.formatDate(date, HISTORY_DATE);
        List<String> list = Arrays.asList(time.split(","));
        System.out.println(JSON.toJSONString(list));
    }
}

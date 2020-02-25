package com.walking.meeting.Service;


import javax.servlet.http.HttpServletRequest;

public interface WXService {
    /**
     * 微信公众号处理(关注微信公众号，推送对应的消息)
     * @param request
     * @return
     */
    String newMessageRequest(HttpServletRequest request);

}

package com.walking.meeting.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.walking.meeting.Service.WXService;
import com.walking.meeting.utils.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Api(tags = "WXController", description = "微信模块")
@RestController("WXController")
@RequestMapping("/wx")
public class WXController {

    @Autowired
    private WXService wxService;

    @ApiOperation(value = "关注发消息", notes = "关注发消息")
    @PostMapping()
    public void test(HttpServletRequest request, HttpServletResponse response){
        log.info("发消息了兄弟");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        response.setContentType("text/html;charset=UTF-8");
        // 调用核心业务类接收消息、处理消息
        String respMessage = wxService.newMessageRequest(request);
        // 响应消息
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(respMessage);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage(),e);
        } finally {
            out.close();
            out = null;
        }
    }

}

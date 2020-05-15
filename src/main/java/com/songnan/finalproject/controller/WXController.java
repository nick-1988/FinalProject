package com.songnan.finalproject.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.songnan.finalproject.service.WXService;
import com.songnan.finalproject.utils.SignUtils;
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

    /**
     * 微信公众平台服务器配置验证
     * @param request
     * @param response
     */
    @GetMapping
    @ApiOperation("微信公众平台服务器配置验证")
    public void validate(HttpServletRequest request, HttpServletResponse response) {
        log.info("正在通过微信公众平台服务器验证");
        // 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = null;
        try {
            out = response.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，否则接入失败
            if (SignUtils.checkSignature(signature, timestamp, nonce)) {
                out.print(echostr);
            }
            log.info("已通过验证");
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());

        } finally {
            out.close();
            out = null;
        }
    }
}

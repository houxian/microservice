package com.imooc.user.controller;

import com.imooc.thrift.user.UserInfo;
import com.imooc.user.dto.UserDTO;
import com.imooc.user.redis.RedisUtil;
import com.imooc.user.response.LonginResponse;
import com.imooc.user.response.Response;
import com.imooc.user.thrift.ServiceProvider;;
import org.apache.commons.lang.StringUtils;
import org.apache.thrift.TException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private ServiceProvider serviceProvider;

    @Autowired
    private RedisUtil redisUtil;

    @RequestMapping(value="/login",method = RequestMethod.GET)
    public  String login(){

        return "login";

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Response login(@RequestParam("username")String username,
                      @RequestParam("password")String password) {

        UserInfo userInfo = null;
        // 1.验证用户密码
        try {
             userInfo = serviceProvider.getUserService().getUserByName(username);
        }catch (TException e){
            e.printStackTrace();

            return new Response("1001","user or password error");
        }
        if(userInfo ==null){
            return new Response("1001","user or password error");
        }
        if(!userInfo.getPassword().equals(password)){
            return new Response("1001","user or password error");
        }
        // 2. 生成token
        String token = getToken();
        //  3. 缓存用户
        redisUtil.set(token,toDTO(userInfo),3600);
        return  new LonginResponse(token);
    }

    @RequestMapping(value = "/sendverifycode", method = RequestMethod.POST)
    @ResponseBody
    public Response sendVerifyCode(@RequestParam(value = "mobile",required =false) String mobile,
                             @RequestParam(value = "email",required =false) String email) {

        String message = " Verify code is :";
        String code = randomCode("123456",6);
        boolean result = false;
        if(!StringUtils.isBlank(mobile)){
            try {
                result = serviceProvider.getMessageService().sendMobileMessage(mobile,message+code);
                redisUtil.set(mobile,code);
            }catch (Exception ex){
                ex.printStackTrace();
                return  new Response("1002","mobile and email 为Null");
            }

        } else if(!StringUtils.isBlank(email)){
            try {
                result  = serviceProvider.getMessageService().sendEmailMessage(email,message+code);
                redisUtil.set(email,code);
            }catch (Exception ex){
                ex.printStackTrace();
            }

        } else {
            return  new Response("1002","mobile and email 为Null");
        }
        if(!result){
            return  new Response("1002","mobile and email 为Null");
        }
        return  new Response("0","sucess");

    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public Response register(@RequestParam("username")String username,
                             @RequestParam("password")String password,
                             @RequestParam(value = "mobile",required =false) String mobile,
                             @RequestParam(value = "email",required =false) String email,
                             @RequestParam("verifycode")String verifycode) {

        if(StringUtils.isBlank(mobile) && StringUtils.isBlank(email)){
            return  new Response("1002","mobile and email 为Null");
        }
        if(StringUtils.isNotBlank(mobile)){
            String code  = redisUtil.get(mobile).toString();
            if(!code.equals(verifycode)){
                return  new Response("1003","mobile cod error");
            }
        }
        if(StringUtils.isNotBlank(email)){
            String code  = redisUtil.get(email).toString();
            if(!code.equals(verifycode)){
                return  new Response("1003","email cod error");
            }
        }

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setMobile(mobile);
        userInfo.setPassword(password);
        userInfo.setUsername(username);
        try {
            serviceProvider.getUserService().regiserUser(userInfo);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return  new Response("0","sucess ok");

    }
    @RequestMapping(value = "/authentication", method = RequestMethod.POST)
    @ResponseBody
    public  UserDTO authentication(@RequestHeader("token") String token){

        return (UserDTO) redisUtil.get(token);
    }

    private UserDTO toDTO(UserInfo userInfo){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(userInfo,userDTO);
        return  userDTO;

    }
    private String getToken() {
        return randomCode("0123456789abcdefghijklmnopqrstuvwxyz",32);
    }

    /**
     * 生成token
     * @param s
     * @param size
     * @return
     */
    private String randomCode(String s,int size) {

        StringBuilder result = new StringBuilder(size);
        Random random = new Random();
        for (int i =0;i<size;i++){
            int loc = random.nextInt(s.length());
            result.append(s.charAt(loc));
        }
        return result.toString();

    }

}

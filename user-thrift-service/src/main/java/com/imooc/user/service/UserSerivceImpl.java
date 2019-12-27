package com.imooc.user.service;

import com.imooc.thrift.user.UserInfo;
import com.imooc.thrift.user.UserService;
import com.imooc.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSerivceImpl implements UserService.Iface {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo getUserById(int id)  {
        try {
            return userMapper.getUserById(id);
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    public UserInfo getUserByName(String username)  {
        try {
            return  userMapper.getUserByName(username);
        }catch (Exception ex){
             ex.printStackTrace();
             return null;
        }
    }

    @Override
    public void regiserUser(UserInfo userInfo)  {
        try {
            userMapper.registerUser(userInfo);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

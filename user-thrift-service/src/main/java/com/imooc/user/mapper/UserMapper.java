package com.imooc.user.mapper;

import com.imooc.thrift.user.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    @Select("select * from pe_user where id =#{id}")
    UserInfo getUserById(@Param("id")int id);

    @Select("select * from pe_user where username =#{username}")
    UserInfo getUserByName(@Param("username")String username);

    @Insert("insert into pe_user (username,password,realname,mobile,email) " +
            "values (#{u.username},#{u.password},#{u.realname},#{u.mobile},#{u.email})")
    void registerUser(@Param("u") UserInfo userInfo);


}

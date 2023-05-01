package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User findByUsername(String username);

    @Insert("""
            INSERT INTO USERS (username, password, salt, firstname, lastname)
            VALUES (#{username}, #{password}, #{salt}, #{firstname}, #{lastname})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "userid")
    Integer insert(User user);
}

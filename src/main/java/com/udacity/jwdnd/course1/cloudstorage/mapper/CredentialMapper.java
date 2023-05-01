package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userid}")
    List<Credential> findAllByUser(Integer userid);

    @Select("SELECT * FROM CREDENITALS WHERE url = #{credentialUrl}")
    Credential findByCredentialUrl(String credentialUrl);

    @Insert("""
            INSERT INTO CREDENTIALS (url, username, "key", password, userid)
            VALUES (#{url}, #{username}, #{key}, #{password}, #{userid})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "credentialid")
    void save(Credential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{id}")
    Credential findById(Integer id);

    @Update("""
            UPDATE CREDENTIALS 
            SET url = #{url}, username = #{username}, "key" = #{key}, password = #{password} 
            WHERE credentialid = #{credentialid}
            """)
    void update(Credential credential);
}

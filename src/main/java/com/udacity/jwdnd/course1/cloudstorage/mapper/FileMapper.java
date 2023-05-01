package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userid}")
    List<File> findAllByUser(Integer userid);

    @Select("SELECT * FROM FILES WHERE filename = #{filename}")
    File findByFilename(String filename);

    @Insert("""
            INSERT INTO FILES (filename, contenttype, filesize, userid, filedata)
            VALUES (#{filename}, #{contenttype}, #{filesize}, #{userid}, #{filedata})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    void save(File file);

    @Delete("DELETE FROM FILES WHERE fileid = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File findById(Integer fileId);

    @Select("SELECT * FROM FILES WHERE filename = #{filename} AND userid = #{userid}")
    File findByFilenameAndUserid(String filename, Integer userid);
}

package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {

    @Select("SELECT * FROM NOTES WHERE userid = #{userid}")
    List<Note> findAllByUser(Integer userid);

    @Select("SELECT * FROM NOTES WHERE notetitle = #{notetitle}")
    File findByNoteTitle(String notetitle);

    @Insert("""
            INSERT INTO NOTES (notetitle, notedescription, userid)
            VALUES (#{notetitle}, #{notedescription}, #{userid})
            """)
    @Options(useGeneratedKeys = true, keyProperty = "noteid")
    void save(Note note);

    @Delete("DELETE FROM NOTES where noteid = #{id}")
    void deleteById(Integer id);

    @Select("SELECT * FROM NOTES WHERE noteid = #{id}")
    Note findByNoteId(Integer id);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteid = #{noteid}")
    void update(Note note);
}

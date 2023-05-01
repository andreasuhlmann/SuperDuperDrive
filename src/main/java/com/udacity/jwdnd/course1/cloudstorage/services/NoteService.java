package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    public List<Note> findAllByUser(User user) {
        return noteMapper.findAllByUser(user.getUserid());
    }

    public File findByNoteTitle(String notetitle) {
        return noteMapper.findByNoteTitle(notetitle);
    }

    public void save(Note note) {
        noteMapper.save(note);
    }

    public void deleteById(Integer id) {
        noteMapper.deleteById(id);
    }

    public Note findById(Integer id) {
        return noteMapper.findByNoteId(id);
    }

    public void update(Note note) {
        noteMapper.update(note);
    }
}

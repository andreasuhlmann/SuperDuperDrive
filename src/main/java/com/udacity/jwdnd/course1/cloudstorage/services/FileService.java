package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public List<File> findAllByUser(User user) {
        return fileMapper.findAllByUser(user.getUserid());
    }

    public void save(File file) {
        fileMapper.save(file);
    }

    public void deleteById(Integer id) {
        fileMapper.deleteById(id);
    }

    public File findById(Integer fileId) {
        return fileMapper.findById(fileId);
    }

    public boolean isFilenameAvailable(String filename, Integer userid) {
        return fileMapper.findByFilenameAndUserid(filename, userid) == null;
    }
}

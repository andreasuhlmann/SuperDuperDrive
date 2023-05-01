package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class CredentialService {

    private CredentialMapper credentialMapper;
    private EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> findAllByUser(User user) {
        List<Credential> allCredentialsByUser =  credentialMapper.findAllByUser(user.getUserid());
        allCredentialsByUser.forEach(this::decrypt);
        return allCredentialsByUser;
    }

    public Credential findByCredentialUrl(String credentialUrl) {
        return credentialMapper.findByCredentialUrl(credentialUrl);
    }

    public void save(Credential credential) {
        encrypt(credential);
        credentialMapper.save(credential);
    }

    public void deleteById(Integer id) {
        credentialMapper.deleteById(id);
    }

    private void encrypt(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        credential.setPassword(encryptedPassword);
        credential.setKey(encodedKey);
    }

    private void decrypt(Credential credential) {
        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(decryptedPassword);
    }

    public Credential findById(Integer id) {
        if (id == null) {
            return null;
        }

        Credential savedCredential = credentialMapper.findById(id);
        return savedCredential;
    }

    public void update(Credential credential) {
        encrypt(credential);
        credentialMapper.update(credential);
    }
}

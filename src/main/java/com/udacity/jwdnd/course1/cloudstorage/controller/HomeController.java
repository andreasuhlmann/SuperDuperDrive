package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private FileService fileService;
    private UserService userService;
    private NoteService noteService;
    private CredentialService credentialService;

    public HomeController(
            FileService fileService,
            UserService userService,
            NoteService noteService,
            CredentialService credentialService
    ) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
    }

    @GetMapping("/home")
    String homeView(Model model, Authentication authentication){
        showAllFilesByUser(model, authentication);
        showAllNotesByUser(model, authentication);
        showAllCredentialsByUser(model, authentication);
        return "/home";
    }

    @PostMapping("/files/upload")
    public String handleFileUpload(
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam("fileUpload") MultipartFile file
    ) throws IOException {
        String fileUploadError = null;
        String fileUploadSuccess = null;

        if (file.isEmpty()) {
            fileUploadError = "No file has been selected for upload.";
        } else {
            String username = authentication.getName();
            Integer userid = userService.findByUsername(username).getUserid();


            String filename = file.getOriginalFilename();

            if (!fileService.isFilenameAvailable(filename, userid)) {
                fileUploadError = "The document already exists.";
            }

            if (fileUploadError == null) {
                try {
                    fileService.save(
                        new File(
                            filename,
                            file.getContentType(),
                            Long.toString(file.getSize()),
                            userid,
                            file.getBytes()
                        )
                    );
                    fileUploadSuccess = "File has been successfully uploaded!";
                } catch (Exception e) {
                    fileUploadError = "The file could be not uploaded!";
                }
            }
        }

        redirectAttributes.addFlashAttribute("fileError", fileUploadError);
        redirectAttributes.addFlashAttribute("fileUploadSuccess", fileUploadSuccess);

        showAllFilesByUser(model, authentication);
        return "redirect:/home";
    }

    @GetMapping("/files/download/{fileId}")
    @ResponseBody
    public ResponseEntity<File> downloadFile(@PathVariable Integer fileId) {
        File file = fileService.findById(fileId);
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\""
        ).body(file);
    }
    
    @GetMapping("/files/delete/{fileId}")
    public String removeFile(
            Model model,
            Authentication authentication,
            @PathVariable Integer fileId,
            RedirectAttributes redirectAttributes
    ) {
        String fileDeletionSuccess = null;
        String fileUploadError = null;

        try {
            fileService.deleteById(fileId);
            fileDeletionSuccess = "File has been successfully deleted!";
        } catch (Exception e) {
            fileUploadError = "The file could be not uploaded!";
        }

        redirectAttributes.addFlashAttribute("fileError", fileUploadError);
        redirectAttributes.addFlashAttribute("fileDeletionSuccess", fileDeletionSuccess);

        showAllFilesByUser(model, authentication);
        return "redirect:/home";
    }

    @GetMapping("/notes/delete/{noteId}")
    public String removeNote(
            Model model,
            Authentication authentication,
            @PathVariable Integer noteId,
            RedirectAttributes redirectAttributes
    ) {
        String noteDeletionSuccess = null;
        String noteUploadError = null;

        try {
            noteService.deleteById(noteId);
            noteDeletionSuccess = "Note has been successfully deleted!";
        } catch (Exception e) {
            noteUploadError = "The file could be not uploaded!";
        }

        redirectAttributes.addFlashAttribute("noteError", noteUploadError);
        redirectAttributes.addFlashAttribute("noteDeletionSuccess", noteDeletionSuccess);

        showAllNotesByUser(model, authentication);
        return "redirect:/home";
    }

    @GetMapping("/credentials/delete/{credentialId}")
    public String removeCredential(
            Model model,
            Authentication authentication,
            @PathVariable Integer credentialId,
            RedirectAttributes redirectAttributes
    ) {
        String credentialDeletionSuccess = null;
        String credentialUploadError = null;

        try {
            credentialService.deleteById(credentialId);
            credentialDeletionSuccess = "Credential has been successfully deleted!";
        } catch (Exception e) {
            credentialUploadError = "The credential could be not uploaded!";
        }

        redirectAttributes.addFlashAttribute("credentialError", credentialUploadError);
        redirectAttributes.addFlashAttribute("credentialUploadSuccess", credentialDeletionSuccess);

        showAllCredentialsByUser(model, authentication);
        return "redirect:/home";
    }

    @PostMapping("/notes/upload")
    public String handleNoteUpload(
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "noteId", required = false) Integer noteId,
            @RequestParam("notetitle") String notetitle,
            @RequestParam("notedescription") String notedescription
    ) throws IOException {
        String noteUploadError = null;
        String noteUpdateSuccess = null;
        String noteUploadSuccess = null;

        String username = authentication.getName();
        Integer userid = userService.findByUsername(username).getUserid();

        Note existingNote = noteService.findById(noteId);

        try {
            if (existingNote != null) {
                noteService.update(new Note(noteId, notetitle, notedescription, userid));
                noteUpdateSuccess = "Note has been successfully updated!";
            } else {
                noteService.save(new Note(notetitle, notedescription, userid));
                noteUploadSuccess = "Note has been successfully uploaded!";
            }
        } catch (Exception e) {
            noteUploadError = "The note could be not uploaded!";
        }

        redirectAttributes.addFlashAttribute("noteError", noteUploadError);
        redirectAttributes.addFlashAttribute("noteUploadSuccess", noteUploadSuccess);
        redirectAttributes.addFlashAttribute("noteEditSuccess", noteUpdateSuccess);

        showAllNotesByUser(model, authentication);
        return "redirect:/home";
    }

    @PostMapping("/credentials/upload")
    public String handleCredentialUpload(
            Authentication authentication,
            Model model,
            RedirectAttributes redirectAttributes,
            @RequestParam(name = "credentialId", required = false) Integer credentialId,
            @RequestParam("url") String credentialUrl,
            @RequestParam("username") String credentialUsername,
            @RequestParam("password") String credentialPassword
    ) throws IOException {
        String credentialUploadError = null;
        String credentialUpdateSuccess = null;
        String credentialUploadSuccess = null;

        String username = authentication.getName();
        Integer userid = userService.findByUsername(username).getUserid();

        Credential existingCredential = credentialService.findById(credentialId);

        try {
            if (existingCredential != null) {
                credentialService.update(new Credential(
                        credentialId,
                        credentialUrl,
                        credentialUsername,
                        null,
                        credentialPassword,
                        userid
                ));
                credentialUpdateSuccess = "Credential has been successfully updated!";
            } else {
                credentialService.save(new Credential(
                        credentialUrl,
                        credentialUsername,
                        null,
                        credentialPassword,
                        userid
                ));
                credentialUploadSuccess = "Credential has been successfully uploaded!";
            }
        } catch (Exception e) {
            credentialUploadError = "The credential could be not uploaded!";
        }

        redirectAttributes.addFlashAttribute("credentialError", credentialUploadError);
        redirectAttributes.addFlashAttribute("credentialUploadSuccess", credentialUploadSuccess);
        redirectAttributes.addFlashAttribute("credentialEditSuccess", credentialUpdateSuccess);

        showAllCredentialsByUser(model, authentication);
        return "redirect:/home";
    }


    private void showAllFilesByUser(Model model, Authentication authentication) {
        User user = getAuthenticatedUser(model, authentication);
        List<File> allFiles = new ArrayList<>(fileService.findAllByUser(user));
        model.addAttribute("files", allFiles);
    }

    private void showAllNotesByUser(Model model, Authentication authentication) {
        User user = getAuthenticatedUser(model, authentication);
        List<Note> allNotes = new ArrayList<>(noteService.findAllByUser(user));
        model.addAttribute("notes", allNotes);
    }

    private void showAllCredentialsByUser(Model model, Authentication authentication) {
        User user = getAuthenticatedUser(model, authentication);
        List<Credential> allCredentials = new ArrayList<>(credentialService.findAllByUser(user));
        model.addAttribute("credentials", allCredentials);
    }

    private User getAuthenticatedUser(Model model, Authentication authentication) {
        String username = authentication.getName();
        return userService.findByUsername(username);
    }
}

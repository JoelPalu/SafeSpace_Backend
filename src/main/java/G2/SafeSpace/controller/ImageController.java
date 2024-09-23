package G2.SafeSpace.controller;

import G2.SafeSpace.config.CustomMultipart;
import G2.SafeSpace.entity.User;
import G2.SafeSpace.service.ImageService;
import G2.SafeSpace.service.UserContextService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/storage")
public class ImageController {

    private final ImageService imageService;
    private final UserContextService userContextService;


    public ImageController(ImageService imageService, UserContextService userContextService) {
        this.imageService = imageService;
        this.userContextService = userContextService;
    }

    @PostMapping("/profile")
    public ResponseEntity<String> profileImage(@RequestParam("file") MultipartFile file) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(150, 150)
                    .toOutputStream(outputStream);

            MultipartFile resizedFile = new CustomMultipart(file.getName(), file.getOriginalFilename(), file.getContentType(), outputStream.toByteArray());
            return ResponseEntity.status(200).body(imageService.saveImageToStorage(resizedFile, true));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error on image process");
        }
    }

    @PostMapping("/post")
    public ResponseEntity<String> postImage(@RequestParam("file") MultipartFile file) {
        Optional<User> optionalUser = getCurrentUser();
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Empty file");
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Thumbnails.of(file.getInputStream())
                    .size(100, 100)
                    .toOutputStream(outputStream);

            MultipartFile resizedFile = new CustomMultipart(file.getName(), file.getOriginalFilename(), file.getContentType(), outputStream.toByteArray());
            return ResponseEntity.status(200).body(imageService.saveImageToStorage(resizedFile, false));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error on image process");
        }
    }

    @GetMapping("/profile/{filename}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String filename) {
        try {
            Resource resource = imageService.loadImageAsResource(filename, true);
            return ResponseEntity.ok()
                    .contentType(getContentType(filename))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    @GetMapping("/post/{filename}")
    public ResponseEntity<Resource> getPostImage(@PathVariable String filename) {
        try {
            Resource resource = imageService.loadImageAsResource(filename, false);
            return ResponseEntity.ok()
                    .contentType(getContentType(filename))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    private MediaType getContentType(String filename) {
        if (filename.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.toLowerCase().endsWith(".jpeg") || filename.toLowerCase().endsWith(".jpg")) {
            return MediaType.IMAGE_JPEG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }

}

/**
 * REST controller for managing image upload, processing, and retrieval for user profiles and posts.
 */
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

/**
 * Controller for handling image-related API requests such as uploading and retrieving profile and post images.
 */
@RestController
@RequestMapping("/api/v1/storage")
public class ImageController {

    private final ImageService imageService;
    private final UserContextService userContextService;

    /**
     * Constructs an instance of {@link ImageController} with the required services.
     *
     * @param imageService the service handling image processing and storage.
     * @param userContextService the service providing the current user context.
     */
    public ImageController(ImageService imageService, UserContextService userContextService) {
        this.imageService = imageService;
        this.userContextService = userContextService;
    }

    /**
     * Endpoint to upload and process a profile image.
     *
     * @param file the image file to be uploaded.
     * @return a {@link ResponseEntity} with:
     *         <ul>
     *             <li>{@code 200 OK} - if the image is successfully processed and saved.</li>
     *             <li>{@code 400 Bad Request} - if the file is empty.</li>
     *             <li>{@code 401 Unauthorized} - if the user is not authenticated.</li>
     *             <li>{@code 500 Internal Server Error} - if there is an error during image processing.</li>
     *         </ul>
     */
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

    /**
     * Endpoint to upload and process a post image.
     *
     * @param file the image file to be uploaded.
     * @return a {@link ResponseEntity} with similar status codes and outcomes as the {@code profileImage} method.
     */
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

    /**
     * Endpoint to retrieve a profile image by its filename.
     *
     * @param filename the name of the file to retrieve.
     * @return a {@link ResponseEntity} containing the image as a {@link Resource}.
     *         <ul>
     *             <li>{@code 200 OK} - if the image is successfully retrieved.</li>
     *             <li>{@code 500 Internal Server Error} - if there is an error retrieving the image.</li>
     *         </ul>
     */
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

    /**
     * Endpoint to retrieve a post image by its filename.
     *
     * @param filename the name of the file to retrieve.
     * @return a {@link ResponseEntity} containing the image as a {@link Resource}.
     *         <ul>
     *             <li>{@code 200 OK} - if the image is successfully retrieved.</li>
     *             <li>{@code 500 Internal Server Error} - if there is an error retrieving the image.</li>
     *         </ul>
     */
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

    /**
     * Determines the content type of a file based on its extension.
     *
     * @param filename the name of the file.
     * @return the {@link MediaType} corresponding to the file extension.
     */
    private MediaType getContentType(String filename) {
        if (filename.toLowerCase().endsWith(".png")) {
            return MediaType.IMAGE_PNG;
        } else if (filename.toLowerCase().endsWith(".jpeg") || filename.toLowerCase().endsWith(".jpg")) {
            return MediaType.IMAGE_JPEG;
        } else {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    /**
     * Retrieves the currently authenticated user from the context.
     *
     * @return an {@link Optional} containing the current user, or empty if not authenticated.
     */
    private Optional<User> getCurrentUser() {
        return userContextService.getCurrentUser();
    }

}

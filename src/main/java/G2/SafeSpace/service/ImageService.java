package G2.SafeSpace.service;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {
    private final Path pathProfile = Path.of("/var/www/html/pictures/profile");
    private final Path pathPost = Path.of("/var/www/html/pictures/post");
    private final Path pathTest = Path.of("C:/Users/miroh/Documents/testi"); // for local testing only
    private final Path pathTestPost = Path.of("C:/Users/miroh/Documents/testi/post"); // for local testing only, remember to change when build


    public String saveImageToStorage(MultipartFile image, boolean isProfile) {
        Path filePath;
        try {
            String uniqueFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            if (isProfile) {
                //filePath = pathProfile.resolve(uniqueFilename);
                filePath = pathTest.resolve(uniqueFilename);
            } else {
                //filePath = pathPost.resolve(uniqueFilename);
                filePath = pathTestPost.resolve(uniqueFilename);
            }


            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;
        } catch (IOException e) {
            e.getMessage();
            return null;
        }
    }

    public Resource loadImageAsResource(String filename, boolean isProfile) {
        try {
            Path filePath;
            if (isProfile) {
                filePath = pathProfile.resolve(filename).normalize();
            } else {
                filePath = pathPost.resolve(filename).normalize();
            }
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not found or not readable: " + filename);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error loading file: " + filename, e);
        }
    }
}

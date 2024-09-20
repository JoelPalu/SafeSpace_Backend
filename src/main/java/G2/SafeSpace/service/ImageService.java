package G2.SafeSpace.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class ImageService {
    private final Path path = Path.of("/var/www/html/pictures");

    public String saveImageToStorage(MultipartFile image) {
        try {
            String uniqueFilename = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path filePath = path.resolve(uniqueFilename);

            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;
        } catch (IOException e) {
            e.getMessage();
            return null;
        }
    }
}

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

/**
 * The {@code ImageService} class provides methods for saving and loading image files.
 * It supports saving images to either a profile or post directory and allows loading
 * those images as resources. The images are stored with a unique filename to prevent
 * conflicts and ensure they can be accessed securely.
 */
@Service
public class ImageService {

    private final Path pathProfile = Path.of("/var/www/html/pictures/profile");
    private final Path pathPost = Path.of("/var/www/html/pictures/post");

    /**
     * Saves an image to the storage system. The image is saved in either the profile
     * or post directory based on the provided {@code isProfile} flag. The image is
     * given a unique filename using a randomly generated UUID to avoid name conflicts.
     *
     * @param image the {@link MultipartFile} containing the image data
     * @param isProfile a boolean flag indicating if the image is for a profile (true)
     *                  or a post (false)
     * @return the unique filename of the saved image, or {@code null} if an error occurred
     */
    public String saveImageToStorage(MultipartFile image, boolean isProfile) {
        Path filePath;
        try {
            String uniqueFilename = UUID.randomUUID() + "_" + image.getOriginalFilename();
            if (isProfile) {
                filePath = pathProfile.resolve(uniqueFilename);
            } else {
                filePath = pathPost.resolve(uniqueFilename);
            }

            // Save the image file to the target directory
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return uniqueFilename;
        } catch (IOException e) {
            // Log error and return null in case of failure
            e.getMessage();
            return null;
        }
    }

    /**
     * Loads an image as a {@link Resource} from the storage system. The image is loaded
     * from either the profile or post directory based on the provided {@code isProfile} flag.
     * If the file is not found or is unreadable, an exception is thrown.
     *
     * @param filename the name of the image file to load
     * @param isProfile a boolean flag indicating if the image is for a profile (true)
     *                  or a post (false)
     * @return the {@link Resource} representing the image file
     * @throws RuntimeException if the file is not found or is not readable
     */
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

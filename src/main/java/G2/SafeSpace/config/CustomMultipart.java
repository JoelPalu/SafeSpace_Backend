/**
 * Custom implementation of {@link MultipartFile} for handling file uploads.
 * Provides a constructor for creating an in-memory representation of a file
 * with its associated metadata such as name, original filename, content type, 
 * and byte content.
 */
package G2.SafeSpace.config;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CustomMultipart implements MultipartFile {

    private final String name;
    private final String originalFilename;
    private final String contentType;
    private final byte[] content;

    /**
     * Constructs a new {@code CustomMultipart} object.
     *
     * @param name             the name of the parameter in the multipart form.
     * @param originalFilename the original name of the uploaded file.
     * @param contentType      the MIME type of the file.
     * @param content          the file's content as a byte array.
     */
    public CustomMultipart(String name, String originalFilename, String contentType, byte[] content) {
        this.name = name;
        this.originalFilename = originalFilename;
        this.contentType = contentType;
        this.content = content;
    }

    /**
     * @return the name of the parameter in the multipart form.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the original filename in the client's filesystem.
     */
    @Override
    public String getOriginalFilename() {
        return originalFilename;
    }

    /**
     * @return the content type of the file.
     */
    @Override
    public String getContentType() {
        return contentType;
    }

    /**
     * @return {@code true} if the file is empty; {@code false} otherwise.
     */
    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    /**
     * @return the size of the file in bytes.
     */
    @Override
    public long getSize() {
        return content.length;
    }

    /**
     * @return the content of the file as a byte array.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public byte[] getBytes() throws IOException {
        return content;
    }

    /**
     * @return an {@code InputStream} to read the file's content.
     * @throws IOException if an I/O error occurs.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
    }

    /**
     * Throws an {@code UnsupportedOperationException} as transferring the file 
     * to a physical destination is not supported.
     *
     * @param dest the destination file.
     * @throws IOException              if an I/O error occurs.
     * @throws IllegalStateException    if the file state is illegal for transfer.
     * @throws UnsupportedOperationException always.
     */
    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        throw new UnsupportedOperationException("transferTo not supported");
    }
}

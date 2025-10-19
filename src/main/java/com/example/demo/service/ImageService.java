package com.example.demo.service;

import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.UUID;

@Service
public class ImageService {

    // Maximum dimensions for the resized image
    private static final int MAX_WIDTH = 1920;
    private static final int MAX_HEIGHT = 1080;
    private static final float COMPRESSION_QUALITY = 0.85f; // 0.0 to 1.0 (higher = better quality)

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("travel-hotelbooking.firebasestorage.app", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("image/jpeg").build(); // Changed to image/jpeg
        InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream("travel-hotelbooking-firebase-adminsdk-fbsvc-ac3417e239.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/travel-hotelbooking.firebasestorage.app/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    private String getExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ".jpg"; // Default extension
        }
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ".jpg"; // No extension found, return default
        }
        return fileName.substring(lastIndexOf);
    }

    /**
     * Compresses and resizes the image
     */
    private File compressImage(File inputFile, String outputFileName) throws IOException {
        // Read the original image
        BufferedImage originalImage = ImageIO.read(inputFile);

        if (originalImage == null) {
            throw new IOException("Unable to read image file");
        }

        // Calculate new dimensions while maintaining aspect ratio
        int originalWidth = originalImage.getWidth();
        int originalHeight = originalImage.getHeight();

        int newWidth = originalWidth;
        int newHeight = originalHeight;

        // Only resize if image is larger than max dimensions
        if (originalWidth > MAX_WIDTH || originalHeight > MAX_HEIGHT) {
            double aspectRatio = (double) originalWidth / originalHeight;

            if (originalWidth > originalHeight) {
                newWidth = MAX_WIDTH;
                newHeight = (int) (MAX_WIDTH / aspectRatio);
            } else {
                newHeight = MAX_HEIGHT;
                newWidth = (int) (MAX_HEIGHT * aspectRatio);
            }
        }

        // Create resized image
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = resizedImage.createGraphics();

        // Enable high-quality rendering
        graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
        graphics.dispose();

        // Save compressed image
        File compressedFile = new File(outputFileName);

        // Get JPEG writer
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
        if (!writers.hasNext()) {
            throw new IOException("No JPEG writer available");
        }

        ImageWriter writer = writers.next();
        ImageWriteParam writeParam = writer.getDefaultWriteParam();

        // Set compression quality
        writeParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        writeParam.setCompressionQuality(COMPRESSION_QUALITY);

        // Write the compressed image
        try (FileOutputStream fos = new FileOutputStream(compressedFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            writer.setOutput(ios);
            writer.write(null, new javax.imageio.IIOImage(resizedImage, null, null), writeParam);
        } finally {
            writer.dispose();
        }

        return compressedFile;
    }

    public String upload(MultipartFile multipartFile) {
        File originalFile = null;
        File compressedFile = null;

        try {
            // Validate multipart file
            if (multipartFile == null || multipartFile.isEmpty()) {
                return "No file provided";
            }

            String fileName = multipartFile.getOriginalFilename();

            // Additional null check
            if (fileName == null || fileName.trim().isEmpty()) {
                fileName = "image.jpg"; // Default filename
            }

            String extension = this.getExtension(fileName);

            // Check if it's an image file
            if (!isImageFile(extension)) {
                return "Only image files are allowed";
            }

            String uniqueFileName = UUID.randomUUID().toString();
            String tempFileName = uniqueFileName + extension;
            String compressedFileName = uniqueFileName + ".jpg"; // Always save as JPEG

            // Convert multipart file to file
            originalFile = this.convertToFile(multipartFile, tempFileName);

            // Compress and resize the image
            compressedFile = this.compressImage(originalFile, compressedFileName);

            // Upload compressed file
            String URL = this.uploadFile(compressedFile, compressedFileName);

            return URL;
        } catch (Exception e) {
            e.printStackTrace();
            return "Image couldn't upload, Something went wrong: " + e.getMessage();
        } finally {
            // Clean up temporary files
            if (originalFile != null && originalFile.exists()) {
                originalFile.delete();
            }
            if (compressedFile != null && compressedFile.exists()) {
                compressedFile.delete();
            }
        }
    }

    /**
     * Check if the file extension is a valid image format
     */
    private boolean isImageFile(String extension) {
        String ext = extension.toLowerCase();
        return ext.equals(".jpg") || ext.equals(".jpeg") ||
                ext.equals(".png") || ext.equals(".gif") ||
                ext.equals(".bmp") || ext.equals(".webp");
    }
}
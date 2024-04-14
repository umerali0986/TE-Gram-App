package com.techelevator.util;

import com.techelevator.model.Image;

import java.io.File;

public class ImageFileProvider {

    private static final String DEFAULT_ABSOLUTE_PATH = "C:/Users/Student/workspace/java-gray-finalcapstone-team2/java/src/ImageFiles/";

    public File createImageFile(Image image) {
        File imageUploadsDir = createImageUploadsDir();
        String imageFileName = String.format("image-%d.%s", image.getImageId(), image.getImageType());
        return new File(imageUploadsDir, imageFileName);
    }

    private File createImageUploadsDir() {
        String absolutePath = System.getenv("UPLOADS_DIR");
        if (null == absolutePath || absolutePath.isBlank())
            absolutePath = DEFAULT_ABSOLUTE_PATH;
        return new File(absolutePath);
    }

}
package model;

import java.io.File;

public class UpdateImageRequest {
    private String userId;
    private File image;

    public UpdateImageRequest(String userId, File image) {
        this.image = image;
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public File getImage() {
        return image;
    }
}

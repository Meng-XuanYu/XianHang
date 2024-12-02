package model;

public class AIPublishRequest {
    private String image;

    public AIPublishRequest(String imageUrl) {
        this.image = imageUrl;
    }

    public String getImageUrl() {
        return image;
    }
}

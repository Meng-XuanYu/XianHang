package Message;

import android.net.Uri;

public class MessageLan {
    private Uri avatar;
    private String messageText;
    private Uri commodityImage;
    private String name;
    private String otherId;
    private String commodityId;

    public MessageLan(Uri uri, String messageText, Uri commodityImage, String name, String otherId, String commodityId) {
        this.avatar = uri;
        this.messageText = messageText;
        this.name = name;
        this.commodityImage = commodityImage;
        this.otherId = otherId;
        this.commodityId = commodityId;
    }

    public String getCommodityId() {
        return commodityId;
    }

    public String getOtherId() {
        return otherId;
    }

    public String getName() {
        return name;
    }

    public Uri getAvatar() {
        return avatar;
    }

    public String getMessageText() {
        return messageText;
    }

    public Uri getCommodityImage() {
        return commodityImage;
    }
}
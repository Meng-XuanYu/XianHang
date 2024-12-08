package model;

public class DeleteCommodityRequest {
    private String commodityId;

    public DeleteCommodityRequest(String id) {
        this.commodityId = id;
    }

    public String getCommodityId() {
        return commodityId;
    }
}

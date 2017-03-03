package com.ins.feast.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/17.
 */

public class Card implements Serializable {
    private int id;
    private String topBanner;
    private String banners;
    private float price;
    private int promotionPrice;
    private String name;
    private int recommendationIndex;
    private int sales;
    private String units;
    private int leastNumber;
    private String introduction;
    private int foodCategoryId;
    private long createTime;

    //本地字段
    private int index;

    public Card(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTopBanner() {
        return topBanner;
    }

    public void setTopBanner(String topBanner) {
        this.topBanner = topBanner;
    }

    public String getBanners() {
        return banners;
    }

    public void setBanners(String banners) {
        this.banners = banners;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(int promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRecommendationIndex() {
        return recommendationIndex;
    }

    public void setRecommendationIndex(int recommendationIndex) {
        this.recommendationIndex = recommendationIndex;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public int getLeastNumber() {
        return leastNumber;
    }

    public void setLeastNumber(int leastNumber) {
        this.leastNumber = leastNumber;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getFoodCategoryId() {
        return foodCategoryId;
    }

    public void setFoodCategoryId(int foodCategoryId) {
        this.foodCategoryId = foodCategoryId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}

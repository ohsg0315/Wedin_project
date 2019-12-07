package com.hong.fragement.Event;

// 이벤트페이지에 사용될 객체를 위한 클래스
public class EventInfo {

    private String OTTsiteLogoImage;
    private String name;
    private String webUrl;
    private String imageUrl;

    public EventInfo() { }

    public EventInfo(String OTTsiteLogoImage, String webUrl, String name, String imageUrl) {
        this.OTTsiteLogoImage = OTTsiteLogoImage;
        this.name=name;
        this.webUrl=webUrl;
        this.imageUrl =  imageUrl;
    }


    public String getOTTsiteLogoImage() {
        return OTTsiteLogoImage;
    }
    public String getName() {
        return name;
    }
    public String getWebUrl() {
        return webUrl;
    }
    public String getImageUrl() {
        return imageUrl;
    }


    public void setOTTsiteLogoImage(String OTTsiteLogoImage) {
        this.OTTsiteLogoImage = OTTsiteLogoImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }


}

package com.hong.fragement.Event;

// 이벤트페이지에 사용될 객체를 위한 클래스
public class EventInfo {

    private int OTTsiteLogoImage;
    private String nameLogo;
    private String webUrl;
    private String eventUrl;

    public EventInfo() { }

    public EventInfo(int OTTsiteLogoImage, String webUrl, String nameLogo, String eventUrl) {
        this.OTTsiteLogoImage = OTTsiteLogoImage;
        this.nameLogo=nameLogo;
        this.webUrl=webUrl;
        this.eventUrl =  eventUrl;
    }


    public int getOTTsiteLogoImage() {
        return OTTsiteLogoImage;
    }
    public String getNameLogo() {
        return nameLogo;
    }
    public String getWebUrl() {
        return webUrl;
    }
    public String getEventUrl() {
        return eventUrl;
    }


    public void setOTTsiteLogoImage(int OTTsiteLogoImage) {
        this.OTTsiteLogoImage = OTTsiteLogoImage;
    }

    public void setNameLogo(String nameLogo) {
        this.nameLogo = nameLogo;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public void setEventUrl(String eventUrl) {
        this.eventUrl = eventUrl;
    }


}

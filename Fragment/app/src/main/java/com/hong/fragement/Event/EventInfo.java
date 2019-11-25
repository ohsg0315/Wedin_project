package com.hong.fragement.Event;

// 이벤트페이지에 사용될 객체를 위한 클래스
public class EventInfo {

    private int OTTsiteLogoImage;
    private String nameLogo;
    private int eventImage;

    public EventInfo(int OTTsiteLogoImage, int eventImage, String nameLogo) {
        this.OTTsiteLogoImage = OTTsiteLogoImage;
        this.nameLogo=nameLogo;
        this.eventImage = eventImage;
    }


    public int getOTTsiteLogoImage() {
        return OTTsiteLogoImage;
    }
    public String getNameLogo() {
        return nameLogo;
    }
    public int getEventImage() {
        return eventImage;
    }


    public void setOTTsiteLogoImage(int OTTsiteLogoImage) {
        this.OTTsiteLogoImage = OTTsiteLogoImage;
    }

    public void setNameLogo(String nameLogo) {
        this.nameLogo = nameLogo;
    }

    public void setEventImage(int eventImage) {
        this.eventImage = eventImage;
    }


}

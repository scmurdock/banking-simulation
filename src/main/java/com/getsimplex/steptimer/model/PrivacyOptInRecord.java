package com.getsimplex.steptimer.model;

public class PrivacyOptInRecord {

    private boolean shareCurrentLocation;
    private boolean shareHomeBranchLocation;
    private boolean shareEmailWithThirdParties;
    private boolean sharePhoneNumberWithMarketing;
    private boolean socialBankingAppProfilePublic;

    public boolean isShareCurrentLocation() {
        return shareCurrentLocation;
    }

    public void setShareCurrentLocation(boolean shareCurrentLocation) {
        this.shareCurrentLocation = shareCurrentLocation;
    }

    public boolean isShareHomeBranchLocation() {
        return shareHomeBranchLocation;
    }

    public void setShareHomeBranchLocation(boolean shareHomeBranchLocation) {
        this.shareHomeBranchLocation = shareHomeBranchLocation;
    }

    public boolean isShareEmailWithThirdParties() {
        return shareEmailWithThirdParties;
    }

    public void setShareEmailWithThirdParties(boolean shareEmailWithThirdParties) {
        this.shareEmailWithThirdParties = shareEmailWithThirdParties;
    }

    public boolean isSharePhoneNumberWithMarketing() {
        return sharePhoneNumberWithMarketing;
    }

    public void setSharePhoneNumberWithMarketing(boolean sharePhoneNumberWithMarketing) {
        this.sharePhoneNumberWithMarketing = sharePhoneNumberWithMarketing;
    }

    public boolean isSocialBankingAppProfilePublic() {
        return socialBankingAppProfilePublic;
    }

    public void setSocialBankingAppProfilePublic(boolean socialBankingAppProfilePublic) {
        this.socialBankingAppProfilePublic = socialBankingAppProfilePublic;
    }
}

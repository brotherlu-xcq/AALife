package com.aalife.bo;

/**
 * @author brother lu
 * @date 2018-06-06
 */
public class WxUserBo {
    private String wxCode;
    private String iv;
    private String encryptedData;

    public String getWxCode() {
        return wxCode;
    }

    public void setWxCode(String wxCode) {
        this.wxCode = wxCode;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }
}

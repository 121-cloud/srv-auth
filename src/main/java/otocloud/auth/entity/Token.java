package otocloud.auth.entity;

/**
 * Created by zhangye on 15/10/8.
 */
public class Token extends JsonableAdapter {
    //{"access_token":"ACCESS_TOKEN","expires_in":7200}
    private String accessToken;
    private long expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
}

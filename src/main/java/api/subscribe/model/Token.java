package api.subscribe.model;

import java.util.Date;

public class Token {

    private final String token;

    private final Date expiration;

    private String plan;

    public Token(String name, Date expiration, String plan) {
        this.token = name;
        this.expiration = expiration;
        this.plan = plan;
    }

    public String getToken() {
        return token;
    }

    public Date getExpiration() {
        return expiration;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}

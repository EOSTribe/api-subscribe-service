package api.subscribe.model;

import java.util.Date;

public class Token {

    private final String token;

    private final Date expiration;

    private final String plan;

    private String message;


    public Token(String name, Date expiration, String plan) {
        this.token = name;
        this.expiration = expiration;
        this.plan = plan;
        this.message = "";
    }

    public Token(String message) {
        this.token = "N/A";
        this.expiration = new Date();
        this.plan = "N/A";
        this.message = message;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

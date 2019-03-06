package api.subscribe;

import java.util.Date;

public class Token {

    private final String token;

    private Date expiration;

    public Token(String name, Date expiration) {
        this.token = name;
        this.expiration = expiration;
    }

    public String getToken() {
        return token;
    }

    public Date getExpiration() {
        return expiration;
    }

}

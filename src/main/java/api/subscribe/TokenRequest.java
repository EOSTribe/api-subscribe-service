package api.subscribe;

public class TokenRequest {

    private final String account;
    private final String transaction;
    private final String secret;

    public TokenRequest(String account, String transaction, String secret) {
        this.account = account;
        this.transaction = transaction;
        this.secret = secret;
    }

    public String getAccount() {
        return account;
    }

    public String getTransaction() {
        return transaction;
    }

    public String getSecret() { return secret; }
}

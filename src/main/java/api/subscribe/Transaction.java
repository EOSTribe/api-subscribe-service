package api.subscribe;

public class Transaction {

    private String action;

    private String from;

    private String to;

    private Float quantity;

    private String token;

    private String memo;

    private String error;


    public Transaction() {
        this.error = null;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        if(this.error != null) {
            return "Error ["+action+", Error: "+error+"]";
        } else {
            return "Transaction [" + action + ", " + from + ", " + to + ", " + quantity + " "+token+", " + memo + "]";
        }
    }
}

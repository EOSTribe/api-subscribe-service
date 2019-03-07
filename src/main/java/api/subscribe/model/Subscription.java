package api.subscribe.model;

import java.util.Date;


public class Subscription {

    private String token;

    private String transaction;

    private String account;

    private Date issueDate;

    private Date expirationDate;

    private String plan; // L1 | L2 | L3

    private Integer status = 1; // 1 - On (default), 0 - Off

    private Float eosPaid;

    private String memo;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTransaction() {
        return transaction;
    }

    public void setTransaction(String transaction) {
        this.transaction = transaction;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getEosPaid() {
        return eosPaid;
    }

    public void setEosPaid(Float eosPaid) {
        this.eosPaid = eosPaid;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

}

package api.subscribe.db;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="Subscription")
public class Subscription {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    @Column(name = "token")
    private String token;

    @Column(name = "transaction")
    private String transaction;

    @Column(name = "action")
    private String account;

    @Column(name = "secret")
    private String secret;

    @Column(name = "issueDate")
    private Date issueDate;

    @Column(name = "expirationDate")
    private Date expirationDate;

    @Column(name = "plan")
    private String plan; // L1 | L2 | L3

    @Column(name = "eosPaid")
    private Float eosPaid;

    @Column(name = "memo")
    private String memo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
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

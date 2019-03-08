package api.subscribe.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:global.properties")
@ConfigurationProperties
public class Properties {

    @Value("${history-api}")
    private String historyApi;

    @Value("${haproxy-api}")
    private String haproxyApi;

    @Value("${receiver-account}")
    private String receiverAccount;

    @Value("${private-key}")
    private String privateKey;

    @Value("${L1-cost-eos}")
    private Integer L1cost;

    @Value("${L2-cost-eos}")
    private Integer L2cost;

    @Value("${L3-cost-eos}")
    private Integer L3cost;

    public String getHistoryApi() {
        return historyApi;
    }

    public String getHaproxyApi() { return haproxyApi; }

    public String getReceiverAccount() {
        return receiverAccount;
    }

    protected String getPrivateKey() {
        return privateKey;
    }

    public Integer getL1cost() {
        return L1cost;
    }

    public Integer getL2cost() {
        return L2cost;
    }

    public Integer getL3cost() {
        return L3cost;
    }
}

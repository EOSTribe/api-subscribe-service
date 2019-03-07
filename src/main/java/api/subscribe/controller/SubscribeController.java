package api.subscribe.controller;


import api.subscribe.model.Token;
import api.subscribe.model.TokenRequest;
import api.subscribe.model.Transaction;
import api.subscribe.model.Subscription;
import api.subscribe.repo.SubscriptionRepository;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.codehaus.jettison.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.plactal.ecc.crypto.ec.EosPrivateKey;
import io.plactal.ecc.crypto.ec.EcSignature;
import io.plactal.ecc.crypto.ec.EcDsa;
import io.plactal.ecc.crypto.digest.Sha256;


import org.codehaus.jettison.json.JSONObject;

import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@RestController
public class SubscribeController {


    private static Logger LOGGER = LoggerFactory.getLogger(SubscribeController.class);

    private String HISTORY_API;
    private String RECEIVER_ACCOUNT;
    private final String EOS = "EOS";
    private final Float MIN_AMOUNT = new Float(10.0F);
    private EosPrivateKey PRIVATE_KEY;

    private final String L1 = "L1";
    private final String L2 = "L2";
    private final String L3 = "L3";
    private final String AGENT_NAME = "EOS Tribe API Subscribe Service";

    private Map<String, Integer> planCost = new HashMap<>(3);

    @Autowired
    SubscriptionRepository repository;

    @Autowired
    public void setProperties(Properties properties) {
        PRIVATE_KEY = new EosPrivateKey(properties.getPrivateKey());
        HISTORY_API = properties.getHistoryApi();
        RECEIVER_ACCOUNT = properties.getReceiverAccount();
        planCost.put(L1, properties.getL1cost());
        planCost.put(L2, properties.getL2cost());
        planCost.put(L3, properties.getL3cost());
    }

    @RequestMapping(value = "/subscribe", method = RequestMethod.POST)
    @ResponseBody
    public Token getToken(@RequestBody TokenRequest request) {
        String account = request.getAccount();
        String transId = request.getTransaction();
        String secret = request.getSecret();
        try {
            JSONObject transJson = getTransaction(transId);
            Transaction transaction = parseTransaction(transJson);
            boolean valid = isValidTransaction(transaction, account);
            LOGGER.info(transaction.toString() + " - valid: " + valid);
            if (valid) {
                String info = account + "." + secret;
                Sha256 digest = Sha256.from(info.getBytes());
                EcSignature signature = EcDsa.sign(digest, PRIVATE_KEY);
                Subscription subscription = createSubscription(account, transId, signature.toString(),
                        transaction.getQuantity(), transaction.getMemo());
                repository.add(subscription);
                Token token = new Token(signature.toString(),
                        subscription.getExpirationDate(),
                        subscription.getPlan());
                int status = sendTokenToHaproxy(token.toString());
                if(status != 200) {
                    token.setPlan(token.getPlan()+": Error registering token. Contact support!");
                }
                return token;
            } else {
                LOGGER.warn("Invalid token request: " + transaction.getError());
                return new Token("Invalid token request: " + transaction.getError(), null, null);
            }
        } catch (Exception ex) {
            LOGGER.warn("Error processing transaction: " + ex.getMessage(), ex);
            return new Token("Error processing transaction", null, null);
        }
    }

    private int sendTokenToHaproxy(String token) {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            String request = "http://api4.eostribe.io/setmap?apikey=" + token + "&sla=full";
            HttpGet httpGet = new HttpGet(request);
            httpGet.addHeader("User-Agent", AGENT_NAME);
            CloseableHttpResponse httpResponse = httpclient.execute(httpGet);
            int responseCode = httpResponse.getStatusLine().getStatusCode();
            return responseCode;
        } catch(Exception ex) {
            LOGGER.warn("Error sending token to Haproxy", ex);
            return 0;
        }
    }

    private Subscription createSubscription(String account,
                                            String transaction,
                                            String token,
                                            Float amount,
                                            String memo) {
        Subscription subscription = new Subscription();
        subscription.setAccount(account);
        subscription.setTransaction(transaction);
        subscription.setToken(token);
        subscription.setEosPaid(amount);
        subscription.setMemo(memo);
        subscription.setStatus(1);
        String plan = L1;
        if(memo.contains(L3)) {
            plan = L3;
        } else if(memo.contains(L2)) {
            plan = L2;
        }
        subscription.setPlan(plan);
        Integer cost = planCost.get(plan);
        int periodHours = Math.round(24*31*(amount/cost));
        Date today = new Date();
        subscription.setIssueDate(today);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.HOUR, periodHours);
        subscription.setExpirationDate(calendar.getTime());
        return subscription;
    }

    private boolean isValidTransaction(Transaction transaction, String account) {
        if(transaction.getError() != null) return false;
        if(transaction.getTo() == null || !transaction.getTo().equals(RECEIVER_ACCOUNT)) return false;
        if(transaction.getFrom() == null || !transaction.getFrom().equals(account)) return false;
        if(transaction.getQuantity() == null || transaction.getQuantity() < MIN_AMOUNT) return false;
        if(transaction.getToken() == null || !transaction.getToken().equals(EOS)) return false;
        return true;
    }

    private Transaction parseTransaction(JSONObject tran) {
        Transaction transaction = new Transaction();
        try {
            if (tran.has("action_traces")) {
                JSONArray traces = tran.getJSONArray("action_traces");
                JSONObject action = traces.getJSONObject(0);
                if(action.has("act")) {
                    JSONObject act = action.getJSONObject("act");
                    String actName = act.getString("name");
                    transaction.setAction(actName);
                    if(actName == null || !actName.equals("transfer")) {
                        transaction.setError("Not transfer transaction");
                    }
                    if (act.has("data")) {
                        JSONObject data = act.getJSONObject("data");
                        String from = data.getString("from");
                        String to = data.getString("to");
                        String quantity = data.getString("quantity");
                        String memo = data.getString("memo");
                        transaction.setFrom(from);
                        transaction.setTo(to);
                        transaction.setMemo(memo);
                        String[] qParts = quantity.split(" ");
                        Float amount = Float.parseFloat(qParts[0]);
                        String token = qParts[1];
                        transaction.setQuantity(amount);
                        transaction.setToken(token);
                    } else {
                        transaction.setError("No transaction data found");
                    }
                } else {
                    transaction.setError("No transaction act found");
                }
            } else {
                transaction.setError("No transaction action_traces found");
            }
        } catch(Exception ex) {
            LOGGER.warn("Error parsing Transaction JSON:"+ex.getMessage());
        }
        return transaction;
    }

    private JSONObject getTransaction(String id) throws Exception {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String body = "{\"id\":\""+id+"\"}";
        StringEntity se = new StringEntity(body);
        se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        HttpPost post = new HttpPost(HISTORY_API + "/v1/history/get_transaction");
        post.setEntity(se);
        post.addHeader("User-Agent", AGENT_NAME);
        CloseableHttpResponse httpResponse = httpclient.execute(post);
        int responseCode = httpResponse.getStatusLine().getStatusCode();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                httpResponse.getEntity().getContent()));

        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = reader.readLine()) != null) {
            response.append(inputLine);
        }
        reader.close();
        httpclient.close();
        if(responseCode == 200) {
            return new JSONObject(response.toString());
        } else {
            LOGGER.warn("Error retrieving transaction: "+body+
                    ", got response code: "+responseCode);
            return null;
        }
    }


}

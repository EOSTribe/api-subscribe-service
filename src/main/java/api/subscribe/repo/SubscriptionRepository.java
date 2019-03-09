package api.subscribe.repo;

import api.subscribe.model.Subscription;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionRepository {

    private static final String COUNT_BY_TOKEN_SQL = "SELECT COUNT(token) FROM SUBSCRIPTIONS WHERE token = ?";
    private static final String GET_BY_TOKEN_SQL = "SELECT * FROM SUBSCRIPTIONS WHERE token = ?";
    private static final String ADD_SUBSCRIPTION = "INSERT INTO SUBSCRIPTIONS VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String DEL_BY_TOKEN_SQL = "DELETE FROM SUBSCRIPTIONS WHERE token = ?";
    private static final String RENEW_BY_TOKEN_SQL = "UPDATE SUBSCRIPTIONS SET transaction = ?, plan = ?, expiration_date = ? WHERE token = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Subscription get(String token) {
        if(tokenExists(token)) {
            Subscription subscription = jdbcTemplate.queryForObject(GET_BY_TOKEN_SQL, new Object[]{token},
                    new BeanPropertyRowMapper<>(Subscription.class));
            return subscription;
        } else {
            return null;
        }
    }

    public boolean tokenExists(String token) {
        Integer count = jdbcTemplate.queryForObject(COUNT_BY_TOKEN_SQL, new Object[]{token}, Integer.class);
        if(count > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int add(Subscription subscription) {
        return jdbcTemplate.update(ADD_SUBSCRIPTION,
                subscription.getToken(),
                subscription.getTransaction(),
                subscription.getAccount(),
                subscription.getIssueDate(),
                subscription.getExpirationDate(),
                subscription.getPlan(),
                subscription.getStatus(),
                subscription.getEosPaid(),
                subscription.getMemo());
    }

    public int renew(Subscription subscription) {
        return jdbcTemplate.update(RENEW_BY_TOKEN_SQL,
                subscription.getTransaction(),
                subscription.getPlan(),
                subscription.getExpirationDate(),
                subscription.getToken());
    }

    public int delete(String token) {
        return jdbcTemplate.update(DEL_BY_TOKEN_SQL, token);
    }

}

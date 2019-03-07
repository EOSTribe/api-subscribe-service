package api.subscribe.repo;

import api.subscribe.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SubscriptionRepository {

    private static final String GET_BY_TOKEN_SQL = "SELECT * FROM SUBSCRIPTIONS WHERE TOKEN = ?";
    private static final String ADD_SUBSCRIPTION = "INSERT INTO SUBSCRIPTIONS VALUES(?,?,?,?,?,?,?,?,?)";
    private static final String DEL_BY_TOKEN_SQL = "DELETE FROM SUBSCRIPTIONS WHERE TOKEN = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    public Subscription get(String token) {
        Subscription subscription = jdbcTemplate.queryForObject(GET_BY_TOKEN_SQL, new Object[]{token},
                new BeanPropertyRowMapper<>(Subscription.class));
        return subscription;
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

    public int delete(String token) {
        return jdbcTemplate.update(DEL_BY_TOKEN_SQL, token);
    }

}

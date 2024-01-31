/*package tacos;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class H2IntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDefaultSettings() throws Exception {
        assertThat(this.jdbcTemplate.queryForObject("SELECT 1", Integer.class)).isEqualTo(1);
    }
}*/

package tacos.authorization;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingJwtDecoder implements JwtDecoder {

    private final JwtDecoder delegate;
    private static final Logger log = LoggerFactory.getLogger(LoggingJwtDecoder.class);

    public LoggingJwtDecoder(JwtDecoder delegate) {
        this.delegate = delegate;
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        log.info("Decoding JWT: {}", token);
        try {
            Jwt jwt = delegate.decode(token);
            log.info("Decoded JWT: {}", jwt);
            return jwt;
        } catch (JwtException e) {
            log.error("Error decoding JWT", e);
            throw e;
        }
    }
}

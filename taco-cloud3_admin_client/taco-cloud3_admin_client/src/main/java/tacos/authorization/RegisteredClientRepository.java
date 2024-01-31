package tacos.authorization;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;

import io.micrometer.common.lang.Nullable;

public interface RegisteredClientRepository {
	 @Nullable
	 RegisteredClient findById(String id);
	 @Nullable
	 RegisteredClient findByClientId(String clientId);
	}


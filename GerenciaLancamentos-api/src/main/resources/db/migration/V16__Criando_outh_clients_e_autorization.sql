CREATE TABLE oauth_client (
    client_id VARCHAR(255) PRIMARY KEY,
    client_secret VARCHAR(255) NOT NULL,
    access_token_validity_seconds INT NOT NULL DEFAULT 3600,
    refresh_token_validity_seconds INT NOT NULL DEFAULT 86400,
    auto_approve BOOLEAN NOT NULL DEFAULT FALSE,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    description VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_used_at TIMESTAMP
);

-- Grant types permitidos
CREATE TABLE oauth_client_grant_types (
    client_id VARCHAR(255) NOT NULL,
    grant_type VARCHAR(50) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES oauth_client(client_id) ON DELETE CASCADE,
    PRIMARY KEY (client_id, grant_type)
);

-- Scopes permitidos
CREATE TABLE oauth_client_scopes (
    client_id VARCHAR(255) NOT NULL,
    scope VARCHAR(50) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES oauth_client(client_id) ON DELETE CASCADE,
    PRIMARY KEY (client_id, scope)
);

-- Redirect URIs (para authorization_code flow)
CREATE TABLE oauth_client_redirect_uris (
    client_id VARCHAR(255) NOT NULL,
    redirect_uri VARCHAR(500) NOT NULL,
    FOREIGN KEY (client_id) REFERENCES oauth_client(client_id) ON DELETE CASCADE,
    PRIMARY KEY (client_id, redirect_uri)
);

-- Criar índices
CREATE INDEX idx_client_enabled ON oauth_client(enabled);
CREATE INDEX idx_client_last_used ON oauth_client(last_used_at);
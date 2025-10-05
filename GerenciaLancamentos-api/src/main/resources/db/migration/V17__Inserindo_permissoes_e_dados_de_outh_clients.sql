INSERT INTO oauth_client (client_id, client_secret, access_token_validity_seconds, 
                         refresh_token_validity_seconds, description, enabled)
VALUES 
-- Frontend Angular (Password Flow)
('angular', 
 '$2a$12$Xv0E.k7OvJEuNwn1eKCLV.YsLv5vW3YEqZZV7Hc5EKz3xVQE8L7Vy', -- secret: @ngul@r0
 3600, 
 86400,
 'Aplicação frontend Angular',
 true),

-- Mobile App (Password Flow)
('mobile', 
 '$2a$12$kP3Xv0E.k7OvJEuNwn1eKCLV.YsLv5vW3YEqZZV7Hc5EKz3xVQE8L',  -- secret: m0b1l3@pp
 7200, 
 604800, -- 7 dias
 'Aplicação mobile iOS/Android',
 true);

-- Grant types
INSERT INTO oauth_client_grant_types (client_id, grant_type)
VALUES 
('angular', 'password'),
('angular', 'refresh_token'),
('mobile', 'password'),
('mobile', 'refresh_token');

-- Scopes
INSERT INTO oauth_client_scopes (client_id, scope)
VALUES 
('angular', 'read'),
('angular', 'write'),
('mobile', 'read'),
('mobile', 'write');
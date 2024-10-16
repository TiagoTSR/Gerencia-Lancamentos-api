UPDATE `users` 
SET `password` = CASE `user_name`
    WHEN 'Tiago' THEN 'cad5108cc51323020a3e6d087364127e45f24d399878a1b4872218883d112a373df04571cc0f54ad'
    WHEN 'Usuario' THEN 'a01a06db8c0e27a17b71179785df17c80b47f528886bb9c8ccd4e1591451507ba0468aecca797ee9'
    WHEN 'Administrador' THEN 'bcbbf65b054f1d0814fa681727820c8bf0e84431389038d5b974f352d78d72bbec8e2a57e5fad43d'
END
WHERE `user_name` IN ('Tiago', 'Usuario', 'Administrador');

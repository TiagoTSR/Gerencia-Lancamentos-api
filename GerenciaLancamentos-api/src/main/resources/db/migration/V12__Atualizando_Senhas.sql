UPDATE `usuarios` 
SET `password` = CASE `user_name`
    WHEN 'Tiago' THEN '$2a$12$Vsl/8eZTcvoPhreYda010.Qurv0hG70WMZvm/.95kAMrPp1EwleVq'
    WHEN 'Usuario' THEN '$2a$12$LLYaGRwJhJnzK/LDHbofyukKE85f1xinyBdG5kEerYVHK40T45QoC'
    WHEN 'Administrador' THEN '$2a$12$oWBkQlkB0B54lunK/PtZ3.BBo9l7PsUn/pyromK9MkSKxIQPe0A0.'
END
WHERE `user_name` IN ('Tiago', 'Usuario', 'Administrador');

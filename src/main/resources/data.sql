INSERT INTO USERS (username, email, password, IS_ACTIVE)
VALUES ('admin', 'admin@gmail.com', '$2a$10$PRex3hFH8n.1sZByoZFDhuBvZga.npk2vK4ztvI0pyhCiEFaSTzCG', true);

INSERT INTO ROLES (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

INSERT INTO USER_ROLES (user_id, role_id)
VALUES (1, 1);
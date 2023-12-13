INSERT INTO USERS (username, email, password, IS_ACTIVE)
VALUES ('admin', 'admin@mail.me', '$2a$10$PRex3hFH8n.1sZByoZFDhuBvZga.npk2vK4ztvI0pyhCiEFaSTzCG',
        true),
       ('user', 'user@mail.me', '$2a$10$PRex3hFH8n.1sZByoZFDhuBvZga.npk2vK4ztvI0pyhCiEFaSTzCG',
        true);

INSERT INTO ROLES (name)
VALUES ('ROLE_USER'),
       ('ROLE_ADMIN');

INSERT INTO USER_ROLES (user_id, role_id)
VALUES (1, 2),
       (2, 1);
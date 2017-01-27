INSERT INTO users (id, first_name, last_name, email) VALUES ( '1', 'Potato', 'Development', 'sweetpotatodevelopment@gmail.com');
INSERT INTO groups (id, name, identity) VALUES (1, 'Default', 'i1d2e3n4t5i6t7y8');
INSERT INTO usersingroups (id, user_id, group_id, joined_at) VALUES (1, 1, 1, {ts '2012-09-17 18:47:52.69'});
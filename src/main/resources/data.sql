  INSERT INTO USERS (USERNAME, AGE, ENABLED, NAME, PASSWORD) VALUES
  ('admin', 15, true, 'Piotr', '$2a$10$hbxecwitQQ.dDT4JOFzQAulNySFwEpaFLw38jda6Td.Y/cOiRzDFu');
  
  INSERT INTO AUTHORITIES (AUTHORITY, username) VALUES
  ('ROLE_ADMIN', 'admin'),
  ('ROLE_USER', 'admin');
  
  
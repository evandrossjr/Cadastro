-- ================== CONTATOS ==================
INSERT INTO contato (id, nome_completo, email, email_adicional, telefone, telefone_adicional)
VALUES (1, 'Fabio Alex', 'fabio.alex@clinica.com', 'fabio.alex@outlook.com', '71991213095', '71991213098'),
       (2, 'Mariana Souza', 'mariana@clinica.com', 'mariana.souza@gmail.com', '71991345022', '71991887766'),
       (3, 'Paulo César', 'paulo@clinica.com', 'paulo.cesar@hotmail.com', '71991555522', '71991774455'),
       (4, 'Renata Lima', 'renata@clinica.com', 'renata.lima@gmail.com', '71991889977', '71991224477'),
       (5, 'Carlos Eduardo', 'carlos@clinica.com', 'cadu@gmail.com', '71991113344', '71991995533'),
       (6, 'Juliana Reis', 'juliana@clinica.com', 'juliana.reis@hotmail.com', '71991228844', '71991339955'),
       (7, 'Felipe Costa', 'felipe@clinica.com', 'felipe.costa@gmail.com', '71991447788', '71991889944'),
       (8, 'Tatiane Rocha', 'tatiane@clinica.com', 'tatiane.rocha@gmail.com', '71991775522', '71991337755'),
       (9, 'Bruno Henrique', 'bruno@clinica.com', 'bruno.henrique@icloud.com', '71991992233', '71991223344'),
       (10, 'Larissa Melo', 'larissa@clinica.com', 'larissa.melo@gmail.com', '71991112233', '71991998877'),
       (11, 'Thiago Ramos', 'thiago@clinica.com', 'thiago.ramos@gmail.com', '71991887766', '71991331122'),
       (12, 'Amanda Oliveira', 'amanda@clinica.com', 'amanda.oliveira@gmail.com', '71991772233', '71991669988'),
       (13, 'Lucas Martins', 'lucas@clinica.com', 'lucas.martins@gmail.com', '71991553322', '71991336655'),
       (14, 'Fernanda Alves', 'fernanda@clinica.com', 'fernanda.alves@gmail.com', '71991995522', '71991225544'),
       (15, 'Diego Silva', 'diego@clinica.com', 'diego.silva@gmail.com', '71991886633', '71991114455');

-- ================== CLIENTES ==================
INSERT INTO cliente (id, nome_completo, email, email_adicional, telefone, telefone_adicional, data_registro)
VALUES 
(1, 'Fabio Alex', 'fabio.alex@clinica.com', 'fabio.alex@outlook.com', '71991213095', '71991213098', '2025-01-25'),
(2, 'Mariana Souza', 'mariana@clinica.com', 'mariana.souza@gmail.com', '71991345022', '71991887766', '2025-02-12'),
(3, 'Paulo César', 'paulo@clinica.com', 'paulo.cesar@hotmail.com', '71991555522', '71991774455', '2025-03-18'),
(4, 'Renata Lima', 'renata@clinica.com', 'renata.lima@gmail.com', '71991889977', '71991224477', '2025-03-21'),
(5, 'Carlos Eduardo', 'carlos@clinica.com', 'cadu@gmail.com', '71991113344', '71991995533', '2025-03-25'),
(6, 'Juliana Reis', 'juliana@clinica.com', 'juliana.reis@hotmail.com', '71991228844', '71991339955', '2025-04-03'),
(7, 'Felipe Costa', 'felipe@clinica.com', 'felipe.costa@gmail.com', '71991447788', '71991889944', '2025-04-10'),
(8, 'Tatiane Rocha', 'tatiane@clinica.com', 'tatiane.rocha@gmail.com', '71991775522', '71991337755', '2025-04-18'),
(9, 'Bruno Henrique', 'bruno@clinica.com', 'bruno.henrique@icloud.com', '71991992233', '71991223344', '2025-05-02'),
(10, 'Larissa Melo', 'larissa@clinica.com', 'larissa.melo@gmail.com', '71991112233', '71991998877', '2025-05-09'),
(11, 'Thiago Ramos', 'thiago@clinica.com', 'thiago.ramos@gmail.com', '71991887766', '71991331122', '2025-05-17'),
(12, 'Amanda Oliveira', 'amanda@clinica.com', 'amanda.oliveira@gmail.com', '71991772233', '71991669988', '2025-05-28'),
(13, 'Lucas Martins', 'lucas@clinica.com', 'lucas.martins@gmail.com', '71991553322', '71991336655', '2025-06-05'),
(14, 'Fernanda Alves', 'fernanda@clinica.com', 'fernanda.alves@gmail.com', '71991995522', '71991225544', '2025-06-14'),
(15, 'Diego Silva', 'diego@clinica.com', 'diego.silva@gmail.com', '71991886633', '71991114455', '2025-06-21');

-- ================== CLIENTE_CONTATO (TABELA DE JUNÇÃO) ==================
INSERT INTO cliente_contato (cliente_id, contato_id) VALUES
(1, 1), (1, 2),
(2, 3), (2, 4),
(3, 5), (3, 6),
(4, 2), (4, 7),
(5, 5), (5, 8),
(6, 9),
(7, 10),
(8, 6), (8, 12),
(9, 2), (9, 13),
(10, 14),
(11, 11),
(12, 15),
(13, 7), (13, 10),
(14, 8), (14, 12),
(15, 13), (15, 15);

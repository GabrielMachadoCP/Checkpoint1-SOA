INSERT INTO cliente (nome, email, documento)
VALUES ('Ana Silva', 'ana.silva@email.com', '123.456.789-00');
INSERT INTO cliente (nome, email, documento)
VALUES ('Carlos Souza', 'carlos.souza@email.com', '987.654.321-00');
INSERT INTO produto (nome, preco, categoria, descricao, ativo)
VALUES ('Notebook Dell', 3500.00, 'Informática', 'Notebook básico para estudos', TRUE);
INSERT INTO produto (nome, preco, categoria, descricao, ativo)
VALUES ('Mouse sem fio', 120.00, 'Acessórios', 'Mouse sem fio USB', TRUE);
INSERT INTO carrinho (cliente_id) VALUES (1);
INSERT INTO item_carrinho (carrinho_id, produto_id, quantidade, preco_unitario)
VALUES (1, 1, 1, 3500.00);
INSERT INTO item_carrinho (carrinho_id, produto_id, quantidade, preco_unitario)
VALUES (1, 2, 2, 120.00);
INSERT INTO pedido (cliente_id, total, status)
VALUES (1, 3740.00, 'CRIADO');
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario)
VALUES (1, 1, 1, 3500.00);
INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario)
VALUES (1, 2, 2, 120.00);
INSERT INTO pagamento (pedido_id, valor, status, metodo)
VALUES (1, 3740.00, 'APROVADO', 'PIX');
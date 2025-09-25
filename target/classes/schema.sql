
CREATE SCHEMA IF NOT EXISTS byteshop_db;
SET SCHEMA byteshop_db;

CREATE TABLE cliente (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,  
  nome         VARCHAR(150)      NOT NULL,         
  email        VARCHAR(150)      NOT NULL,         
  documento    VARCHAR(30)       NOT NULL,         
  UNIQUE (email)                                   
);
CREATE TABLE carrinho (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  cliente_id   BIGINT            NOT NULL,         
  CONSTRAINT fk_carrinho_cliente
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
    ON DELETE CASCADE,                             
  UNIQUE (cliente_id)                              
);
CREATE TABLE produto (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  nome          VARCHAR(120)     NOT NULL,         
  preco         DECIMAL(10,2)    NOT NULL,         
  categoria     VARCHAR(60)      NULL,             
  descricao     TEXT             NULL,             
  ativo         BOOLEAN          NOT NULL DEFAULT TRUE  
);
CREATE TABLE item_carrinho (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  carrinho_id    BIGINT         NOT NULL,
  produto_id     BIGINT         NOT NULL,
  quantidade     INT            NOT NULL,          
  preco_unitario DECIMAL(10,2)  NOT NULL,          

  CONSTRAINT fk_itemcarrinho_carrinho
    FOREIGN KEY (carrinho_id) REFERENCES carrinho(id)
    ON DELETE CASCADE,                             

  CONSTRAINT fk_itemcarrinho_produto
    FOREIGN KEY (produto_id) REFERENCES produto(id)
    ON DELETE RESTRICT                              

  
  
);
CREATE TABLE pedido (
  id           BIGINT AUTO_INCREMENT PRIMARY KEY,
  cliente_id   BIGINT           NOT NULL,
  total        DECIMAL(10,2)    NOT NULL,          
  status       VARCHAR(30)      NOT NULL,          

  CONSTRAINT fk_pedido_cliente
    FOREIGN KEY (cliente_id) REFERENCES cliente(id)
    ON DELETE RESTRICT                               
);
CREATE TABLE item_pedido (
  id             BIGINT AUTO_INCREMENT PRIMARY KEY,
  pedido_id      BIGINT          NOT NULL,
  produto_id     BIGINT          NOT NULL,
  quantidade     INT             NOT NULL,
  preco_unitario DECIMAL(10,2)   NOT NULL,

  CONSTRAINT fk_itempedido_pedido
    FOREIGN KEY (pedido_id) REFERENCES pedido(id)
    ON DELETE CASCADE,                               

  CONSTRAINT fk_itempedido_produto
    FOREIGN KEY (produto_id) REFERENCES produto(id)
    ON DELETE RESTRICT
);
CREATE TABLE pagamento (
  id         BIGINT AUTO_INCREMENT PRIMARY KEY,
  pedido_id  BIGINT         NOT NULL,
  valor      DECIMAL(10,2)  NOT NULL,
  status     VARCHAR(30)    NOT NULL,              
  metodo     VARCHAR(40)    NOT NULL,              

  CONSTRAINT fk_pagamento_pedido
    FOREIGN KEY (pedido_id) REFERENCES pedido(id)
    ON DELETE CASCADE,                              

  UNIQUE (pedido_id)                                
);
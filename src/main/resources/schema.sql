
CREATE TABLE public.imagem (
   id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
   nome varchar NOT NULL,
   descricao varchar NOT NULL,
   tipoconteudo varchar NOT NULL,
   caminho varchar NOT NULL,
   tamanho int4 NOT NULL,
   CONSTRAINT imagem_pk PRIMARY KEY (id)
);

CREATE TABLE public.produto (
    id int4 GENERATED ALWAYS AS IDENTITY( INCREMENT BY 1 MINVALUE 1 MAXVALUE 2147483647 START 1 CACHE 1 NO CYCLE) NOT NULL,
    valor numeric NOT NULL,
    quantidade int4 NOT NULL,
    categoria varchar NOT NULL,
    nome varchar NOT NULL,
    descricao varchar NOT NULL,
    CONSTRAINT produto_pk PRIMARY KEY (id)
);

CREATE TABLE public.pedido (
   id int4 GENERATED ALWAYS AS IDENTITY NOT NULL,
   status varchar NOT NULL,
   valor numeric NOT NULL,
   datacriacao date NOT NULL,
   datafinalizacao date NOT NULL,
   idcliente int4 NOT NULL,
   CONSTRAINT pedido_pk PRIMARY KEY (id),
   CONSTRAINT pedido_cliente_fk FOREIGN KEY (idcliente) REFERENCES public.cliente(id)
);

CREATE TABLE public.pedido_produto (
   idpedido int4 NOT NULL,
   idproduto int4 NOT NULL,
   quantidade int4 NOT NULL,
   valorunitario numeric NOT NULL,
   CONSTRAINT pedido_produto_pk PRIMARY KEY (idpedido, idproduto),
   CONSTRAINT pedido_produto_fk_1 FOREIGN KEY (idpedido) REFERENCES public.pedido(id),
   CONSTRAINT pedido_produto_fk_2 FOREIGN KEY (idproduto) REFERENCES public.produto(id)
);


CREATE TABLE public.produto_imagem (
   idproduto int4 NOT NULL,
   idimagem int4 NOT NULL,
   CONSTRAINT produto_imagem_pk PRIMARY KEY (idproduto, idimagem),
   CONSTRAINT imagem_produto_fk_1 FOREIGN KEY (idproduto) REFERENCES public.produto(id),
   CONSTRAINT imagem_produto_fk_2 FOREIGN KEY (idimagem) REFERENCES public.imagem(id)
);

create table cliente (
                         dt_cadastro date,
                         dth_atualizacao timestamp(6),
                         sq_cliente UUID not null,
                         cpf_cnpj varchar(255) unique,
                         ds_cliente TEXT,
                         email varchar(255),
                         nm_cliente varchar(255),
                         telefone varchar(255),
                         constraint PK_cliente primary key (sq_cliente));

create table item_estoque (
                              qt_estoque_atual integer,
                              qt_estoque_minimo integer,
                              dth_atualizacao timestamp(6),
                              vl_preco_unitario bigint,
                              sq_item_estoque uuid not null,
                              ds_item_estoque TEXT,
                              nm_item_estoque varchar(255),
                              tp_item varchar(255) check ((TP_ITEM in ('PECA', 'INSUMO')) and (tp_item in ('PECA','INSUMO'))),
                              constraint PK_item_estoque primary key (sq_item_estoque));

create table movimento_estoque (
                                   qt_mov integer,
                                   dth_movimento timestamp(6),
                                   sq_item_estoque uuid,
                                   sq_item_peca_os uuid,
                                   sq_movimento_estoque UUID not null,
                                   ds_movimento TEXT,
                                   nm_tipo_movimento varchar(255),
                                   constraint PK_movimento_estoque primary key (sq_movimento_estoque)
);

create table orcamento (
                           dth_geracao timestamp(6),
                           vl_total bigint,
                           sq_orcamento UUID not null,
                           sq_ordem_servico UUID unique,
                           nm_situacao varchar(255) check ((nm_situacao in ('GERADO', 'ENVIADO', 'APROVADO', 'REJEITADO')) and (nm_situacao in ('GERADO','ENVIADO','APROVADO','REJEITADO'))),
                           constraint PK_orcamento primary key (sq_orcamento)
);

create table ordem_servico (
                               dth_abertura timestamp(6),
                               dth_fechamento timestamp(6),
                               vl_orcamento_total bigint,
                               vl_total_final bigint,
                               sq_cliente UUID,
                               sq_ordem_servico UUID not null,
                               sq_veiculo UUID,
                               ds_os TEXT,
                               nm_situacao varchar(255) check ((nm_situacao in ('RECEBIDA', 'EM_DIAGNOSTICO', 'AGUARDANDO_APROVACAO', 'EM_EXECUCAO', 'FINALIZADA', 'ENTREGUE')) and (nm_situacao in ('RECEBIDA','EM_DIAGNOSTICO','AGUARDANDO_APROVACAO','EM_EXECUCAO','FINALIZADA','ENTREGUE'))),
                               constraint PK_ordem_servico primary key (sq_ordem_servico)
);

create table os_item (
                         qt_itens integer,
                         vl_preco_total bigint,
                         vl_preco_unitario bigint,
                         sq_item_estoque uuid,
                         sq_ordem_servico UUID,
                         sq_os_item uuid not null,
                         constraint PK_os_item primary key (sq_os_item)
);

create table os_servico (
                            qt_horas integer,
                            vl_preco_total bigint,
                            vl_preco_unitario bigint,
                            sq_ordem_servico UUID,
                            sq_os_servico UUID not null,
                            sq_servico UUID,
                            constraint PK_os_servico primary key (sq_os_servico)
);

create table servico (
                         dth_atualizacao timestamp(6),
                         vl_preco_base bigint,
                         sq_servico UUID not null,
                         ds_servico TEXT,
                         nm_servico varchar(255),
                         constraint PK_servico primary key (sq_servico)
);

create table veiculo (
                         ano integer,
                         dth_cadastro timestamp(6),
                         sq_cliente UUID,
                         sq_veiculo UUID not null,
                         ds_veiculo TEXT,
                         marca varchar(255),
                         modelo varchar(255),
                         nm_veiculo varchar(255),
                         placa varchar(255) unique,
                         constraint PK_veiculo primary key (sq_veiculo)
);

alter table if exists movimento_estoque
    add constraint FK_MOVIMENTO_ITEMESTOQUE
    foreign key (sq_item_estoque)
    references item_estoque;

alter table if exists movimento_estoque
    add constraint FK_MOVIMENTO_ITEMPECAOS
    foreign key (sq_item_peca_os)
    references os_item;

alter table if exists orcamento
    add constraint FK_ORCAMENTO_ORDEMSERVICO
    foreign key (sq_ordem_servico)
    references ordem_servico;

alter table if exists ordem_servico
    add constraint FK_ORDEMSERVICO_CLIENTE
    foreign key (sq_cliente)
    references cliente;

alter table if exists ordem_servico
    add constraint FK_ORDEMSERVICO_VEICULO
    foreign key (sq_veiculo)
    references veiculo;

alter table if exists os_item
    add constraint FK_ITEMPECA_ITEMESTOQUE
    foreign key (sq_item_estoque)
    references item_estoque;

alter table if exists os_item
    add constraint FK_ITEMPECA_ORDEMSERVICO
    foreign key (sq_ordem_servico)
    references ordem_servico;

alter table if exists os_servico
    add constraint FK_ITEMSERVICO_ORDEM
    foreign key (sq_ordem_servico)
    references ordem_servico;

alter table if exists os_servico
    add constraint FK_ITEMSERVICO_SERVICO
    foreign key (sq_servico)
    references servico;

alter table if exists veiculo
    add constraint FK_VEICULO_CLIENTE
    foreign key (sq_cliente)
    references cliente;


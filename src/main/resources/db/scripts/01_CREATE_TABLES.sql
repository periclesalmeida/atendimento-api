
SET search_path = admatm;

/*==============================================================*/
/* Table: atm_atendimento                                       */
/*==============================================================*/
create table atm_atendimento (
   seq_atendimento      INT4                 not null,
   num_atendimento      INT4                 not null,
   dth_cadastro         DATE                 not null,
   dth_apresentacao     DATE                 null,
   dth_chamada          DATE                 null,
   ind_prioridade       BOOL                 not null,
   seq_servico          INT4                 not null,
   seq_localizacao      INT4                 null,
   seq_usuario          INT4                 null,
   constraint pk_atendimento primary key (seq_atendimento)
);

comment on table atm_atendimento is
   'Dados do atendimento.';

comment on column atm_atendimento.seq_atendimento is
   'Sequencial númérico identificador do atendimento.';

comment on column atm_atendimento.num_atendimento is
   'Nùmero do atendimento.';

comment on column atm_atendimento.dth_cadastro is
   'Data hora de cadastro.';

comment on column atm_atendimento.dth_apresentacao is
   'Data hora de apresentação.';

comment on column atm_atendimento.dth_chamada is
   'Data hora da chamada.';

comment on column atm_atendimento.ind_prioridade is
   'Indicador de prioridade.';

comment on column atm_atendimento.seq_servico is
   'Sequencial numérico identificador do serviço.';

comment on column atm_atendimento.seq_localizacao is
   'Sequencial numérico identificador da localização.';

comment on column atm_atendimento.seq_usuario is
   'Sequencial numérido identificador do usuário.';


/*==============================================================*/
/* Table: atm_localizacao                                       */
/*==============================================================*/
create table atm_localizacao (
   seq_localizacao      INT4                 not null,
   seq_tipo_localizacao INT4                 not null,
   dsc_localizacao      VARCHAR(50)          not null,
   ind_ativo            BOOL                 not null,
   constraint pk_localizacao primary key (seq_localizacao)
);

comment on table atm_localizacao is
   'Dados da localização.';

comment on column atm_localizacao.seq_localizacao is
   'Sequencial numérico identificador da localização.';

comment on column atm_localizacao.seq_tipo_localizacao is
   'Sequencial numérico identificador do tipo localização.';

comment on column atm_localizacao.dsc_localizacao is
   'Descrição da localização';

comment on column atm_localizacao.ind_ativo is
   'Indicador de ativo.';


/*==============================================================*/
/* Table: atm_localizacao_servico                               */
/*==============================================================*/
create table atm_localizacao_servico (
   seq_localizacao      INT4                 not null,
   seq_servico          INT4                 not null,
   constraint pk_localizacaoservico primary key (seq_localizacao, seq_servico)
);

comment on table atm_localizacao_servico is
   'Dados da localização do serviço.';

comment on column atm_localizacao_servico.seq_localizacao is
   'Sequencial numérico identificador da localização.';

comment on column atm_localizacao_servico.seq_servico is
   'Sequencial numérico identificador do serviço.';


/*==============================================================*/
/* Table: atm_permissao                                         */
/*==============================================================*/
create table atm_permissao (
   cod_permissao        VARCHAR(40)          not null,
   dsc_permissao        VARCHAR(100)         not null,
   constraint pk_permissao primary key (cod_permissao)
);

comment on table atm_permissao is
   'Permissões dos usuários.';

comment on column atm_permissao.cod_permissao is
   'Código da permissão.';

comment on column atm_permissao.dsc_permissao is
   'Descrição da permissão.';



/*==============================================================*/
/* Table: atm_servico                                           */
/*==============================================================*/
create table atm_servico (
   seq_servico          INT4                 not null,
   dsc_servico          VARCHAR(100)         not null,
   dsc_sigla            VARCHAR(10)          not null,
   tip_cor              CHAR(1)              not null,
   num_atendimento_atual INT4                 not null,
   ind_ativo            BOOL                 not null,
   constraint pk_servico primary key (seq_servico)
);

comment on table atm_servico is
   'Dados do serviço.';

comment on column atm_servico.seq_servico is
   'Sequencial numérico identificador do serviço.';

comment on column atm_servico.dsc_servico is
   'Descrição do serviço.';

comment on column atm_servico.dsc_sigla is
   'Descrição da sigla do serviço.';

comment on column atm_servico.tip_cor is
   'Tipo da cor do serviço.';

comment on column atm_servico.num_atendimento_atual is
   'Número de atendimento atual.';

comment on column atm_servico.ind_ativo is
   'Indicador de ativo.';


/*==============================================================*/
/* Table: atm_tipo_localizacao                                  */
/*==============================================================*/
create table atm_tipo_localizacao (
   seq_tipo_localizacao SERIAL               not null,
   dsc_tipo_localizacao VARCHAR(50)          not null,
   constraint pk_tipolocalizacao primary key (seq_tipo_localizacao)
);

comment on table atm_tipo_localizacao is
   'Dados do tipo da localização.';

comment on column atm_tipo_localizacao.seq_tipo_localizacao is
   'Sequencial numérico identificador do tipo localização.';

comment on column atm_tipo_localizacao.dsc_tipo_localizacao is
   'Descrição do tipo localização.';

/*==============================================================*/
/* Table: atm_usuario                                           */
/*==============================================================*/
create table atm_usuario (
   seq_usuario          SERIAL               not null,
   nom_login            VARCHAR(50)          not null,
   dsc_senha            VARCHAR(100)         not null,
   ind_ativo            BOOL                 not null,
   constraint pk_usuario primary key (seq_usuario)
);

comment on table atm_usuario is
   'Dados dos usuários.';

comment on column atm_usuario.seq_usuario is
   'Sequencial numérido identificador do usuário.';

comment on column atm_usuario.nom_login is
   'Nomo do login do usuário.';

comment on column atm_usuario.dsc_senha is
   'Descrição da senha do usuário.';

comment on column atm_usuario.ind_ativo is
   'Indicador de ativo do usuáiro.';


/*==============================================================*/
/* Table: atm_usuario_permissao                                 */
/*==============================================================*/
create table atm_usuario_permissao (
   seq_usuario          INT4                 not null,
   cod_permissao        VARCHAR(40)          not null,
   constraint pk_permissaousuario primary key (seq_usuario, cod_permissao)
);

comment on table atm_usuario_permissao is
   'Permissões do usuário.';

comment on column atm_usuario_permissao.seq_usuario is
   'Sequencial numérido identificador do usuário.';

comment on column atm_usuario_permissao.cod_permissao is
   'Código da permissão.';


alter table atm_atendimento
   add constraint fk_atendimento_localizacao foreign key (seq_localizacao)
references atm_localizacao (seq_localizacao);

alter table atm_atendimento
   add constraint fk_atendimento_servico foreign key (seq_servico)
references atm_servico (seq_servico);

alter table atm_atendimento
   add constraint fk_atendimento_usuario foreign key (seq_usuario)
references atm_usuario (seq_usuario);

alter table atm_localizacao
   add constraint fk_localizacao_tipolocalizacao foreign key (seq_tipo_localizacao)
references atm_tipo_localizacao (seq_tipo_localizacao);

alter table atm_localizacao_servico
   add constraint fk_localizacaoservico_localizacao foreign key (seq_localizacao)
references atm_localizacao (seq_localizacao);

alter table atm_localizacao_servico
   add constraint fk_localizacaoservico_servico foreign key (seq_servico)
references atm_servico (seq_servico);

alter table atm_usuario_permissao
   add constraint fk_permissaousuario_permissao foreign key (cod_permissao)
references atm_permissao (cod_permissao);

alter table atm_usuario_permissao
   add constraint fk_permissaousuario_usuario foreign key (seq_usuario)
references atm_usuario (seq_usuario);

SET search_path = public;
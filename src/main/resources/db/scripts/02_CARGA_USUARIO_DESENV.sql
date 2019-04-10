INSERT INTO admatm.atm_usuario (nom_login, dsc_senha, ind_ativo) VALUES ('admin', MD5('1'), true);

INSERT INTO admatm.atm_usuario_permissao (seq_usuario, cod_permissao)
SELECT (SELECT seq_usuario FROM admatm.atm_usuario WHERE nom_login = 'admin'), cod_permissao
  FROM admatm.atm_permissao




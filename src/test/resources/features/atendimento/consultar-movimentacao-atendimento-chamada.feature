# language: pt
@atendimento

Funcionalidade: Consultar Movimentação de Atendimento Chamado
    O sistema permite que o usuário consulte a movimentação diária de atendimento chamado

  Cenario de Fundo:
    Dado que o usuário "pericles" possui permissao de acesso a funcionalidade
    E que exitem tipo localização cadastrados
      | id             | descricao            |
      | 001            | SALA                 |
      | 002            | GUICHÊ               |
      | 003            | MESA                 |
    E que exitem serviços cadastrados
      | id     | descricao     | sigla | cor    | atendimento atual |
      | 001    | FINANCIAMENTO | FIN   | AZUL   |       5           |
      | 002    | NEGOCIAÇÃO    | NEG   | AMARELO|       0           |
      | 003    | MATRÍCULA     | MAT   | VERDE  |       3           |
      | 004    | BIBLIOTECA    | BIB   | AMARELO|       6           |
   E que exitem localizações cadastradas
        | id     | descricao     | tipo  | servicos    | ativo |
        | 001    | A             | 001   | 001,002     | SIM   |
        | 002    | AB            | 001   | 001,002,003 | SIM   |
        | 003    | 1             | 002   | 003         | SIM   |
   E que existem atendimentos cadastradas
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
       | 002 | 2                  | DATA_ATUAL          |                               | DATA_ATUAL                 | 002         | 003     | pericles | NÃO        |
       | 001 | 1                  | DATA_ATUAL          |   DATA_ATUAL                  | DATA_ATUAL                 | 001         | 001     | pericles | SIM        |
       | 003 | 3                  | DATA_ATUAL          |   DATA_ATUAL_MAIS_1_MINUTO   | DATA_ATUAL_MAIS_1_MINUTO   | 001         | 001     | pericles | NÃO        |
       | 004 | 4                  | DATA_ONTEM          |                               | DATA_ATUAL_MENOS_1_MINUTO  | 001         | 001     | pericles | NÃO        |
       | 005 | 5                  | DATA_ONTEM          |   DATA_ONTEM                  | DATA_ONTEM                 | 001         | 002     | pericles | NÃO        |
       | 006 | 5                  | DATA_ONTEM          |   DATA_ATUAL                  | DATA_ATUAL_MAIS_1_MINUTO   | 001         | 002     | pericles | NÃO        |

  Cenário: Dado que foram informados dados válidos com atendimento apresentado e não apresentado
    Quando foi informado os serviços "001,003" para consulta de atendimento chamado
    Então o sistema exibe os atendimetos apresentados:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
       | 003 | 3                  | DATA_ATUAL          |   DATA_ATUAL_MAIS_1_MINUTO    | DATA_ATUAL_MAIS_1_MINUTO   | 001         | 001     | pericles | NÃO        |
       | 001 | 1                  | DATA_ATUAL          |   DATA_ATUAL                  | DATA_ATUAL                 | 001         | 001     | pericles | SIM        |
    E os atendimentos não apresentados:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
       | 004 | 4                  | DATA_ONTEM          |                               | DATA_ATUAL_MENOS_1_MINUTO  | 001         | 001     | pericles | NÃO        |
       | 002 | 2                  | DATA_ATUAL          |                               | DATA_ATUAL                 | 002         | 003     | pericles | NÃO        |
    E o próximo atendimento que deveria ser apresentado:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
       | 004 | 4                  | DATA_ONTEM          |                               | DATA_ATUAL_MENOS_1_MINUTO  | 001         | 001     | pericles | NÃO        |
    E o último atendimento apresentado:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
       | 003 | 3                  | DATA_ATUAL          |   DATA_ATUAL_MAIS_1_MINUTO    | DATA_ATUAL_MAIS_1_MINUTO   | 001         | 001     | pericles | NÃO        |

  Cenário: Dado que foram informados dados sem atendimento atendimento apresentado
    Quando foi informado os serviços "002" para consulta de atendimento chamado
    Então o sistema exibe os atendimetos apresentados:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
       | 006 | 5                  | DATA_ONTEM          |   DATA_ATUAL                  | DATA_ATUAL_MAIS_1_MINUTO   | 001         | 002     | pericles | NÃO        |
    E os atendimentos não apresentados:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
    E o próximo atendimento que deveria ser apresentado:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
    E o último atendimento apresentado:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
       | 006 | 5                  | DATA_ONTEM          |   DATA_ATUAL                  | DATA_ATUAL_MAIS_1_MINUTO   | 001         | 002     | pericles | NÃO        |

  Cenário: Dado que foram informados dados sem atendimento
    Quando foi informado os serviços "004" para consulta de atendimento chamado
    Então o sistema exibe os atendimetos apresentados:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
    E os atendimentos não apresentados:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
    E o próximo atendimento que deveria ser apresentado:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
    E o último atendimento apresentado:
       | id  | número atendimento | data cadastro       | data apresentacao             | data chamada               | localizacao | servico | usuario  | prioridade |
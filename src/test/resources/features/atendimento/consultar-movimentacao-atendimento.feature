# language: pt
@atendimento

Funcionalidade: Consultar Movimentação de Atendimento
    O sistema permite que o usuário consulte a movimentação diária de uma lista de serviços

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
   E que exitem localizações cadastradas
        | id     | descricao     | tipo  | servicos    | ativo |
        | 001    | A             | 001   | 001,002     | SIM   |
        | 002    | AB            | 001   | 001,002,003 | SIM   |
        | 003    | 1             | 002   | 003         | SIM   |
   E que existem atendimentos cadastradas
       | id  | número atendimento | data cadastro       | data apresentacao | data chamada       | localizacao | servico | usuario  | prioridade |
       | 002 | 2                  | DATA_ATUAL          |                   | DATA_ATUAL          | 002         | 003     | pericles | NÃO        |
       | 001 | 1                  | DATA_ATUAL          |                   | DATA_ATUAL          | 001         | 001     | pericles | SIM        |
       | 003 | 3                  | DATA_ATUAL          |                   |                    | 001         | 001     |          | NÃO        |
       | 004 | 4                  | DATA_ONTEM          |                   |                    | 001         | 001     |          | NÃO        |
       | 005 | 5                  | DATA_ONTEM          |                   |                    | 001         | 002     |          | NÃO        |

  Cenário: Dado que foram informados dados válidos
    Quando foi informado os serviços "001,003"
    Então o sistema exibe os atendimetos em espera:
           | id  | número atendimento | data cadastro       | data apresentacao | data chamada       | localizacao | servico | usuario  | prioridade |
           | 003 | 3                  | DATA_ATUAL           |                   |                    | 001         | 001     |          | NÃO        |
    E os atendimentos realizados:
           | id  | número atendimento | data cadastro       | data apresentacao | data chamada       | localizacao | servico | usuario  | prioridade |
           | 001 | 1                  | DATA_ATUAL           |                   | DATA_ATUAL          | 001         | 001     | pericles | SIM        |
           | 002 | 2                  | DATA_ATUAL           |                   | DATA_ATUAL          | 002         | 003     | pericles | NÃO        |

  Cenário: Dado que foram informados dados sem atendimento acadastrado
    Quando foi informado os serviços "002"
    Então o sistema exibe os atendimetos em espera:
           | id  | número atendimento | data cadastro       | data apresentacao | data chamada       | localizacao | servico | usuario  | prioridade |
    E os atendimentos realizados:
           | id  | número atendimento | data cadastro       | data apresentacao | data chamada       | localizacao | servico | usuario  | prioridade |
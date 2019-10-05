# language: pt
@atendimento

Funcionalidade: Chamar Próximo Atendimento para a Localização
    O sistema permite que o usuário chame o próximo atendimento para a localização

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
       | id  | número atendimento | data cadastro              | data apresentacao | data chamada       | localizacao | servico | usuario  | prioridade |
       | 002 | 2                  | DATA_ATUAL                 |                   | DATA_ATUAL         | 002         | 003     | pericles | NÃO        |
       | 001 | 1                  | DATA_ATUAL                 |                   | DATA_ATUAL         | 001         | 001     | pericles | SIM        |
       | 003 | 3                  | DATA_ATUAL_MENOS_1_MINUTO  |                   |                    |             | 001     |          | NÃO        |
       | 004 | 4                  | DATA_ONTEM                 |                   |                    |             | 001     |          | NÃO        |
       | 005 | 5                  | DATA_ONTEM                 |                   |                    |             | 002     |          | NÃO        |

  Cenário: Dado que foram informado localização com atendimento pendente
    Quando foi informado a localização "001"
    Então deveria retornar sucesso
    E os detalhes do Atendimento são:
     | id   | número atendimento | data cadastro              | data apresentacao | data chamada       | localizacao | usuario  | prioridade | servico | 
     | 003  | 3                  | DATA_ATUAL_MENOS_1_MINUTO  |                   | DATA_ATUAL         |   001       |          | NÃO        |   001   |

  Cenário: Dado que foram informado localização sem atendimento pendente
    Quando foi informado a localização "003"
    Então deveria retornar erro
    E a mensagem "Não existe atendimento na fila"

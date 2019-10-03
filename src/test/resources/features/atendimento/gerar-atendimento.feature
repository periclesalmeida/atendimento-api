# language: pt
@atendimento

Funcionalidade: Gerar Atendimento
    O sistema permite que o gere um atendimento para um seviço

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
       | 001 | 1                  | 01/01/2019 05:50:35 |                   | 01/01/2019 05:51:01| 001         | 001     | pericles | SIM        |
       | 002 | 2                  | 01/01/2019 06:50:35 |                   | 01/01/2019 07:50:59| 002         | 003     | pericles | NÃO        |
       | 003 | 2                  | 01/01/2019 05:55:00 |                   |                    | 001         | 001     |          | NÃO        |

  Cenário: Dado que foram informados dados válido
    Quando foi informado o serviço "002"
    Então deveria retornar objecto criado com sucesso
    E os detalhes do Atendimento são:
       | número atendimento | data cadastro       | data apresentacao | data chamada       | localizacao | servico | usuario  | prioridade |
       | 1                  | DATA_ATUAL          |                   |                    |             | 002     |          | NÃO        |
# language: pt
@localizacao

Funcionalidade: Consultar Localização
    O sistema permite que o usuário consulte as localizações cadastradas

  Cenario de Fundo:
    Dado que o usuário "pericles" possui permissao de acesso a funcionalidade
    E que exitem tipo localização cadastrados
      | id             | descricao            |
      | 001            | SALA                 |
      | 002            | GUICHÊ               |
      | 003            | MESA                 |
    E que exitem serviços cadastrados
      | id     | descricao     | sigla | cor    |
      | 001    | FINANCIAMENTO | FIN   | AZUL   |
      | 002    | NEGOCIAÇÃO    | NEG   | AMARELO|
      | 003    | MATRÍCULA     | MAT   | VERDE  |
   E que exitem localizações cadastradas
        | id     | descricao     | tipo  | servicos    | ativo |
        | 001    | A             | 001   | 001,002     | SIM   |
        | 002    | AB            | 001   | 001,002,003 | SIM   |
        | 003    | 1             | 002   | 003         | SIM   |

  Cenário: Informando dados existentes
    Quando foi informada a descricao "AB" e o tipo "001"
    Então o sistema exibe:
        | id     | descricao     | tipo  | servicos    | ativo |
        | 002    | AB            | 001   | 001,002,003 | SIM   |

  Cenário: Informando dados com mais de uma localização cadastrado
    Quando foi informada a descricao "A" e o tipo "001"
    Então o sistema exibe:
        | id     | descricao     | tipo  | servicos    | ativo |
        | 001    | A             | 001   | 001,002     | SIM   |
        | 002    | AB            | 001   | 001,002,003 | SIM   |

  Cenário: Informando dados que não existem
    Quando foi informada a descricao "A" e o tipo "003"
    Então o sistema exibe:
        | id     | descricao     | tipo  | servicos    | ativo |

  Cenário: Sem informar parâmetros
    Quando foi informada a descricao "" e o tipo ""
    Então o sistema exibe:
        | id     | descricao     | tipo  | servicos    | ativo |
        | 001    | A             | 001   | 001,002     | SIM   |
        | 002    | AB            | 001   | 001,002,003 | SIM   |
        | 003    | 1             | 002   | 003         | SIM   |
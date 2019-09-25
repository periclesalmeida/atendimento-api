# language: pt
@servico

Funcionalidade: Consultar Serviço
    O sistema permite que o usuário consulte os serviços cadastradas

  Cenario de Fundo:
    Dado que o usuário "pericles" possui permissao de acesso a funcionalidade
    E que exitem serviços cadastrados
      | id     | descricao     | sigla | cor    |
      | 001    | FINANCIAMENTO | FIN   | AZUL   |
      | 002    | NEGOCIAÇÃO    | NEG   | AMARELO|
      | 003    | MATRÍCULA     | MAT   | VERDE  |

  Cenário: Informando dados existentes
    Quando foi informada a descricao "NANCIA", a sigla "FI" e a cor "AZUL"
    Então o sistema exibe:
      | id     | descricao     | sigla | cor    |
      | 001    | FINANCIAMENTO | FIN   | AZUL   |

  Cenário: Informando dados com mais de um serviço cadastrado
    Quando foi informada a descricao "CI", a sigla "N" e a cor ""
    Então o sistema exibe:
      | id     | descricao     | sigla | cor    |
      | 001    | FINANCIAMENTO | FIN   | AZUL   |
      | 002    | NEGOCIAÇÃO    | NEG   | AMARELO|

  Cenário: Informando dados que não existem
    Quando foi informada a descricao "FIX", a sigla "N" e a cor ""
    Então o sistema exibe:
      | id     | descricao     | sigla | cor    |

  Cenário: Sem informar parâmetros
    Quando foi informada a descricao "", a sigla "" e a cor ""
    Então o sistema exibe:
      | id     | descricao     | sigla | cor    |
      | 001    | FINANCIAMENTO | FIN   | AZUL   |
      | 002    | NEGOCIAÇÃO    | NEG   | AMARELO|
      | 003    | MATRÍCULA     | MAT   | VERDE  |






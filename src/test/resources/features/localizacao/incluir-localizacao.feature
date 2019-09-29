# language: pt
@localizacao

Funcionalidade: Incluir Localização
    O sistema permite que o usuário inclua uma localização

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

  Cenário: Dado que foi informado dados válidos
    Quando foi informaro a descrição "A", tipo "002" e os serviços "001,002,003"
    Então deveria retornar objecto criado com sucesso
    E os detalhes do Serviço são:
      | descricao     | tipo  | servicos    | ativo |
      | A             | 002   | 001,002,003 | SIM   |

  Cenário: Dado que foi informado localização cadastrada com a descrição e tipo informados
    Quando foi informaro a descrição "1", tipo "002" e os serviços "001,002,003"
    Então deveria retornar erro
    E a mensagem "Já existe localização com a descrição e o tipo informados."

  Cenário: Dado que não foi informada a descrição
    Quando foi informaro a descrição "", tipo "002" e os serviços "001,002,003"
    Então deveria retornar erro
    E a mensagem "Obrigatório informar a descrição"

  Cenário: Dado que não foi informado o tipo
    Quando foi informaro a descrição "A", tipo "" e os serviços "001,002,003"
    Então deveria retornar erro
    E a mensagem "Obrigatório informar o tipo"

  Cenário: Dado que não foi informado serviço
    Quando foi informaro a descrição "A", tipo "002" e os serviços ""
    Então deveria retornar erro
    E a mensagem "Obrigatório informar pelo menos um serviço"
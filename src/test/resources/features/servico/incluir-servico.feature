# language: pt
@servico

Funcionalidade: Incluir Serviço
    O sistema permite que o usuário inclua um serviço

  Cenario de Fundo:
    Dado que o usuário "pericles" possui permissao de acesso a funcionalidade
    E que exitem serviços cadastrados
      | id     | descricao     | sigla | cor    |
      | 001    | FINANCIAMENTO | FIN   | AZUL   |
      | 002    | NEGOCIAÇÃO    | NEG   | AMARELO|

  Cenário: Dado que foi informado dados válidos
    Quando foi informaro a descrição "MATRÍCULA", sigla "MAT" e cor "VERDE"
    Então deveria retornar objecto criado com sucesso
    E os detalhes do Serviço são:
      | id     | descricao     | sigla | cor    |
      | 003    | MATRÍCULA     | MAT   | VERDE  |

  Cenário: Dado que foi informado serviço já cadastrado com a sigla informada
    Quando foi informaro a descrição "MATRÍCULA", sigla "NEG" e cor "VERDE"
    Então deveria retornar erro
    E a mensagem "Já existe serviço cadastrado com a sigla informada"

  Cenário: Dado que foi informado serviço com cor indisponível
    Quando foi informaro a descrição "MATRÍCULA", sigla "NEG" e cor "PRETA"
    Então deveria retornar erro
    E a mensagem "Cor inválida"

  Cenário: Dado que não foi informada a descrição
    Quando foi informaro a descrição "", sigla "MAT" e cor "VERDE"
    Então deveria retornar erro
    E a mensagem "Obrigatório informar a descrição"

  Cenário: Dado que não foi informada a sigla
    Quando foi informaro a descrição "MATRÍCULA", sigla "" e cor "VERDE"
    Então deveria retornar erro
    E a mensagem "Obrigatório informar a sigla"

  Cenário: Dado que não foi informada a cor
    Quando foi informaro a descrição "MATRÍCULA", sigla "MAT" e cor ""
    Então deveria retornar erro
    E a mensagem "Cor inválida"






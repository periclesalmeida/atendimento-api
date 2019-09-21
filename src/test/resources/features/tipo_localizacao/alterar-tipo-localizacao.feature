# language: pt
@alterar_tipo_localizacao

Funcionalidade: Alterar Tipo Localização
    O sistema permite que o usuário altere um tipo localizãção

  Cenario de Fundo:
    Dado que o usuário "pericles" possui permissao de acesso a funcionalidade
    E que exitem tipo localização cadastrados
      | id             | descricao            |
      | 001            | SALA                 |
      | 003            | MESA                 |

  Cenário: Dado que foi informado dados válidos
    Quando foi informaro a descrição "GUICHÊ" para o tipo documento de id "003"
    Então deveria retornar sucesso
    E os detalhes do Tipo Localização são:
      | id             | descricao            |
      | 003            | GUICHÊ               |

  Cenário: Dado que foi informado tipo localização já cadastrado
    Quando foi informaro a descrição "SALA" para o tipo documento de id "003"
    Então deveria retornar erro
    E a mensagem "Tipo Localização já cadastrado"

  Cenário: Dado que não foi informada a descrição
    Quando foi informaro a descrição "" para o tipo documento de id "003"
    Então deveria retornar erro
    E a mensagem "Obrigatório informar a descrição"






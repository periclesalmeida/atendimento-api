# language: pt
@consultar_tipo_localizacao

Funcionalidade: Consultar Tipo Localização
    O sistema permite que o usuário consulte os tipos localização cadastradas

  Cenario de Fundo:
    Dado que o usuário "pericles" possui permissao de acesso a funcionalidade
    E que exitem tipo localização cadastrados
      | id             | descricao            |
      | 001            | SALA                 |
      | 002            | GUICHÊ               |
      | 003            | MESA                 |

  Cenário: Informando dados existentes
    Quando foi que informado a descricao "SAL"
    Então o sistema exibe:
      | id             | descricao            |
      | 001            | SALA                 |

  Cenário: Informando dados com mais de um tipo localização cadastrado
    Quando foi que informado a descricao "SA"
    Então o sistema exibe:
      | id             | descricao            |
      | 001            | SALA                 |
      | 003            | MESA                 |

  Cenário: Informando dados que não existem
    Quando foi que informado a descricao "CADEIRA"
    Então o sistema exibe:
      | id               | descricao            |

  Cenário: Sem informar parâmetros
    Quando foi que informado a descricao ""
    Então o sistema exibe:
      | id             | descricao            |
      | 001            | SALA                 |
      | 002            | GUICHÊ               |
      | 003            | MESA                 |






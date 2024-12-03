Feature: Cadastro de Produto
  Cenário: Cadastrar um novo produto
    Given que o sistema está pronto para cadastrar um novo produto
    And o produto a ser cadastrado tem as seguintes características:
      | nome        | descricao     | quantidade | valor  |
      | Produto A   | Descrição A   | 10         | 50.00  |
    When o usuário cadastra o produto
    Then o produto deve ser salvo no banco de dados
    And o produto deve ter um id gerado
    And o id do produto deve ser retornado
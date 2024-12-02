Feature: Cadastro de Produto

Feature: Cadastro de Produto
  Cenário: Cadastrar um novo produto
    Dado que o sistema está pronto para cadastrar um novo produto
    E o produto a ser cadastrado tem as seguintes características:
      | nome        | descricao     | quantidade | valor  |
      | Produto A   | Descrição A   | 10         | 50.00  |
    Quando o usuário cadastra o produto
    Então o produto deve ser salvo no banco de dados
    E o produto deve ter um id gerado
    E o id do produto deve ser retornado

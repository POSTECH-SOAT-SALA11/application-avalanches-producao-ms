package com.avalanches.stepdefinitions;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.interfaceadapters.gateways.ProdutoGateway;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collections;

public class CadastroProdutoSteps {

    @InjectMocks
    private ProdutoGateway produtoGateway;
    private Produto produto;

    @Given("que o sistema está pronto para cadastrar um novo produto")
    public void sistemaProntoParaCadastrarProduto() {
        produtoGateway = Mockito.mock(ProdutoGateway.class); // Criação do mock diretamente aqui
    }

    @And("o produto a ser cadastrado tem as seguintes características:")
    public void produtoCaracteristicas(java.util.Map<String, String> produtoInfo) {
        String nome = produtoInfo.get("nome");
        String descricao = produtoInfo.get("descricao");
        CategoriaProduto categoria = CategoriaProduto.LANCHE;
        int quantidade = Integer.parseInt(produtoInfo.get("quantidade"));
        BigDecimal valor = new BigDecimal(produtoInfo.get("valor"));

        produto = new Produto(0, valor, quantidade, categoria, nome, descricao, Collections.emptyList());
    }

    @When("o usuário cadastra o produto")
    public void cadastrarProduto() {
        produtoGateway.cadastrar(produto);
    }

    @Then("o produto deve ser salvo no banco de dados")
    public void produtoSalvoNoBanco() {
        Mockito.verify(produtoGateway).cadastrar(produto);
    }

    @Then("o produto deve ter um id gerado")
    public void idGerado() {
        Assert.assertNotNull(produto.id);
    }

    @Then("o id do produto deve ser retornado")
    public void idRetornado() {
        Assert.assertTrue(produto.id > 0);
    }
}
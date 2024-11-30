package com.avalanches.frameworksanddrivers.api;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.frameworksanddrivers.api.dto.ImagemParams;
import com.avalanches.frameworksanddrivers.api.dto.ProdutoParams;
import com.avalanches.interfaceadapters.controllers.ProdutoController;
import com.avalanches.interfaceadapters.presenters.dtos.CategoriaProdutoDto;
import com.avalanches.interfaceadapters.presenters.dtos.ImagemDto;
import com.avalanches.interfaceadapters.presenters.dtos.ProdutoDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ProdutoApiTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private ProdutoController produtoController;

    @Mock
    private GeneratedKeyHolder keyHolder;

    @InjectMocks
    private ProdutoApi produtoApi;

    ProdutoParams produtoParams;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoParams = new ProdutoParams(
                1,
                new BigDecimal("29.90"),
                10,
                CategoriaProduto.LANCHE,
                "X-Avalanche",
                "XAvalanche com molho da casa",
                new ImagemParams[]{
                        new ImagemParams(1, "imagem1.png", "Imagem frontal", "image/png", 1024, new byte[]{1}),
                        new ImagemParams(2, "imagem2.jpg", "Imagem lateral", "image/jpeg", 2048, new byte[]{2})
                }
        );
    }

    @Test
    void deveCadastrarProduto() {

        doNothing().when(produtoController).cadastrarProduto(any(), eq(jdbcOperations));

        Map<String, Object> generatedKeys = new HashMap<>();
        generatedKeys.put("id", 1);
        when(keyHolder.getKeys()).thenReturn(generatedKeys);

        when(jdbcOperations.update(any(), eq(keyHolder))).thenReturn(1);

        ResponseEntity<Void> response = produtoApi.cadastrar(produtoParams);

        ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoController, times(1)).cadastrarProduto(produtoCaptor.capture(), eq(jdbcOperations));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void deveAtualizarProduto() {
        int id = 10;
        ProdutoParams produtoParams = new ProdutoParams(
                id,
                new BigDecimal("29.90"),
                5,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "Delicioso X-Burger com queijo e bacon",
                new ImagemParams[]{
                        new ImagemParams(1, "imagem1.png", "Descrição 1", "image/png", 2048, new byte[0]),
                        new ImagemParams(2, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, new byte[0])
                }
        );

        Produto produtoEntityEsperado = new Produto(
                id,
                produtoParams.valor(),
                produtoParams.quantidade(),
                produtoParams.categoria(),
                produtoParams.nome(),
                produtoParams.descricao(),
                List.of(
                        new Imagem(1, "imagem1.png", "Descrição 1", "image/png", 2048, null, new byte[0]),
                        new Imagem(2, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, null, new byte[0])
                )
        );

        doNothing().when(produtoController).atualizarProduto(any(Produto.class), eq(jdbcOperations));

        ResponseEntity<Void> response = produtoApi.atualizar(id, produtoParams);

        ArgumentCaptor<Produto> produtoCaptor = ArgumentCaptor.forClass(Produto.class);
        verify(produtoController, times(1)).atualizarProduto(produtoCaptor.capture(), eq(jdbcOperations));

        Produto produtoCapturado = produtoCaptor.getValue();

        assertEquals(produtoEntityEsperado.id, produtoCapturado.id);
        assertEquals(produtoEntityEsperado.valor, produtoCapturado.valor);
        assertEquals(produtoEntityEsperado.quantidade, produtoCapturado.quantidade);
        assertEquals(produtoEntityEsperado.categoria, produtoCapturado.categoria);
        assertEquals(produtoEntityEsperado.nome, produtoCapturado.nome);
        assertEquals(produtoEntityEsperado.descricao, produtoCapturado.descricao);
        assertEquals(produtoEntityEsperado.imagens.size(), produtoCapturado.imagens.size());

        assertEquals(ResponseEntity.status(200).build(), response);
    }

    @Test
    void deveExcluirProduto() {
        int id = 10;

        doNothing().when(produtoController).excluirProduto(eq(id), eq(jdbcOperations));

        ResponseEntity<Void> response = produtoApi.excluir(id);

        verify(produtoController, times(1)).excluirProduto(eq(id), eq(jdbcOperations));

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deveConsultarProdutosPorCategoria() {
        CategoriaProduto categoriaProduto = CategoriaProduto.LANCHE;
        CategoriaProdutoDto categoriaProdutoDto = CategoriaProdutoDto.LANCHE;

        ImagemDto imagem1 = new ImagemDto(1, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", new byte[0]);
        ImagemDto imagem2 = new ImagemDto(2, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        ProdutoDto produto1 = new ProdutoDto(
                1,
                new BigDecimal("29.90"),
                10,
                categoriaProdutoDto,
                "Produto 1",
                "Descrição 1",
                List.of(imagem1, imagem2)
        );

        ProdutoDto produto2 = new ProdutoDto(
                2,
                new BigDecimal("19.90"),
                20,
                categoriaProdutoDto,
                "Produto 2",
                "Descrição 2",
                List.of(imagem1)
        );

        List<ProdutoDto> produtosEsperados = List.of(produto1, produto2);

        when(produtoController.consultarProdutos(eq(categoriaProduto), eq(jdbcOperations)))
                .thenReturn(produtosEsperados);

        ResponseEntity<List<ProdutoDto>> response = produtoApi.consultarPorCategoria(categoriaProduto);

        verify(produtoController, times(1)).consultarProdutos(eq(categoriaProduto), eq(jdbcOperations));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(produtosEsperados, response.getBody());
    }
}
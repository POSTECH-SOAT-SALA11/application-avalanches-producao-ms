package com.avalanches.frameworksanddrivers.api;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.frameworksanddrivers.api.dto.ImagemParams;
import com.avalanches.frameworksanddrivers.api.dto.ProdutoParams;
import com.avalanches.interfaceadapters.controllers.ProdutoController;
import com.avalanches.interfaceadapters.controllers.interfaces.ProdutoControllerInterface;
import com.avalanches.interfaceadapters.presenters.dtos.CategoriaProdutoDto;
import com.avalanches.interfaceadapters.presenters.dtos.ProdutoDto;
import mockit.Expectations;
import mockit.Mocked;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProdutoApiTest {

    @Mock
    private JdbcOperations jdbcOperations;


    @Mock
    private ProdutoControllerInterface produtoController;

    @Mock
    private GeneratedKeyHolder keyHolder;

    @InjectMocks
    private ProdutoApi produtoApi; // A classe que estamos testando

    private ImagemParams[] imagens;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imagens = new ImagemParams[]{
                new ImagemParams(
                        100,
                        "imagem1.png",
                        "Descrição 1",
                        "image/png",
                        2048,
                        new byte[0] // Conteúdo vazio, para exemplo
                ),
                new ImagemParams(
                        101,
                        "imagem2.jpg",
                        "Descrição 2",
                        "image/jpeg",
                        3072,
                        new byte[0] // Conteúdo vazio, para exemplo
                )
        };
    }


//
//    @Test
//    void testConsultarPorCategoria() {
//        // Arrange
//        CategoriaProduto categoriaProdutoDto = CategoriaProduto.LANCHE;
//
//        // Criando a lista de ProdutoDto esperada
//        ProdutoDto produto1 = new ProdutoDto(
//                1,
//                new BigDecimal("10.00"),
//                30,
//                CategoriaProdutoDto.LANCHE, // Usando CategoriaProdutoDto diretamente
//                "XAvalanche",
//                "Lanche com molho da casa",
//                Arrays.asList() // Imagens vazias
//        );
//
//        ProdutoDto produto2 = new ProdutoDto(
//                2,
//                new BigDecimal("15.00"),
//                20,
//                CategoriaProdutoDto.LANCHE,
//                "XBurger",
//                "Hamburguer com queijo",
//                Arrays.asList()
//        );
//
//        List<ProdutoDto> produtoList = Arrays.asList(produto1, produto2);
//
//        // Configurando o mock para retornar a lista esperada
//        when(produtoController.consultarProdutos(any(), any())).thenReturn(produtoList);
//
//        // Usando PowerMockito para mockar a criação do ProdutoController
//        //PowerMockito.whenNew(ProdutoController.class).withNoArguments().thenReturn(produtoController);
//
//        // Act
//        ResponseEntity<List<ProdutoDto>> response = produtoApi.consultarPorCategoria(categoriaProdutoDto);
//
//        // Assert
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertEquals(produtoList, response.getBody());
//    }

}
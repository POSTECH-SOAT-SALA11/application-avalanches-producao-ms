package com.avalanches.interfaceadapters.gateways;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class ProdutoGatewayTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @InjectMocks
    private ProdutoGateway produtoGateway;

    @Mock
    private GeneratedKeyHolder keyHolder;

    private List<Imagem> imagens;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        imagens =  List.of(
                new Imagem(100, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", new byte[0]),
                new Imagem(101, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0])
        );
    }
    @Test
    void deveCadastrarProduto() {
        Produto produto = new Produto(
                1,
                new BigDecimal("23.90"),
                10,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "X-Burger xpto",
                imagens
        );

        Map<String, Object> generatedKeys = new HashMap<>();
        generatedKeys.put("id", 1);
        when(keyHolder.getKeys()).thenReturn(generatedKeys);

        when(jdbcOperations.update(any(), eq(keyHolder))).thenReturn(1);

        produtoGateway.cadastrar(produto);

        ArgumentCaptor<KeyHolder> keyHolderCaptor = ArgumentCaptor.forClass(KeyHolder.class);
        verify(jdbcOperations).update(any(), keyHolderCaptor.capture());

        assertNotNull(produto.id);
        assertEquals(1, produto.id); // Verifica que o ID gerado é 1
    }
}


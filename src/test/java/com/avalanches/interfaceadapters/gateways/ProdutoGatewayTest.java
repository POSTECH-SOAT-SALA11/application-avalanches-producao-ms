package com.avalanches.interfaceadapters.gateways;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.interfaceadapters.gateways.mapper.ImagemRowMapper;
import com.avalanches.interfaceadapters.gateways.mapper.ProdutoRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.*;

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
        imagens = List.of(
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


    @Test
    void DeveCadastrarImagemProduto() {

        int idProduto = 1;
        int idImagem = 101;

        when(jdbcOperations.update(anyString(), eq(idProduto), eq(idImagem))).thenReturn(1); // Retorna 1 indicando sucesso na execução

        produtoGateway.cadastrarImagemProduto(idProduto, idImagem);

        verify(jdbcOperations, times(1)).update(
                eq("INSERT INTO produto_imagem(idproduto, idimagem) VALUES (?, ?);"),
                eq(idProduto),
                eq(idImagem)
        );
    }

    @Test
    void DeveAtualizarProduto() {
        Produto produto = new Produto(
                1,
                new BigDecimal("23.90"),
                10,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "X-Burger xpto",
                new ArrayList<>()
        );

        produtoGateway.atualizar(produto);

        verify(jdbcOperations, times(1)).update(
                eq("UPDATE produto SET nome=?, descricao=?, categoria=?, quantidade=?, valor=? WHERE id=?"),
                eq(produto.nome),
                eq(produto.descricao),
                eq(produto.categoria.getValue()),
                eq(produto.quantidade),
                eq(produto.valor),
                eq(produto.id)
        );
    }


    @Test
    void deveExcluirProduto() {
        int idProduto = 1;

        produtoGateway.excluir(idProduto);

        verify(jdbcOperations, times(1)).update(
                eq("DELETE FROM produto WHERE id=?"),
                eq(idProduto)
        );
    }

    @Test
    void DeveExcluirImagemProduto() {
        // Arrange: Definir os IDs do produto e da imagem a serem excluídos
        int idProduto = 1;
        int idImagem = 101;

        // Act: Chamar o método excluirImagemProduto
        produtoGateway.excluirImagemProduto(idProduto, idImagem);

        // Assert: Verificar se o método update foi chamado corretamente com os parâmetros corretos
        verify(jdbcOperations, times(1)).update(
                eq("DELETE FROM produto_imagem WHERE idproduto=? AND idimagem=?"),
                eq(idProduto),
                eq(idImagem)
        );
    }


    @Test
    void DeveConsultarProdutos() {
        CategoriaProduto categoriaProduto = CategoriaProduto.LANCHE;

        Produto produtoA = new Produto(
                1,
                new BigDecimal("23.90"),
                10,
                categoriaProduto,
                "X-Burger",
                "X-Burger xpto",
                imagens
        );
        Produto produtoB = new Produto(
                2,
                new BigDecimal("19.90"),
                15,
                categoriaProduto,
                "X-Bacon",
                "X-Bacon xpto",
                imagens
        );

        List<Produto> produtosEsperados = Arrays.asList(produtoA, produtoB);

        when(jdbcOperations.query(
                anyString(),  // Para a string SQL, qualquer valor que corresponda
                any(ProdutoRowMapper.class), // Para o mapeador, qualquer instância do ProdutoRowMapper
                eq(categoriaProduto.getValue())  // Para a categoria
        )).thenReturn(produtosEsperados);

        List<Produto> produtosRetornados = produtoGateway.consultarProdutos(categoriaProduto);

        assertNotNull(produtosRetornados);
        assertEquals(2, produtosRetornados.size());
        assertEquals(produtoA, produtosRetornados.get(0));
        assertEquals(produtoB, produtosRetornados.get(1));

        verify(jdbcOperations, times(1)).query(
                anyString(),
                any(ProdutoRowMapper.class),
                eq(categoriaProduto.getValue())
        );
    }

    @Test
    void DeveConsultarProdutosPorID() {
        int idProduto = 1;
        CategoriaProduto categoriaProduto = CategoriaProduto.LANCHE;

        Produto produtoEsperado = new Produto(
                idProduto,
                new BigDecimal("23.90"),
                10,
                categoriaProduto,
                "X-Burger",
                "X-Burger xpto",
                List.of(new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]))
        );

        when(jdbcOperations.queryForObject(
                anyString(),
                any(ProdutoRowMapper.class),  // ProdutoRowMapper para mapeamento
                eq(idProduto)  // ID passado no parâmetro
        )).thenReturn(produtoEsperado);

        Produto produtoRetornado = produtoGateway.consultarProdutosPorID(idProduto);

        assertNotNull(produtoRetornado, "O produto retornado não pode ser null");
        assertEquals(produtoEsperado, produtoRetornado, "O produto retornado deve ser igual ao esperado");

        verify(jdbcOperations, times(1)).queryForObject(
                anyString(),
                any(ProdutoRowMapper.class),
                eq(idProduto)
        );
    }

    @Test
    void deveLancarNotFoundExceptionQuandoProdutoNaoExistir() {
        int idProdutoInvalido = 999;

        when(jdbcOperations.queryForObject(
                anyString(),
                any(ProdutoRowMapper.class),
                eq(idProdutoInvalido)
        )).thenThrow(new EmptyResultDataAccessException(1));  // Lança a exceção

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            produtoGateway.consultarProdutosPorID(idProdutoInvalido);
        });

        assertEquals("Produto não existe", exception.getMessage());

        verify(jdbcOperations, times(1)).queryForObject(
                anyString(),
                any(ProdutoRowMapper.class),
                eq(idProdutoInvalido)
        );
    }

    @Test
    void deveRetornarImagensQuandoProdutoExistir() {
        int idProduto = 1;

        Imagem imagem1 = new Imagem(1, "imagem1.jpg", "Descrição 1", "image/jpeg", 2048, "/caminho/imagem1.jpg", new byte[0]);
        Imagem imagem2 = new Imagem(2, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        List<Imagem> imagensEsperadas = Arrays.asList(imagem1, imagem2);

        when(jdbcOperations.query(
                anyString(),
                any(ImagemRowMapper.class),
                eq(idProduto)
        )).thenReturn(imagensEsperadas);

        List<Imagem> imagensRetornadas = produtoGateway.consultarImagensPorProduto(idProduto);

        assertNotNull(imagensRetornadas, "A lista de imagens não pode ser nula");
        assertEquals(imagensEsperadas.size(), imagensRetornadas.size(), "A quantidade de imagens deve ser igual");

        assertEquals(imagensEsperadas, imagensRetornadas, "As imagens retornadas devem ser iguais às esperadas");

        verify(jdbcOperations, times(1)).query(
                eq("select i.* from produto_imagem pi2 " +
                        "inner join imagem i on i.id = pi2.idimagem " +
                        "where idproduto = ?"),
                any(ImagemRowMapper.class),
                eq(idProduto)
        );
    }

    @Test
    void deveRetornarTrueQuandoProdutoExistir() {
        int idProduto = 1;
        String sql = "SELECT COUNT(*) FROM produto WHERE id = ?";

        when(jdbcOperations.queryForObject(
                eq(sql),
                eq(new Object[]{idProduto}),
                eq(Integer.class)
        )).thenReturn(1);

        boolean produtoExiste = produtoGateway.verificaProdutoExiste(idProduto);

        assertTrue(produtoExiste, "O produto deve existir");

        verify(jdbcOperations, times(1)).queryForObject(
                eq(sql),
                eq(new Object[]{idProduto}),
                eq(Integer.class)
        );
    }

    @Test
    void deveRetornarFalseQuandoFalhaNaConsulta() {

        int idProduto = 987;
        String sql = "SELECT COUNT(*) FROM produto WHERE id = ?";

        when(jdbcOperations.queryForObject(
                eq(sql),
                eq(new Object[]{idProduto}),
                eq(Integer.class)
        )).thenReturn(0);

        boolean produtoExiste = produtoGateway.verificaProdutoExiste(idProduto);

        assertFalse(produtoExiste, "O produto não  existir");

        verify(jdbcOperations, times(1)).queryForObject(
                eq(sql),
                eq(new Object[]{idProduto}),
                eq(Integer.class)
        );
    }

}


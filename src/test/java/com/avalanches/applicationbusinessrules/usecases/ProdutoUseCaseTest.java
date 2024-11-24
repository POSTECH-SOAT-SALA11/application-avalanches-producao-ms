package com.avalanches.applicationbusinessrules.usecases;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Imagem;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.interfaceadapters.gateways.interfaces.ImagemGatewayInterface;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.webjars.NotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ProdutoUseCaseTest {

    private ProdutoUseCase produtoUseCase;
    private ProdutoGatewayInterface produtoGateway;
    private ImagemGatewayInterface imagemGateway;
    private List<Imagem> imagens;

    @BeforeEach
    void setUp() {
        produtoUseCase = new ProdutoUseCase();
        produtoGateway = Mockito.mock(ProdutoGatewayInterface.class);
        imagemGateway = Mockito.mock(ImagemGatewayInterface.class);
        imagens =  List.of(
                new Imagem(100, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", new byte[0]),
                new Imagem(101, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0])
        );
    }

    @Test
    void deveCadastrarProdutoComImagens() {
        ProdutoGatewayInterface produtoGateway = Mockito.mock(ProdutoGatewayInterface.class);
        ImagemGatewayInterface imagemGateway = Mockito.mock(ImagemGatewayInterface.class);

        ProdutoUseCase produtoUseCase = new ProdutoUseCase();

        Produto produto = new Produto(
                1,
                new BigDecimal("23.90"),
                10,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "X-Burger xpto",
                imagens
        );

        produtoUseCase.cadastrarProduto(produto, produtoGateway, imagemGateway);

        verify(produtoGateway, times(1)).cadastrar(produto); // Verifica se o produto foi cadastrado
        verify(imagemGateway, times(2)).cadastrar(any(Imagem.class)); // Verifica se as imagens foram cadastradas
        verify(produtoGateway, times(1)).cadastrarImagemProduto(produto.id, 100); // Verifica o relacionamento do produto com a 1ª imagem
        verify(produtoGateway, times(1)).cadastrarImagemProduto(produto.id, 101); // Verifica o relacionamento do produto com a 2ª imagem
    }

    @Test
    void deveConsultarProdutosPorCategoria() {

        CategoriaProduto categoria = CategoriaProduto.LANCHE;

        Imagem imagem1 = new Imagem(1, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", null);
        Imagem imagem2 = new Imagem(2, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", null);

        Produto produto1 = new Produto(
                10,
                new BigDecimal("20.90"),
                5,
                categoria,
                "X-Burger-bacon",
                "xpto",
                imagens
        );
        Produto produto2 = new Produto(
                11,
                new BigDecimal("29.90"),
                3,
                categoria,
                "X-Burger",
                "xpto",
                imagens
        );

        List<Produto> produtosMock = List.of(produto1, produto2);
        List<Imagem> imagensMockProduto1 = List.of(imagem1);
        List<Imagem> imagensMockProduto2 = List.of(imagem2);

        when(produtoGateway.consultarProdutos(categoria)).thenReturn(produtosMock);
        when(produtoGateway.consultarImagensPorProduto(10)).thenReturn(imagensMockProduto1);
        when(produtoGateway.consultarImagensPorProduto(11)).thenReturn(imagensMockProduto2);
        when(imagemGateway.lerArquivo("/caminho/imagem1.png")).thenReturn(new byte[]{1, 2, 3});
        when(imagemGateway.lerArquivo("/caminho/imagem2.jpg")).thenReturn(new byte[]{4, 5, 6});

        List<Produto> produtos = produtoUseCase.consultarProdutos(categoria, produtoGateway, imagemGateway);

        assertEquals(2, produtos.size());
        assertEquals(produto1.id, produtos.get(0).id);
        assertEquals(produto2.id, produtos.get(1).id);

        assertEquals(1, produtos.get(0).imagens.size());
        assertEquals("imagem1.png", produtos.get(0).imagens.get(0).nome);

        assertEquals(1, produtos.get(1).imagens.size());
        assertEquals("imagem2.jpg", produtos.get(1).imagens.get(0).nome);

        // Verificação de interações
        verify(produtoGateway, times(1)).consultarProdutos(categoria);
        verify(produtoGateway, times(1)).consultarImagensPorProduto(10);
        verify(produtoGateway, times(1)).consultarImagensPorProduto(11);
        verify(imagemGateway, times(1)).lerArquivo("/caminho/imagem1.png");
        verify(imagemGateway, times(1)).lerArquivo("/caminho/imagem2.jpg");
    }

    @Test
    public void deveAtualizarProdutoComSucesso() {
        
        Produto produto = new Produto(
                10,
                new BigDecimal("29.90"),
                3,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "xpto",
                imagens
        );
        produto.id = 10;
        produto.imagens = List.of(
                new Imagem(1, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", new byte[0]),
                new Imagem(0, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0])
        );

        Imagem imagemExistente = new Imagem(1, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", new byte[0]);
        List<Imagem> imagensNoBanco = Arrays.asList(imagemExistente);

        when(produtoGateway.consultarProdutosPorID(10)).thenReturn(produto); // O produto existe
        when(produtoGateway.consultarImagensPorProduto(10)).thenReturn(imagensNoBanco); // Imagens no banco

        produtoUseCase.atualizarProduto(produto, produtoGateway, imagemGateway);

        verify(produtoGateway).cadastrarImagemProduto(10, 0);  // Imagem 0 deve ser cadastrada

        verify(produtoGateway).atualizar(produto);  // Produto deve ser atualizado

    }

    @Test
    public void deveLancarNotFoundQuandoProdutoNaoExistir() {
        Produto produto = new Produto(
                10,
                new BigDecimal("29.90"),
                3,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "xpto",
                List.of()
        );

        when(produtoGateway.consultarProdutosPorID(10)).thenReturn(null); // Produto não existe

        assertThrows(NotFoundException.class, () -> {
            produtoUseCase.atualizarProduto(produto, produtoGateway, imagemGateway);
        });

        verify(produtoGateway).consultarProdutosPorID(10);
    }

    @Test
    public void deveExcluirProdutoComSucesso() {
        Produto produto = new Produto(
                10,
                new BigDecimal("29.90"),
                3,
                CategoriaProduto.LANCHE,
                "X-Burger",
                "xpto",
                List.of(
                        new Imagem(1, "imagem1.png", "Descrição 1", "image/png", 2048, "/caminho/imagem1.png", new byte[0]),
                        new Imagem(2, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0])
                )
        );

        List<Imagem> imagensNoBanco = produto.imagens;

        when(produtoGateway.consultarProdutosPorID(10)).thenReturn(produto);
        when(produtoGateway.consultarImagensPorProduto(10)).thenReturn(imagensNoBanco);

        produtoUseCase.excluirProduto(10, produtoGateway, imagemGateway);

        verify(produtoGateway).excluirImagemProduto(10, 1);
        verify(produtoGateway).excluirImagemProduto(10, 2);

        verify(imagemGateway).excluir(imagensNoBanco.get(0));
        verify(imagemGateway).excluir(imagensNoBanco.get(1));

        verify(produtoGateway).excluir(10);
    }
    
    @Test
    public void deveLancarNotFoundQuandoProdutoNaoExistirParaExcluir() {
        int produtoId = 10;

        when(produtoGateway.consultarProdutosPorID(produtoId)).thenReturn(null); // Produto não existe

        assertThrows(NotFoundException.class, () -> {
            produtoUseCase.excluirProduto(produtoId, produtoGateway, imagemGateway);
        });

        verify(produtoGateway).consultarProdutosPorID(produtoId);
    }
    
}


package com.avalanches.interfaceadapters.gateways;

import com.avalanches.enterprisebusinessrules.entities.Imagem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static com.avalanches.interfaceadapters.gateways.ImagemGateway.IMAGENS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ImagemGatewayTest {

    @Mock
    private JdbcOperations jdbcOperations;

    @Mock
    private GeneratedKeyHolder keyHolder;

    @Spy
    @InjectMocks
    private ImagemGateway imagemGateway;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void deveCadastrarImagem() throws SQLException {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        Map<String, Object> generatedKeys = new HashMap<>();
        generatedKeys.put("id", 1);
        when(keyHolder.getKeys()).thenReturn(generatedKeys);

        when(jdbcOperations.update(any(PreparedStatementCreator.class), eq(keyHolder))).thenReturn(1);

        imagemGateway.cadastrar(imagem);

        ArgumentCaptor<PreparedStatementCreator> statementCaptor = ArgumentCaptor.forClass(PreparedStatementCreator.class);

        verify(jdbcOperations).update(statementCaptor.capture(), eq(keyHolder));

        PreparedStatementCreator preparedStatementCreator = statementCaptor.getValue();

        Connection connection = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(ps);

        PreparedStatement preparedStatement = preparedStatementCreator.createPreparedStatement(connection);

        assertNotNull(preparedStatement);

        verify(ps).setString(1, imagem.nome);
        verify(ps).setString(2, imagem.descricao);
        verify(ps).setString(3, imagem.tipoConteudo);
        verify(ps).setString(4, imagem.caminho);
        verify(ps).setInt(5, imagem.tamanho);
        assertNotNull(imagem.id);
        assertEquals(1, imagem.id);
    }

    @Test
    void deveAtualizarImagem() {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        when(jdbcOperations.update(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(1);

        doNothing().when(imagemGateway).editarArquivo(imagem);
        imagemGateway.atualizar(imagem);

        verify(jdbcOperations).update(
                eq("UPDATE imagem SET nome=?, descricao=?, tipoconteudo=?, caminho=?, tamanho=? WHERE id=(?);"),
                eq(imagem.nome),
                eq(imagem.descricao),
                eq(imagem.tipoConteudo),
                eq(imagem.caminho),
                eq(imagem.tamanho),
                eq(imagem.id)
        );
    }

    @Test
    void testEditarArquivoQuandoErroNaEscrita()  {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);
        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            Path imagePath = Paths.get(IMAGENS, "arquivo.jpg");

            mockedFiles.when(() -> Files.exists(Paths.get(IMAGENS))).thenReturn(true);
            mockedFiles.when(() -> Files.exists(imagePath)).thenReturn(true);

            mockedFiles.when(() -> Files.write(imagePath, new byte[]{})).thenThrow(new NotFoundException("Erro de escrita"));

            NotFoundException exception = assertThrows(NotFoundException.class, () -> {
                imagemGateway.editarArquivo(imagem);
            });

            assertEquals("Arquivo não existe", exception.getMessage());
        }
    }

    @Test
    void deveExcluirImagem() {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        MockedStatic<Files> filesMocked = mockStatic(Files.class);
        filesMocked.when(() -> Files.exists(any(Path.class))).thenReturn(true);
        when(jdbcOperations.update(anyString(), anyInt())).thenReturn(1);

        imagemGateway.excluir(imagem);

        verify(jdbcOperations, times(1)).update(eq("DELETE FROM imagem WHERE id=?"), eq(imagem.id));

        Path imagePath = Paths.get("imagens", "/caminho/imagem2.jpg");
        assertTrue(Files.exists(imagePath));
        filesMocked.close();
    }


    @Test
    void NaodeveExcluirImagemQuandoPathForInvalido() {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        when(jdbcOperations.update(anyString(), anyInt())).thenReturn(1);

        imagemGateway.excluir(imagem);

        verify(jdbcOperations, times(1)).update(eq("DELETE FROM imagem WHERE id=?"), eq(imagem.id));

        Path imagePath = Paths.get("imagens", "/caminho/imagem2.jpg");
        assertFalse(Files.exists(imagePath));
    }


    @Test
    void deveExcluirImagemComErroAoDeletarArquivo() {

        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        MockedStatic<Files> filesMocked = mockStatic(Files.class);
        filesMocked.when(() -> Files.deleteIfExists(any(Path.class))).thenThrow(new IOException("Erro ao deletar arquivo."));
        when(jdbcOperations.update(anyString(), anyInt())).thenReturn(1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagemGateway.excluir(imagem);
        });

        verify(jdbcOperations, times(1)).update(eq("DELETE FROM imagem WHERE id=?"), eq(imagem.id));

        assertEquals("Erro ao deletar arquivo.", exception.getMessage());

        filesMocked.close();
    }

    @Test
    void deveLerArquivo() throws IOException {
        String path = "imagem.jpg";
        Path imagePath = Paths.get(IMAGENS + '/' + path);
        byte[] expectedContent = { 1, 2, 3, 4, 5 }; // Conteúdo do arquivo esperado

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllBytes(imagePath)).thenReturn(expectedContent);

            byte[] fileContent = imagemGateway.lerArquivo(path);

            assertArrayEquals(expectedContent, fileContent);
        }
    }

    @Test
    void deveDarErroAoLerArquivo() throws IOException {
        String path = "imagem_inexistente.jpg";
        Path imagePath = Paths.get(IMAGENS + '/' + path);

        try (MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.readAllBytes(imagePath)).thenThrow(new IOException("Arquivo não encontrado"));

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                imagemGateway.lerArquivo(path);
            });

            assertEquals("Arquivo não encontrado.", exception.getMessage());
        }
    }
}


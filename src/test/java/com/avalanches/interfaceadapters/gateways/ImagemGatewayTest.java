package com.avalanches.interfaceadapters.gateways;

import com.avalanches.enterprisebusinessrules.entities.Imagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.*;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.*;
import java.util.Map;

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

    @Test
    void deveCadastrarImagem() {
        // Dados da imagem
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        Map<String, Object> generatedKeys = new HashMap<>();
        generatedKeys.put("id", 1);
        when(keyHolder.getKeys()).thenReturn(generatedKeys);

        when(jdbcOperations.update(any(), eq(keyHolder))).thenReturn(1);

        imagemGateway.cadastrar(imagem);

        ArgumentCaptor<KeyHolder> keyHolderCaptor = ArgumentCaptor.forClass(KeyHolder.class);
        verify(jdbcOperations).update(any(), keyHolderCaptor.capture());

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
    void deveExcluirImagem() {
        Imagem imagem = new Imagem(1, "imagem2.jpg", "Descrição 2", "image/jpeg", 3072, "/caminho/imagem2.jpg", new byte[0]);

        MockedStatic<Files> filesMocked = mockStatic(Files.class);
        filesMocked.when(() -> Files.exists(any(Path.class))).thenReturn(true);
        when(jdbcOperations.update(anyString(), anyInt())).thenReturn(1);

        imagemGateway.excluir(imagem);

        verify(jdbcOperations, times(1)).update(eq("DELETE FROM imagem WHERE id=?"), eq(imagem.id));

        Path imagePath = Paths.get("imagens", "/caminho/imagem2.jpg");
        assertTrue(Files.exists(imagePath));
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
        filesMocked.when(() -> Files.deleteIfExists(any(Path.class))).thenThrow(new RuntimeException("Erro ao deletar arquivo."));
        when(jdbcOperations.update(anyString(), anyInt())).thenReturn(1);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            imagemGateway.excluir(imagem);
        });

        verify(jdbcOperations, times(1)).update(eq("DELETE FROM imagem WHERE id=?"), eq(imagem.id));

        assertEquals("Erro ao deletar arquivo.", exception.getMessage());
    }

}


package com.avalanches.interfaceadapters.controllers.interfaces;

import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.interfaceadapters.presenters.dtos.ProdutoDto;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public interface ProdutoControllerInterface {
    void cadastrarProduto(Produto produto, JdbcOperations jdbcOperations);

    List<ProdutoDto> consultarProdutos(CategoriaProduto categoriaProduto, JdbcOperations jdbcOperations);

    void atualizarProduto(Produto produto, JdbcOperations jdbcOperations);

    void excluirProduto(int id, JdbcOperations jdbcOperations);
}

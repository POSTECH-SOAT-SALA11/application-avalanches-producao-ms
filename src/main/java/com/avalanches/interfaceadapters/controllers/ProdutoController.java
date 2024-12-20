package com.avalanches.interfaceadapters.controllers;

import com.avalanches.applicationbusinessrules.usecases.ProdutoUseCase;
import com.avalanches.enterprisebusinessrules.entities.CategoriaProduto;
import com.avalanches.enterprisebusinessrules.entities.Produto;
import com.avalanches.interfaceadapters.controllers.interfaces.ProdutoControllerInterface;
import com.avalanches.interfaceadapters.gateways.ImagemGateway;
import com.avalanches.interfaceadapters.gateways.ProdutoGateway;
import com.avalanches.interfaceadapters.gateways.interfaces.ImagemGatewayInterface;
import com.avalanches.interfaceadapters.gateways.interfaces.ProdutoGatewayInterface;
import com.avalanches.interfaceadapters.presenters.ProdutoPresenter;
import com.avalanches.interfaceadapters.presenters.dtos.ProdutoDto;
import com.avalanches.interfaceadapters.presenters.interfaces.ProdutoPresenterInterface;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
public class ProdutoController implements ProdutoControllerInterface {

    @Override
    public void cadastrarProduto(Produto produto, JdbcOperations jdbcOperations) {
        ProdutoGatewayInterface produtoGateway = new ProdutoGateway(jdbcOperations,new GeneratedKeyHolder());
        ImagemGatewayInterface imagemGateway = new ImagemGateway(jdbcOperations, new GeneratedKeyHolder());
        ProdutoUseCase produtoUseCase = new ProdutoUseCase();
        produtoUseCase.cadastrarProduto(produto, produtoGateway, imagemGateway);
    }

    @Override
    public List<ProdutoDto> consultarProdutos(CategoriaProduto categoriaProduto, JdbcOperations jdbcOperations) {
        ProdutoGatewayInterface produtoGateway = new ProdutoGateway(jdbcOperations, new GeneratedKeyHolder());
        ImagemGatewayInterface imagemGateway = new ImagemGateway(jdbcOperations, new GeneratedKeyHolder());
        ProdutoUseCase produtoUseCase = new ProdutoUseCase();
        List<Produto> produtos = produtoUseCase.consultarProdutos(categoriaProduto, produtoGateway, imagemGateway);
        ProdutoPresenterInterface produtoPresenter = new ProdutoPresenter();
        return produtoPresenter.produtosToDtos(produtos);
    }

    @Override
    public void atualizarProduto(Produto produto, JdbcOperations jdbcOperations) {
        ProdutoGatewayInterface produtoGateway = new ProdutoGateway(jdbcOperations, new GeneratedKeyHolder());
        ImagemGatewayInterface imagemGateway = new ImagemGateway(jdbcOperations, new GeneratedKeyHolder());
        ProdutoUseCase produtoUseCase = new ProdutoUseCase();
        produtoUseCase.atualizarProduto(produto, produtoGateway, imagemGateway);
    }

    @Override
    public void excluirProduto(int id, JdbcOperations jdbcOperations) {
        ProdutoGatewayInterface produtoGateway = new ProdutoGateway(jdbcOperations, new GeneratedKeyHolder());
        ImagemGatewayInterface  imagemGateway = new ImagemGateway(jdbcOperations, new GeneratedKeyHolder());
        ProdutoUseCase produtoUseCase = new ProdutoUseCase();
        produtoUseCase.excluirProduto(id, produtoGateway, imagemGateway);
    }
}

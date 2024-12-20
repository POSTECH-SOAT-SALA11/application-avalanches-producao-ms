package com.avalanches.frameworksanddrivers.api;

import com.avalanches.enterprisebusinessrules.entities.*;
import com.avalanches.frameworksanddrivers.api.dto.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Convert {

    public static Produto produtoParamsToProduto(ProdutoParams params) {
        return new Produto(
            params.id(),
            params.valor(),
            params.quantidade(),
            params.categoria(),
            params.nome(),
            params.descricao(),
            params.imagens() == null ? Collections.emptyList() : imagensParamsToImagens(List.of(params.imagens()))
        );
    }

    public static List<Imagem> imagensParamsToImagens(List<ImagemParams> imagens) {
        if (imagens == null) {
            return Collections.emptyList();
        } else {
            return imagens.stream()
                    .map(imagemParams -> new Imagem(
                        imagemParams.id(),
                        imagemParams.nome(),
                        imagemParams.descricao(),
                        imagemParams.tipoConteudo(),
                        imagemParams.tamanho(),
                        null,
                        imagemParams.conteudo()
                    ))
                    .collect(Collectors.toList());
        }
    }
}
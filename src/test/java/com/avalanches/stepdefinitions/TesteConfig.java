package com.avalanches.stepdefinitions;

import com.avalanches.interfaceadapters.gateways.ProdutoGateway;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TesteConfig {


    @Bean
    public ProdutoGateway produtoGateway() {
        return Mockito.mock(ProdutoGateway.class);
    }

}

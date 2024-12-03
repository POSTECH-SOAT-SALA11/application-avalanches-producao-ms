# application-avalanches-producao-ms
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=POSTECH-SOAT-SALA11_application-avalanches-producao-ms&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=POSTECH-SOAT-SALA11_application-avalanches-producao-ms)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=POSTECH-SOAT-SALA11_application-avalanches-producao-ms&metric=bugs)](https://sonarcloud.io/summary/new_code?id=POSTECH-SOAT-SALA11_application-avalanches-producao-ms)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=POSTECH-SOAT-SALA11_application-avalanches-producao-ms&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=POSTECH-SOAT-SALA11_application-avalanches-producao-ms)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=POSTECH-SOAT-SALA11_application-avalanches-producao-ms&metric=coverage)](https://sonarcloud.io/summary/new_code?id=POSTECH-SOAT-SALA11_application-avalanches-producao-ms)


# Microsserviço de producao
## Funcionalidades Principais 

### Internas
- **Producao de pedido**: Gerencimento dos produtos de um pedido

## Tecnologias Utilizadas

- Java 18
- Spring Boot 3.2.5
- Docker
- Kubernetes (Minikube)
- Amazon Elastic Container Registry (ECR)
- Amazon Elastic Kubernetes Service (EKS)

## Arquitetura AWS(Cloud)
[Ilustração na Wiki](https://github.com/POSTECH-SOAT-SALA11/application-avalanches-pagamento-ms/wiki/Arquitetura-AWS)

## Esteiras CI/CD
[Ilustração na Wiki](https://github.com/POSTECH-SOAT-SALA11/application-avalanches-pagamento-ms/wiki/Esteiras-CI-CD)

## Banco de dados
- Redis: Serve como cache de alta performance, armazenando dados temporários e reduzindo a carga no PostgreSQL. Ele oferece respostas rápidas para consultas frequentes, como o status dos pedidos.

## Estrutura do Projeto

O projeto segue os princípios de Domain-Driven Design (DDD) e clean architecture, com as seguintes camadas:

- **Frameworks and Drivers**: Contém a web api e as configurações de banco de dados.

- **Interface Adapters**: Contém os gateways que garantem a comunicação com o mundo externo,
e os adaptadores que ajudam a camada de apresentação a exibir resultados.

- **Application Business Rules**:  Encapsula e implementa as regras de negócio através de casos de uso.

- **Enterprise Business Rules**:  Representa a camada de entidades e suas regras de negócio.

## Execução do Projeto em Kubernetes

### Requisitos tecnológicos:
- **[Docker](https://www.docker.com/)**: para a criação de imagens de contêineres.

Para executar o projeto em Kubernetes, siga estas etapas:

1. Clone o repositório.
   ```bash
   git clone https://github.com/POSTECH-SOAT-SALA11/application-avalanches-producao-ms.git
   ```

2. Acesse o repositório.
   ```bash
   cd application-avalanches-producao-ms
   ```

3. Execute o script que inicializará o projeto automaticamente.
   ```bash
   docker compose up --build
    ```

4. Acesse o Swagger da aplicação em:
   ```
   http://localhost:8282/swagger-ui/index.html#/
   ```

## Evidencia de cobertura dos testes unitarios
![image](https://github.com/user-attachments/assets/7cfbdd97-f00a-4f0f-b137-5a68af7838fb)

## Autores

- [Hennan Cesar Alves Gadelha de Freitas](https://github.com/HennanGadelha)
  (hennangadelhafreitas@gmail.com)

- [Adinelson da Silva Bruhmuller Júnior](https://github.com/Doomwhite)
  (adinelsonsbruhmuller@gmail.com)

- [RAUL DE SOUZA](https://github.com/raulsouza-rm355416)
  (dev.raulsouza@outlook.com)

- [Raphael Soares Teodoro](https://github.com/raphasteodoro)
  (raphael.s.teodoro@outlook.com)





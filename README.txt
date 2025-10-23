
CF-e SAT Processor

Uma aplicação Java Spring Boot para processar múltiplos arquivos XML de CF-e SAT, extrair dados fiscais relevantes e armazená-los em um banco de dados PostgreSQL.
A aplicação evita duplicidades, ignora cupons cancelados e permite consultas ordenadas por número do CF-e ou valor total do cupom.

⚙️ Funcionalidades

- Processa diversos XMLs de CF-e SAT contidos em um .zip ou em uma pasta.
- Extrai dados fiscais e itens de cada cupom.
- Persiste as informações em banco de dados relacional (PostgreSQL).
- Evita duplicidade de cupons com base na chave de acesso.
- Ignora automaticamente cupons cancelados.
- Permite listar cupons ordenados por:
    Número do CF-e
    Valor total


Para Executar é necessáro:
- Docker -> Para o banco de Dados

- Spring

Comandos:

- Rodar o Docker:
    docker-compose up -d

- Ver status do docker
    docker ps

- Rodar Spring:
   mvn spring-boot:run

- Rodar testes:
    mvn test -Dtest=GlobalExceptionHandlerTest
    mvn test -Dtest=XmlSatParserTest
    mvn test -Dtest=CupomServiceTest

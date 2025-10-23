Para Executar é necessáro:

Docker -> Para o banco de Dados

Spring

Comandos:

Rodar o Docker:
docker-compose up -d

Ver status do docker
docker ps

Rodar Spring:
mvn spring-boot:run

Rodar testes:
mvn test -Dtest=GlobalExceptionHandlerTest

mvn test -Dtest=XmlSatParserTest

mvn test -Dtest=CupomServiceTest

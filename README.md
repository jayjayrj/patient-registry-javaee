# Patient Registry

Aplicação web para cadastro de pacientes, criada como migração de um fluxo legado Struts 2 para a stack Jakarta EE. Permite incluir, listar, filtrar, ordenar, editar e excluir pacientes, além de calcular a idade por meio de uma procedure Oracle.

## Stack

- Java 11
- Maven 3.8+
- WildFly 24 (Jakarta EE 9.1)
- Jakarta Faces 3.0 e CDI
- PrimeFaces 12
- JPA 3.0 / Hibernate
- Oracle 11g ou 19c
- JUnit 5 e Mockito

## Arquitetura

O projeto separa responsabilidades em quatro camadas:

- `domain`: entidade JPA e regras declarativas de validação.
- `persistence`: operações de persistência de pacientes.
- `service`: integração isolada com a procedure Oracle.
- `application`: bean JSF responsável pelo estado da tela e pelas mensagens ao usuário.

O WAR não empacota as APIs Jakarta EE, pois elas são fornecidas pelo WildFly.

## Pré-requisitos

1. JDK 11 configurado em `JAVA_HOME`.
2. Maven 3.8 ou superior.
3. WildFly 24.0.1.Final.
4. Oracle 11g/19c acessível e o driver JDBC instalado no WildFly.

## Banco de dados

Execute [`database/oracle/schema.sql`](database/oracle/schema.sql) no schema da aplicação. O script cria a tabela, a sequência, a procedure `P_PATIENT_AGE` e dois registros de demonstração.

Exemplo de instalação do driver e datasource via WildFly CLI (ajuste os caminhos e credenciais):

```bash
module add --name=com.oracle.ojdbc --resources=/caminho/ojdbc8.jar --dependencies=jakarta.api,jakarta.transaction.api
/subsystem=datasources/jdbc-driver=oracle:add(driver-name=oracle,driver-module-name=com.oracle.ojdbc,driver-class-name=oracle.jdbc.OracleDriver)
data-source add --name=PatientRegistryDS --jndi-name=java:jboss/datasources/PatientRegistryDS --driver-name=oracle --connection-url=jdbc:oracle:thin:@localhost:1521/XEPDB1 --user-name=patient_registry --password=patient_registry --enabled=true
```

Valide a conexão:

```bash
/subsystem=datasources/data-source=PatientRegistryDS:test-connection-in-pool
```

> A aplicação não executa DDL automaticamente. Essa decisão evita alterações inesperadas em ambientes compartilhados e mantém a procedure sob versionamento.

## Compilar e testar

```bash
mvn clean verify
```

O comando executa os testes unitários e gera `target/patient-registry.war`.

## Executar no WildFly

Inicie o servidor e faça o deploy:

```bash
$WILDFLY_HOME/bin/standalone.sh
$WILDFLY_HOME/bin/jboss-cli.sh --connect --command="deploy target/patient-registry.war --force"
```

No Windows, use os arquivos equivalentes `.bat`. A aplicação estará disponível em:

```text
http://localhost:8080/patient-registry/
```

## Funcionalidades e validações

- DataTable responsiva, paginada, filtrável, ordenável e editável em linha.
- Cadastro e exclusão com atualização Ajax.
- Confirmação antes da exclusão.
- Nome obrigatório entre 3 e 120 caracteres.
- Data de nascimento obrigatória e no passado.
- Cálculo de idade pela procedure `P_PATIENT_AGE`, exibido em Growl.
- Layout responsivo para desktop e dispositivos móveis.

## Testes e integração contínua

Os testes cobrem os caminhos de inclusão, atualização, listagem e remoção do DAO, além do contrato completo da chamada à procedure (parâmetros IN/OUT, execução e retorno). Dependências de infraestrutura são simuladas para tornar a suíte rápida e determinística.

O workflow em `.github/workflows/ci.yml` executa `mvn verify` com Java 11 em pushes e pull requests.

## Decisões técnicas

- `LocalDate` representa datas sem fuso horário e evita ambiguidades de nascimento.
- A procedure usa `MONTHS_BETWEEN / 12` e truncamento, respeitando aniversários ainda não ocorridos no ano.
- O `EntityManager` é gerenciado pelo contêiner, com transações fornecidas pelo EJB.
- O banco é acessado por JNDI, mantendo credenciais fora do artefato.

## Licença

Distribuído sob a licença [MIT](LICENSE).

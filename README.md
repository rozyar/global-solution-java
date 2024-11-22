
# Calculadora de Painéis Solares

## Introdução

A Calculadora de Painéis Solares é uma aplicação web projetada para ajudar usuários a determinar o número de painéis solares de 250W necessários para atender ao consumo diário de energia. Ao inserir seus eletrodomésticos e a média de horas de sol por dia, os usuários recebem um cálculo preciso alimentado pela API GPT da OpenAI.

Esta aplicação é desenvolvida com Spring Boot e utiliza diversos módulos Spring, como Spring Data JPA, Spring Security, além de integrar recursos de IA com a API GPT da OpenAI.

## Funcionalidades

- **Cadastro e Autenticação de Usuários**: Funcionalidade segura de registro e login com criptografia de senhas.
- **Gerenciamento de Eletrodomésticos**: Adicione, edite e exclua eletrodomésticos e seus consumos energéticos.
- **Cálculo de Painéis Solares**: Calcule o número de painéis solares necessários com base no consumo total de energia e nas horas de sol.
- **Histórico de Análises**: Visualize análises anteriores com paginação para fácil navegação.
- **Integração com IA**: Utiliza a API GPT da OpenAI para cálculos precisos e inteligentes.
- **Validação de Entrada**: Garante que todas as entradas do usuário sejam válidas utilizando Bean Validation.
- **Aplicação Segura**: Implementa Spring Security para autenticação e autorização.
- **Implantação na Nuvem**: A aplicação está implantada em uma plataforma de nuvem para acessibilidade.

## Pré-requisitos

- **Java 17** ou superior
- **Maven 3.6+**
- **Uma chave de API da OpenAI**
- **Banco de Dados Oracle** (ou modifique para outro banco de dados)
- **Conexão com a Internet**

## Instalação

### 1. Clonar o Repositório

```bash
git clone https://github.com/yourusername/solar-panel-calculator.git
cd solar-panel-calculator
```

### 2. Configuração do Banco de Dados

A aplicação utiliza um banco de dados Oracle. Atualize o arquivo `application.properties` localizado em `src/main/resources/application.properties` com suas credenciais de banco de dados:

```properties
spring.datasource.url=jdbc:oracle:thin:@your_oracle_db_url
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
```

Se preferir usar outro banco de dados (e.g., MySQL, PostgreSQL), atualize a URL JDBC, a classe do driver e o dialeto adequadamente.

### 3. Inserir Chave da API OpenAI

Para habilitar os cálculos com inteligência artificial, você precisa fornecer sua chave da API OpenAI.

#### Opção 1: Diretamente no `AiService.java`

1. Abra o arquivo `AiService.java` localizado em `src/main/java/com/example/solarpanelcalculator/service/AiService.java`.
2. Modifique o construtor para injetar a chave da API a partir das propriedades da aplicação:

```java
@Service
public class AIService {

    private final OpenAiService openAiService;

    public AIService(@Value("${openai.api.key}") String apiKey) {
        this.openAiService = new OpenAiService(apiKey);
    } 
    // ...
}
```

#### Opção 2: No arquivo `application.properties`

1. Abra o arquivo `application.properties` localizado em `src/main/resources/application.properties`.
2. Adicione sua chave da API OpenAI:

```properties
openai.api.key=your_openai_api_key_here
```

**Nota:** Não compartilhe ou comprometa sua chave de API em repositórios públicos.

### 4. Construir o Projeto

Utilize o Maven para construir o projeto:

```bash
mvn clean install
```

## Executando a Aplicação

### Usando o Maven

Execute a aplicação com:

```bash
mvn spring-boot:run
```

### Usando sua IDE

Importe o projeto na sua IDE (e.g., IntelliJ IDEA, Eclipse) e execute a classe principal `SolarPanelCalculator`.

### Acessando a Aplicação

Abra seu navegador e acesse:

```
http://localhost:8080/
```

## Uso

1. **Registrar uma Conta**
   - Clique no link "Registrar".
   - Preencha seu nome, e-mail e senha.
   - Envie o formulário para criar sua conta.

2. **Login**
   - Use seu e-mail registrado e senha para fazer login.

3. **Gerenciar Eletrodomésticos**
   - Navegue até "Eletrodomésticos".
   - Adicione seus eletrodomésticos e insira o consumo energético em watts.
   - Edite ou exclua eletrodomésticos conforme necessário.

4. **Calcular Painéis Solares**
   - Vá até "Calcular Painéis".
   - Insira a média de horas de sol diárias na sua localização.
   - Envie o formulário para obter o cálculo.

5. **Ver Histórico de Análises**
   - Navegue até "Histórico de Análises".
   - Visualize seus cálculos anteriores com detalhes.
   - Use os controles de paginação para navegar pelos registros.

## Cumprimento dos Requisitos do Projeto

- **Anotações do Spring para Configuração e Injeção de Dependências**: Utilização extensiva de anotações Spring como `@Autowired`, `@Service`, `@Repository`, `@Controller` e `@Configuration`.
- **Camada de Modelo/DTO com Métodos de Acesso Adequados**: Todas as classes de modelo possuem campos privados com getters e setters públicos.
- **Persistência de Dados com Spring Data JPA**: Repositórios estendem `JpaRepository`, permitindo operações CRUD e paginação.
- **Validação com Bean Validation**: Entradas de usuários são validadas utilizando anotações como `@NotEmpty`, `@Positive`, `@Email` com mensagens customizadas.
- **Paginação para Recursos com Muitos Registros**: Paginação implementada nas visualizações de eletrodomésticos e análises usando `Pageable` e `Page`.
- **Spring Security para Autenticação e Autorização**: Implementado com `SecurityConfig`, fornecendo login, registro e controle de acesso seguros.
- **Tratamento de Erros e Exceções**: Erros de validação e exceções são tratados adequadamente, fornecendo mensagens amigáveis aos usuários.
- **Recursos de IA Generativa com Spring AI**: Integração da API GPT da OpenAI para cálculos inteligentes.
- **Implantação na Nuvem**: Aplicação implantada em plataforma de nuvem para acessibilidade.
- **Uso Adequado de Verbos HTTP e Códigos de Status**: Endpoints RESTful utilizam métodos HTTP adequados (`GET`, `POST`, `PUT`, `DELETE`) e retornam códigos de status apropriados.

**Nota sobre Mensageria Assíncrona**  
Devido ao prazo, a mensageria com filas assíncronas não foi implementada nesta versão. Esse recurso está planejado para futuras atualizações.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot**
- **Spring Data JPA**
- **Spring Security**
- **Thymeleaf**
- **API GPT da OpenAI**
- **Banco de Dados Oracle**
- **Maven**

## Equipe

- **Razyel Ferrari (RM551875)** - GitHub: [irazyel](https://github.com/irazyel)
- **Rayzor Anael (RM551832)** - GitHub: [rozyar](https://github.com/rozyar)
- **Derick Araújo (RM551007)** - GitHub: [dericki](https://github.com/dericki)
- **Kalel Schlichting (RM550620)** - GitHub: [K413l](https://github.com/K413l)

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).

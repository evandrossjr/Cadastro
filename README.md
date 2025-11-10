<p align="center">
  <img src="https://img.shields.io/badge/Spring%20Boot-3.0-brightgreen?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Thymeleaf-3.1-brightgreen?style=for-the-badge&logo=thymeleaf&logoColor=white" alt="Thymeleaf" />
  <img src="https://img.shields.io/badge/Spring%20Security-secure-blue?style=for-the-badge&logo=springsecurity&logoColor=white" alt="Spring Security" />
  <img src="https://img.shields.io/badge/Bootstrap-5-purple?style=for-the-badge&logo=bootstrap&logoColor=white" alt="Bootstrap" />
</p>

<h1 align="center">Cadastro de Clientes - C.A</h1>

<p align="center">
  Sistema Full-Stack de Gerenciamento de Clientes (CRM) construído com <b>Java</b> e <b>Spring Boot</b>.<br>
  Inclui autenticação, CRUD completo, e geração de relatórios em PDF. 
</p>

---

## Funcionalidades

### Autenticação
- Sistema completo de **Login e Registro** de usuários.  
- **Autenticação baseada em sessão** (cookies) com Spring Security.  
- **Autorização baseada em papéis** (`ADMIN`, `REGULAR`).  
- Página de **"Editar Senha"** para o usuário logado.  

---

### Clientes (CRUD)
- **Cadastro, Leitura, Atualização e Exclusão (CRUD)** de Clientes.  
- **Validação de formulário** (ex: “E-mail já cadastrado”).  
- **Validação inteligente** de e-mail duplicado (ignora o e-mail do próprio usuário ao editar).  

---

### Contatos (CRUD)
- CRUD completo de **Contatos**.  
- **Validação de exclusão** para impedir que contatos vinculados a clientes sejam apagados (Integridade Referencial).  

---

### Relacionamentos
- **Relacionamento Many-to-Many** entre Clientes e Contatos.  
- Formulário de cadastro/edição com campo **`<select multiple>`** para vincular contatos.  
- **Listagem aninhada (accordion)** para mostrar contatos de cada cliente.  

---

### Relatórios
- **Geração de Relatórios** de Clientes e Contatos em tela.  
- **Exportação em PDF** com **Flying Saucer** (`org.xhtmlrenderer`).  

---

## Stack de Tecnologia

### Backend
- Java 17+  
- Spring Boot 3+  
- Spring MVC (Web)  
- Spring Data JPA (Hibernate)  
- Spring Security  

### Frontend
- Thymeleaf (Server-Side Rendering)  
- HTML5 / CSS3 (Flexbox)  
- Bootstrap 5  
- JavaScript  

### Banco de Dados
- **H2 Database** (desenvolvimento e testes)  
- **PostgreSQL** (produção)  

### Geração de PDF
- **Flying Saucer** (`org.xhtmlrenderer`)  

---

## Como Rodar o Projeto

### Pré-requisitos
- **JDK 17** ou superior  
- **Apache Maven**  
- Uma IDE (IntelliJ, VS Code, Eclipse, etc.)

---

### Clone o Repositório
```bash
git clone https://github.com/evandrossjr/Cadastro.git
```

### Banco de Dados
- O projeto está rodando com o o banco em H2 em memória (spring.jpa.hibernate.ddl-auto=create) junto com um arquivo DataLoader.java para popular o banco de dados na inicialização.

- Configuração para PostgreSQl
```
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

### Execute a Aplicação
- Via IDE abra a classe principal CadastroApplication.java e clique em **Run**
- Via Maven (terminal)
```
- mvn spring-boot:run
```

### Acesse o sistema
- Acesse pelo navegador
  -- http://localhost:8080

---

## Usuários de Demonstração

O sistema cria automaticamente dois usuários (via DataLoader.java) caso o banco esteja vazio:

 Tipo de Usuário | E-mail | Senha |
|:---------------:|:------:|:-----:|
| **Admin**  | admin@email.com | admin122 |
| **Regular** | usuario@email.com | senha123 |

---

## Autor

- Evandro Sacramento
- https://github.com/evandrossjr
- evandro-dev@outlook.com

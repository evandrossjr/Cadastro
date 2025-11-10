package com.essjr.Cadastro;

import com.essjr.Cadastro.AppUser.AppUser;
import com.essjr.Cadastro.AppUser.AppUserRepository;
import com.essjr.Cadastro.AppUser.enums.AppUserRole;
import com.essjr.Cadastro.Cliente.Cliente;
import com.essjr.Cadastro.Cliente.ClienteRepository;
import com.essjr.Cadastro.Contato.Contato;
import com.essjr.Cadastro.Contato.ContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class DataLoader implements CommandLineRunner {




    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ClienteRepository clienteRepository;
    private final ContatoRepository contatoRepository;

    public DataLoader(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, ClienteRepository clienteRepository, ContatoRepository contatoRepository) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.clienteRepository = clienteRepository;
        this.contatoRepository = contatoRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        AppUser admin = new AppUser();
        admin.setName("Usuário Admin");
        admin.setEmail("admin@email.com");
        admin.setPasswordHash(passwordEncoder.encode("admin122"));
        admin.setRole(AppUserRole.ADMIN);

        AppUser regular = new AppUser();
        regular.setName("Usuário de Teste");
        regular.setEmail("usuario@email.com");
        regular.setPasswordHash(passwordEncoder.encode("senha123"));
        regular.setRole(AppUserRole.REGULAR);



        appUserRepository.saveAll(List.of(admin, regular));

        if (clienteRepository.count() == 0 && contatoRepository.count() == 0) {

            // Etapa A: Criar e Salvar os 15 Contatos
            List<Contato> savedContatos = createAndSaveContatos();

            // Etapa B: Criar e Salvar os 15 Clientes
            List<Cliente> savedClientes = createAndSaveClientes();

            // Etapa C: Ligar os Clientes e Contatos (Criar as relações)
            linkClientesContatos(savedClientes, savedContatos);

            System.out.println("=========================================");
            System.out.println("Data Loader: Clientes e Contatos carregados.");
            System.out.println("=========================================");
        }
    }


    private List<Contato> createAndSaveContatos() {
        List<Contato> contatos = List.of(
                new Contato("Fabio Alex", "fabio.alex@email.com", "fabio.alex@email.com", "71991213095", "71991213098"),
                new Contato("Mariana Souza", "mariana@email.com", "mariana.souza@email.com", "71991345022", "71991887766"),
                new Contato("Paulo César", "paulo@email.com", "paulo.cesar@email.com", "71991555522", "71991774455"),
                new Contato("Renata Lima", "renata@email.com", "renata.lima@email.com", "71991889977", "71991224477"),
                new Contato("Carlos Eduardo", "carlos@email.com", "cadu@email.com", "71991113344", "71991995533"),
                new Contato("Juliana Reis", "juliana@email.com", "juliana.reis@email.com", "71991228844", "7199133955"),
                new Contato("Felipe Costa", "felipe@email.com", "felipe.costa@email.com", "71991447788", "71991889944"),
                new Contato("Tatiane Rocha", "tatiane@email.com", "tatiane.rocha@email.com", "71991775522", "71991337755"),
                new Contato("Bruno Henrique", "bruno@email.com", "bruno.henrique@icloud.com", "71991992233", "71991223344"),
                new Contato("Larissa Melo", "larissa@email.com", "larissa.melo@email.com", "71991112233", "71991998877"),
                new Contato("Thiago Ramos", "thiago@email.com", "thiago.ramos@email.com", "71991887766", "71991331122"),
                new Contato("Amanda Oliveira", "amanda@email.com", "amanda.oliveira@email.com", "71991772233", "71991669988"),
                new Contato("Lucas Martins", "lucas@email.com", "lucas.martins@email.com", "71991553322", "71991336655"),
                new Contato("Fernanda Alves", "fernanda@email.com", "fernanda.alves@email.com", "71991995522", "71991225544"),
                new Contato("Diego Silva", "diego@email.com", "diego.silva@email.com", "71991886633", "71991114455")
        );
        return contatoRepository.saveAll(contatos);
    }

    private List<Cliente> createAndSaveClientes() {
        List<Cliente> clientes = List.of(
                new Cliente("Rafael Almeida", "rafael.almeida@email.com", "rafael.a@email.com", "71991213095", "71991213098", LocalDate.parse("2025-01-25")),
                new Cliente("Gabriela Lima", "gabriela.lima@email.com", "gabi.lima@email.com", "71991345022", "71991887766", LocalDate.parse("2025-02-12")),
                new Cliente("Bruno Costa", "bruno.costa@email.com", "bruno.c@email.com", "71991555522", "71991774455", LocalDate.parse("2025-03-18")),
                new Cliente("Beatriz Santos", "beatriz.santos@email.com", "bia.santos@email.com", "71991889977", "71991224477", LocalDate.parse("2025-03-21")),
                new Cliente("Lucas Oliveira", "lucas.oliveira@email.com", "lucas.o@email.com", "71991113344", "71991995533", LocalDate.parse("2025-03-25")),
                new Cliente("Letícia Pereira", "leticia.pereira@email.com", "lele.pereira@email.com", "71991228844", "7199133955", LocalDate.parse("2025-04-03")),
                new Cliente("Matheus Ferreira", "matheus.ferreira@email.com", "matheus.f@email.com", "71991447788", "71991889944", LocalDate.parse("2025-04-10")),
                new Cliente("Camila Rodrigues", "camila.rodrigues@email.com", "camila.rocha@email.com", "71991775522", "7199133755", LocalDate.parse("2025-04-18")),
                new Cliente("Gustavo Martins", "gustavo.martins@email.com", "guga.martins@icloud.com", "71991992233", "71991223344", LocalDate.parse("2025-05-02")),
                new Cliente("Amanda Barbosa", "amanda.barbosa@email.com", "amanda.b@email.com", "71991112233", "71991998877", LocalDate.parse("2025-05-09")),
                new Cliente("Vinícius Ribeiro", "vinicius.ribeiro@email.com", "vini.ribeiro@email.com", "71991887766", "71991331122", LocalDate.parse("2025-05-17")),
                new Cliente("Sofia Gonçalves", "sofia.goncalves@email.com", "sofia.g@email.com", "71991772233", "71991669988", LocalDate.parse("2025-05-28")),
                new Cliente("Daniel Carvalho", "daniel.carvalho@email.com", "daniel.c@email.com", "71991553322", "71991336655", LocalDate.parse("2025-06-05")),
                new Cliente("Isabella Moreira", "isabella.moreira@email.com", "isa.moreira@email.com", "71991995522", "71991225544", LocalDate.parse("2025-06-14")),
                new Cliente("Leonardo Nunes", "leonardo.nunes@email.com", "leo.nunes@email.com", "71991886633", "71991114455", LocalDate.parse("2025-06-21"))
        );
        return clienteRepository.saveAll(clientes);
    }

    /**
     * Assume que sua entidade Cliente tem um 'Set<Contato> contatos'
     * e que a relação é @ManyToMany
     */
    private void linkClientesContatos(List<Cliente> clientes, List<Contato> contatos) {
        // Mapeamento baseado no seu SQL (cliente_id, contato_id)
        // Os índices da lista (ex: get(0)) correspondem aos IDs (ex: 1)
        // porque o banco foi criado do zero (ddl-auto=create)

        clientes.get(0).getContatos().addAll(List.of(contatos.get(0), contatos.get(1))); // C1 -> K1, K2
        clientes.get(1).getContatos().addAll(List.of(contatos.get(2), contatos.get(3))); // C2 -> K3, K4
        clientes.get(2).getContatos().addAll(List.of(contatos.get(4), contatos.get(5))); // C3 -> K5, K6
        clientes.get(3).getContatos().addAll(List.of(contatos.get(1), contatos.get(6))); // C4 -> K2, K7
        clientes.get(4).getContatos().addAll(List.of(contatos.get(4), contatos.get(7))); // C5 -> K5, K8
        clientes.get(5).getContatos().add(contatos.get(8)); // C6 -> K9
        clientes.get(6).getContatos().add(contatos.get(9)); // C7 -> K10
        clientes.get(7).getContatos().addAll(List.of(contatos.get(5), contatos.get(11))); // C8 -> K6, K12
        clientes.get(8).getContatos().addAll(List.of(contatos.get(1), contatos.get(12))); // C9 -> K2, K13
        clientes.get(9).getContatos().add(contatos.get(13)); // C10 -> K14
        clientes.get(10).getContatos().add(contatos.get(10)); // C11 -> K11
        clientes.get(11).getContatos().add(contatos.get(14)); // C12 -> K15
        clientes.get(12).getContatos().addAll(List.of(contatos.get(6), contatos.get(9))); // C13 -> K7, K10
        clientes.get(13).getContatos().addAll(List.of(contatos.get(7), contatos.get(11))); // C14 -> K8, K12
        clientes.get(14).getContatos().addAll(List.of(contatos.get(12), contatos.get(14))); // C15 -> K13, K15

        // Salva as alterações (o JPA/Hibernate vai preencher a tabela cliente_contato)
        clienteRepository.saveAll(clientes);

        System.out.println("Usuários de teste salvos com sucesso!");
    };

}

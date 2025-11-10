package com.essjr.Cadastro.Cliente;


import com.essjr.Cadastro.AppUser.AppUser;
import com.essjr.Cadastro.AppUser.AppUserRepository;
import com.essjr.Cadastro.AppUser.dtos.AppUserDTO;
import com.essjr.Cadastro.AppUser.dtos.AppUserLogadoDTO;
import com.essjr.Cadastro.Cliente.ClienteService;
import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.Contato.Contato;
import com.essjr.Cadastro.Contato.ContatoService;
import com.essjr.Cadastro.Contato.dtos.ContatoDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/cliente")
class ClienteWebController {


    private final ClienteService clienteService;
    private final ContatoService contatoService;
    private final ClienteRepository clienteRepository;
    private final AppUserRepository appUserRepository;

    ClienteWebController(ClienteService clienteService, ContatoService contatoService, ClienteRepository clienteRepository, AppUserRepository appUserRepository) {
        this.clienteService = clienteService;
        this.contatoService = contatoService;
        this.clienteRepository = clienteRepository;
        this.appUserRepository = appUserRepository;
    }


    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {

        List<ContatoDTO> todosOsContatos = contatoService.findAll();
        model.addAttribute("todosOsContatos", todosOsContatos);

        model.addAttribute("conteudo", "cadastroCliente");
        model.addAttribute("titulo", "Cadastro de Clientes");
        model.addAttribute("cliente", new ClienteDTO(null, "", "", "", "", "",null,null));
        return "layout";
    }

    @PostMapping("/cadastro")
    public String salvarViaFormulario(@ModelAttribute ClienteDTO dto, RedirectAttributes redirectAttributes, Model model) {
        try {
            clienteService.insert(dto);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente \"" + dto.nomeCompleto() + "\" cadastrado com sucesso!");
            return "redirect:/cliente/cadastro";
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar cliente: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao cadastrar cliente: " + e.getMessage());
            model.addAttribute("cliente", dto); // importante: mesmo nome usado no formulário
            model.addAttribute("titulo", "Cadastro de Clientes");
            model.addAttribute("conteudo", "cadastroCliente"); // caminho do template parcial
            model.addAttribute("todosOsContatos", contatoService.findAll()); // se houver select de contatos

            return "layout"; // volta pelo layout
        }
    }

    @GetMapping("/lista")
    public String ListarClientes(Model model, @AuthenticationPrincipal AppUser appUser) {

        var dto = new AppUserLogadoDTO(appUser.getName(), appUser.getEmail());

        model.addAttribute("usuarioLogado", dto);
        model.addAttribute("titulo", "Lista de Clientes");
        model.addAttribute("conteudo", "listaClientes");

        List<Cliente> clientes = clienteRepository.findAllWithContatos();

        model.addAttribute("clientes", clientes);

        return "layout";
    }

    @GetMapping("/cliente/{clienteId}/contatos")
    public String listarContatosDoCliente(@PathVariable Long clienteId, Model model) {

        // 1. Busca o cliente (como DTO)
        ClienteDTO cliente = clienteService.findById(clienteId);

        // 2. Busca os DTOs dos contatos, não apenas os IDs
        List<ContatoDTO> contatosDoCliente;
        if (cliente.contatosIds() != null && !cliente.contatosIds().isEmpty()) {
            // Usa o novo método do ContatoService
            contatosDoCliente = contatoService.findAllByIds(cliente.contatosIds());
        } else {
            contatosDoCliente = new ArrayList<>(); // Lista vazia
        }

        // 3. Envia os DADOS COMPLETOS para o model
        model.addAttribute("cliente", cliente);
        model.addAttribute("contatos", contatosDoCliente); // <-- Agora é uma List<ContatoDTO>
        model.addAttribute("titulo", "Contatos de " + cliente.nomeCompleto());
        model.addAttribute("conteudo", "listaContatosDoCliente");
        return "layout";
    }




    /**
     * Exibe o formulário de EDIÇÃO de cliente.
     * @param id O ID do cliente vindo da URL
     * @param model O Model para enviar dados ao Thymeleaf
     * @return O nome do layout
     */
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable Long id, Model model) {

        ClienteDTO clienteDTO = clienteService.findById(id);

        List<ContatoDTO> todosOsContatos = contatoService.findAll();

        model.addAttribute("cliente", clienteDTO);
        model.addAttribute("todosOsContatos", todosOsContatos);

        model.addAttribute("conteudo", "cadastroCliente");
        model.addAttribute("titulo", "Editar Cliente");

        return "layout";
    }

    @PostMapping("/editar/{id}")
    public String processarFormularioEdicao(
            @PathVariable Long id,
            @Valid @ModelAttribute("cliente") ClienteDTO dto,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Se houver erros de validação (ex: nome em branco)
        if (bindingResult.hasErrors()) {
            model.addAttribute("erro", "Verifique os campos.");
            model.addAttribute("todosOsContatos", contatoService.findAll()); // Repopula o <select>
            model.addAttribute("conteudo", "cadastroCliente");
            model.addAttribute("titulo", "Editar Cliente");
            return "layout";
        }

        try {
            clienteService.update(id, dto);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso!");
            return "redirect:/cliente/lista";

        } catch (Exception e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("todosOsContatos", contatoService.findAll());
            model.addAttribute("conteudo", "cadastroCliente");
            model.addAttribute("titulo", "Editar Cliente");
            return "layout";
        }
    }


    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
        return "redirect:/cliente/lista";
    }


}

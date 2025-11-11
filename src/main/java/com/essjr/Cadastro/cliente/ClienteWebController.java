package com.essjr.Cadastro.cliente;


import com.essjr.Cadastro.appUser.AppUser;
import com.essjr.Cadastro.appUser.AppUserRepository;
import com.essjr.Cadastro.appUser.dtos.AppUserLogadoDTO;
import com.essjr.Cadastro.cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.contato.ContatoService;
import com.essjr.Cadastro.contato.dtos.ContatoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "Clientes", description = "Gerencia o cadastro de clientes")
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


    /**
     * Retorna a tela de CADASTRO de clientes.
     *
     * @param model O Model usado para enviar dados ao Thymeleaf.
     * @return O layout contendo o formulário de cadastro de clientes.
     */
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {

        List<ContatoDTO> todosOsContatos = contatoService.findAll();
        model.addAttribute("todosOsContatos", todosOsContatos);

        model.addAttribute("conteudo", "cadastroCliente");
        model.addAttribute("titulo", "Cadastro de Clientes");
        model.addAttribute("cliente", new ClienteDTO(null, "", "", "", "", "",null,null));
        return "layout";
    }


    /**
     * Processa o envio do formulário de CADASTRO de clientes.
     *
     * @param dto Os dados do cliente enviados pelo formulário.
     * @param redirectAttributes Usado para enviar mensagens de sucesso ou erro após redirecionamento.
     * @param model O Model para recarregar a página em caso de erro.
     * @return Redirecionamento ou renderização do layout conforme o resultado.
     */
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


    /**
     * Exibe a lista de clientes cadastrados.
     *
     * @param model O Model usado para enviar dados ao Thymeleaf.
     * @param appUser O usuário autenticado para exibição no cabeçalho.
     * @return O layout contendo a lista de clientes.
     */
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


    /**
     * Exibe os contatos associados a um cliente específico.
     *
     * @param clienteId O ID do cliente cujos contatos serão listados.
     * @param model O Model usado para enviar dados ao Thymeleaf.
     * @return O layout contendo a lista de contatos do cliente.
     */
    @GetMapping("/cliente/{clienteId}/contatos")
    public String listarContatosDoCliente(@PathVariable Long clienteId, Model model) {

        ClienteDTO cliente = clienteService.findById(clienteId);

        List<ContatoDTO> contatosDoCliente;
        if (cliente.contatosIds() != null && !cliente.contatosIds().isEmpty()) {
            contatosDoCliente = contatoService.findAllByIds(cliente.contatosIds());
        } else {
            contatosDoCliente = new ArrayList<>();
        }

        model.addAttribute("cliente", cliente);
        model.addAttribute("contatos", contatosDoCliente);
        model.addAttribute("titulo", "Contatos de " + cliente.nomeCompleto());
        model.addAttribute("conteudo", "listaContatosDoCliente");
        return "layout";
    }


    /**
     * Exibe o formulário para edição de um cliente.
     *
     * @param id O ID do cliente a ser editado.
     * @param model O Model usado para enviar dados ao Thymeleaf.
     * @return O layout contendo o formulário de edição do cliente.
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


    /**
     * Processa o envio do formulário de edição de cliente.
     *
     * @param id O ID do cliente a ser atualizado.
     * @param dto Os novos dados do cliente.
     * @param bindingResult Resultado da validação do formulário.
     * @param model O Model para recarregar dados em caso de erro.
     * @param redirectAttributes Usado para mensagens de feedback.
     * @return Redirecionamento ou renderização do layout conforme o resultado.
     */
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

    /**
     * Exclui um cliente pelo ID informado.
     *
     * @param id O ID do cliente a ser excluído.
     * @param redirectAttributes Usado para enviar mensagens de sucesso após a exclusão.
     * @return Redirecionamento para a lista de clientes.
     */
    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
        return "redirect:/cliente/lista";
    }


}

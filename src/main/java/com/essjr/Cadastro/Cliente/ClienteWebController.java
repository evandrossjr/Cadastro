package com.essjr.Cadastro.Cliente;


import com.essjr.Cadastro.Cliente.ClienteService;
import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.Contato.Contato;
import com.essjr.Cadastro.Contato.ContatoService;
import com.essjr.Cadastro.Contato.dtos.ContatoDTO;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    ClienteWebController(ClienteService clienteService, ContatoService contatoService, ClienteRepository clienteRepository) {
        this.clienteService = clienteService;
        this.contatoService = contatoService;
        this.clienteRepository = clienteRepository;
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
    public String ListarClientes(Model model) {

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


    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        ClienteDTO cliente = clienteService.findById(id);

        model.addAttribute("conteudo", "editarCliente");
        model.addAttribute("cliente", cliente);
        model.addAttribute("todosOsContatos", contatoService.findAll());

        return "layout";
    }

    @PostMapping("/editar/{id}")
    public String atualizarCliente(
            @PathVariable Long id,
            @ModelAttribute ClienteDTO dto,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            clienteService.update(id, dto);
            redirectAttributes.addFlashAttribute("mensagem", "Cliente atualizado com sucesso!");
            return "redirect:/cliente/editar/" + id;

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar cliente: " + e.getMessage());
            model.addAttribute("cliente", dto);
            model.addAttribute("todosOsContatos", contatoService.findAll());
            return "editarCliente";
        }
    }


    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        clienteService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente excluído com sucesso!");
        return "redirect:/cliente/lista";
    }

}

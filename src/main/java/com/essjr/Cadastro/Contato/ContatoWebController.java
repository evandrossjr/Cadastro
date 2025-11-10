package com.essjr.Cadastro.Contato;


import com.essjr.Cadastro.AppUser.AppUser;
import com.essjr.Cadastro.AppUser.dtos.AppUserLogadoDTO;
import com.essjr.Cadastro.Cliente.Cliente;
import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;
import com.essjr.Cadastro.Contato.dtos.ContatoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/contato")
public class ContatoWebController {


    private final ContatoService contatoService;
    private final ContatoRepository contatoRepository;

    public ContatoWebController(ContatoService contatoService, ContatoRepository contatoRepository) {
        this.contatoService = contatoService;
        this.contatoRepository = contatoRepository;
    }


    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("conteudo", "cadastroContato");
        model.addAttribute("titulo", "Cadastro de Contatos");
        model.addAttribute("contato", new ContatoDTO(null, "", "", "", "", ""));
        return "layout";
    }

    @PostMapping("/cadastro")
    public String salvarViaFormulario(@ModelAttribute ContatoDTO dto, RedirectAttributes redirectAttributes, Model model) {
        try {
            contatoService.insert(dto);
            redirectAttributes.addFlashAttribute("mensagem", "Contato \"" + dto.nomeCompleto() + "\" cadastrado com sucesso!");
            return "redirect:/contato/cadastro";
        } catch (Exception e) {
            System.err.println("Erro ao cadastrar contato: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("erro", "Erro ao cadastrar contato: " + e.getMessage());
            model.addAttribute("contato", dto); // importante: mesmo nome usado no formulário
            model.addAttribute("titulo", "Cadastro de Contato");
            model.addAttribute("conteudo", "cadastroContato"); // caminho do template parcial

            return "layout"; // volta pelo layout
        }
    }
    @GetMapping("/lista")
    public String ListarContatos(Model model, @AuthenticationPrincipal AppUser appUser) {

        var dto = new AppUserLogadoDTO(appUser.getName(), appUser.getEmail());

        model.addAttribute("usuarioLogado", dto);

        model.addAttribute("titulo", "Lista de Contatos");
        model.addAttribute("conteudo", "listaContatos");

        List<ContatoDTO> contatos = contatoService.findAll();

        model.addAttribute("contatos", contatos);

        return "layout";
    }


    @GetMapping("/editar/{id}")
    public String editarContato(@PathVariable Long id, Model model) {
        ContatoDTO contato = contatoService.findById(id);

        model.addAttribute("conteudo", "cadastroContato");
        model.addAttribute("contato", contato);

        return "layout";
    }

    @PostMapping("/editar/{id}")
    public String atualizarCliente(
            @PathVariable Long id,
            @ModelAttribute ContatoDTO dto,
            RedirectAttributes redirectAttributes,
            Model model) {

        try {
            contatoService.update(id, dto);
            redirectAttributes.addFlashAttribute("mensagem", "Contato atualizado com sucesso!");
            return "redirect:/contato/editar/" + id;

        } catch (Exception e) {
            model.addAttribute("erro", "Erro ao atualizar contato: " + e.getMessage());
            model.addAttribute("contato", dto);
            return "layout";


        }
    }

    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contatoService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "Contato excluído com sucesso!");
        return "redirect:/cliente/lista";
    }


}

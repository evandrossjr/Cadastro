package com.essjr.Cadastro.Contato;


import com.essjr.Cadastro.Contato.dtos.ContatoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequestMapping("/contato")
public class ContatoWebController {

    @Autowired
    private ContatoService contatoService;



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


}

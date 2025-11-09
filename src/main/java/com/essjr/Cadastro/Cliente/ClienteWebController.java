package com.essjr.Cadastro.Cliente;


import com.essjr.Cadastro.Cliente.ClienteService;
import com.essjr.Cadastro.Cliente.dtos.ClienteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/cliente")
class ClienteWebController {

    @Autowired
    private ClienteService clienteService;



    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("conteudo", "cadastroCliente");
        model.addAttribute("titulo", "Cadastro de Clientes");
        model.addAttribute("cliente", new ClienteDTO(null, "", "", "", "", "",null));
        return "layout";
    }

    @PostMapping("/cadastro")
    public String salvarViaFormulario(@ModelAttribute ClienteDTO dto, RedirectAttributes redirectAttributes) {
        clienteService.insert(dto);
        redirectAttributes.addFlashAttribute("mensagem", "Cliente \"" + dto.nomeCompleto() + "\" cadastrado com sucesso!");
        return "redirect:/cliente/cadastro";
    }


}

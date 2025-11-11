package com.essjr.Cadastro.contato;


import com.essjr.Cadastro.appUser.AppUser;
import com.essjr.Cadastro.appUser.dtos.AppUserLogadoDTO;
import com.essjr.Cadastro.contato.dtos.ContatoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Tag(name = "Contatos", description = "Gerencia o cadastro e edição de contatos")
@Controller
@RequestMapping("/contato")
public class ContatoWebController {


    private final ContatoService contatoService;
    private final ContatoRepository contatoRepository;

    public ContatoWebController(ContatoService contatoService, ContatoRepository contatoRepository) {
        this.contatoService = contatoService;
        this.contatoRepository = contatoRepository;
    }

    /**
     * Exibe o formulário de CADASTRO de contatos.
     *
     * @param model O Model usado para enviar dados ao Thymeleaf.
     * @return O layout contendo o formulário de cadastro de contatos.
     */
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        model.addAttribute("conteudo", "cadastroContato");
        model.addAttribute("titulo", "Cadastro de Contatos");
        model.addAttribute("contato", new ContatoDTO(null, "", "", "", "", ""));
        return "layout";
    }

    /**
     * Processa o envio do formulário de CADASTRO de contatos.
     *
     * @param dto Os dados do contato enviados pelo formulário.
     * @param redirectAttributes Usado para enviar mensagens de sucesso ou erro após redirecionamento.
     * @param model O Model para recarregar a página em caso de erro.
     * @return Redirecionamento ou renderização do layout conforme o resultado.
     */
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


    /**
     * Exibe a lista de contatos cadastrados.
     *
     * @param model O Model usado para enviar dados ao Thymeleaf.
     * @param appUser O usuário autenticado exibido na página.
     * @return O layout contendo a lista de contatos.
     */
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

    /**
     * Exibe o formulário de edição de um contato existente.
     *
     * @param id O ID do contato a ser editado.
     * @param model O Model usado para enviar dados ao Thymeleaf.
     * @return O layout contendo o formulário de edição do contato.
     */
    @GetMapping("/editar/{id}")
    public String editarContato(@PathVariable Long id, Model model) {
        ContatoDTO contato = contatoService.findById(id);

        model.addAttribute("conteudo", "cadastroContato");
        model.addAttribute("contato", contato);

        return "layout";
    }


    /**
     * Processa a atualização de um contato existente.
     *
     * @param id O ID do contato a ser atualizado.
     * @param dto Os novos dados do contato.
     * @param redirectAttributes Usado para enviar mensagens de sucesso após redirecionamento.
     * @param model O Model usado para recarregar dados em caso de erro.
     * @return Redirecionamento ou renderização do layout conforme o resultado.
     */
    @PostMapping("/editar/{id}")
    public String atualizarContato(
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


    /**
     * Exclui um contato pelo ID informado.
     *
     * @param id O ID do contato a ser excluído.
     * @param redirectAttributes Usado para enviar mensagens de sucesso após a exclusão.
     * @return Redirecionamento para a lista de contatos.
     */
    @Operation(
            summary = "Excluir contato",
            description = "Exclui um contato do sistema com base no ID informado e redireciona para a lista."
    )
    @PostMapping("/excluir/{id}")
    public String excluirContato(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        contatoService.delete(id);
        redirectAttributes.addFlashAttribute("mensagem", "Contato excluído com sucesso!");
        return "redirect:/contato/lista";
    }


}

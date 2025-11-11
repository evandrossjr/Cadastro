package com.essjr.Cadastro.relatorio;

import com.essjr.Cadastro.cliente.Cliente;
import com.essjr.Cadastro.cliente.ClienteRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Controller
@RequestMapping("/relatorio")
public class RelatorioWebController {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private TemplateEngine templateEngine;

    @GetMapping
    public String relatorioClientes(Model model) {


        List<Cliente> clientesComContatos = clienteRepository.findAllWithContatos();

        model.addAttribute("clientes", clientesComContatos);
        model.addAttribute("dataAtual", java.time.LocalDate.now());
        model.addAttribute("horaAtual", java.time.LocalTime.now());

        model.addAttribute("titulo", "Relatório de Clentes");
        model.addAttribute("conteudo", "relatorioClientes");

        return "layout";
    }

    @GetMapping("/pdf")
    public void exportarRelatorioPdf(HttpServletResponse response) {
        try {
            List<Cliente> clientesComDuplicatas = clienteRepository.findAllWithContatos();

            //    Um LinkedHashSet remove duplicatas e mantém a ordem de inserção.
            List<Cliente> clientesUnicos = new ArrayList<>(new LinkedHashSet<>(clientesComDuplicatas));

            // Preparar o Contexto do Thymeleaf
            Context context = new Context();
            context.setVariable("clientes", clientesUnicos); // <-- Passa a lista limpa
            context.setVariable("dataAtual", java.time.LocalDate.now());
            context.setVariable("horaAtual", java.time.LocalTime.now());


            //    Processa o novo template de PDF, não o fragmento
            String htmlContent = templateEngine.process("relatorioClientesPDF", context);

            //  Configurar a Resposta do HTTP
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_clientes.pdf\"");

            //  Usar o Flying Saucer
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();

            //  Escrever o PDF
            OutputStream outputStream = response.getOutputStream();
            renderer.createPDF(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

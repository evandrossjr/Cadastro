package com.essjr.Cadastro.relatorio;

import com.essjr.Cadastro.Cliente.Cliente;
import com.essjr.Cadastro.Cliente.ClienteRepository;
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
            // 1. Buscar os dados (vem com duplicatas por causa do Join)
            List<Cliente> clientesComDuplicatas = clienteRepository.findAllWithContatos();

            // 2. --- CORREÇÃO DE DUPLICATAS ---
            //    Um LinkedHashSet remove duplicatas e mantém a ordem de inserção.
            //    (Isso requer que sua entidade Cliente tenha .equals() e .hashCode())
            List<Cliente> clientesUnicos = new ArrayList<>(new LinkedHashSet<>(clientesComDuplicatas));

            // 3. Preparar o Contexto do Thymeleaf
            Context context = new Context();
            context.setVariable("clientes", clientesUnicos); // <-- Passa a lista limpa
            context.setVariable("dataAtual", java.time.LocalDate.now());
            context.setVariable("horaAtual", java.time.LocalTime.now());

            // 4. --- MUDANÇA DE TEMPLATE ---
            //    Processa o novo template de PDF, não o fragmento
            String htmlContent = templateEngine.process("relatorioClientePDF", context);

            // 5. Configurar a Resposta do HTTP (continua igual)
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_clientes.pdf\"");

            // 6. Usar o Flying Saucer (continua igual)
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();

            // 7. Escrever o PDF (continua igual)
            OutputStream outputStream = response.getOutputStream();
            renderer.createPDF(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

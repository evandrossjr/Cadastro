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
            // 1. Buscar os dados (mesma lógica do relatório em tela)
            List<Cliente> clientes = clienteRepository.findAllWithContatos();

            // 2. Preparar o Contexto do Thymeleaf (enviar dados para o template)
            Context context = new Context();
            context.setVariable("clientes", clientes);
            // Adicione outras variáveis se o seu template PDF precisar
            // (Note: data/hora serão as do momento da geração do PDF)
            context.setVariable("dataAtual", java.time.LocalDate.now());
            context.setVariable("horaAtual", java.time.LocalTime.now());

            // 3. Processar o template HTML como uma String
            // Vamos reusar o mesmo template, mas você poderia criar um
            // "relatorioClientes-pdf.html" se quisesse um layout diferente
            String htmlContent = templateEngine.process("relatorioClientes", context);

            // 4. Configurar a Resposta do HTTP para PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=\"relatorio_clientes.pdf\"");

            // 5. Usar o Flying Saucer para gerar o PDF
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();

            // 6. Escrever o PDF na saída da resposta
            OutputStream outputStream = response.getOutputStream();
            renderer.createPDF(outputStream);
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            // Lidar com o erro (ex: enviar uma resposta de erro)
        }
    }
}

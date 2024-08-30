package com.example.projeto.controllers;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;

import org.springframework.web.servlet.ModelAndView;

import com.example.projeto.model.Produto;
import com.example.projeto.services.PdfReportService;
import com.example.projeto.services.ProdutoService;

import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProdutoController {

    @Autowired
    private PdfReportService pdfReportService;

    @Autowired
    private ProdutoService produtoService;

    @RequestMapping(value = "**/index", method=RequestMethod.GET)
    public String requestMethodName() {
        return "index";
    }
    

    @RequestMapping(value = "**/inicioestoque", method = RequestMethod.GET)
    public ModelAndView telaDeInicio() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("cadastro/cadastroproduto.html");

        modelAndView.addObject("objProduto", new Produto());
        return modelAndView;
    }

    @RequestMapping(value = "**/salvarproduto", method = RequestMethod.POST)
    public ModelAndView inserirProduto(@Valid Produto produto, BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto.html");
        // ERROS DE VALIDAÇÃO:
        if (bindingResult.hasErrors()) {
            modelAndView.addObject("objProduto", produto);

            List<String> listaMensagensErro = new ArrayList<>();

            for (ObjectError string : bindingResult.getAllErrors()) {
                listaMensagensErro.add(string.getDefaultMessage());
            }
            modelAndView.addObject("msgPraIterar", listaMensagensErro);
            return modelAndView;
        }

        String msgPraTela = produto.getId() == null ? "Produto inserido com sucesso"
                : "Produto atualizado com sucesso";
        modelAndView.addObject("msgPraIterar", msgPraTela);

        try {
            produtoService.save(produto);
        } catch (DataIntegrityViolationException e) {
            // EM CASO DE EXCEÇÃO POR VIOLAÇÃO DE INTEGRIDADE:
            // Mando uma mensagem mensagem de alerta e continuo com os atributos na tela
            modelAndView.addObject("msgPraIterar", "Produto com código " + produto.getCodBarras() +
                    " já cadastrado");
            modelAndView.addObject("objProduto", produto);
            return modelAndView;
        }

        modelAndView.addObject("objProduto", new Produto());

        return modelAndView;
    }

    @RequestMapping(value = "**/atualizarproduto/{id}", method = RequestMethod.GET)
    public ModelAndView atualizarProduto(@PathVariable Long id) {
        Optional<Produto> produto = produtoService.findById(id);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto.html");
        modelAndView.addObject("objProduto", produto.get());
        modelAndView.addObject("listaProdutosFront",
                produtoService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "**/listarprodutos", method = RequestMethod.GET)
    public ModelAndView listarTodos() {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto.html");

        modelAndView.addObject("listaProdutosFront",
                produtoService.findAll());

        modelAndView.addObject("objProduto", new Produto());

        return modelAndView;
    }

    @RequestMapping(value = "deletarproduto/{id}", method = RequestMethod.GET)
    public ModelAndView deletarProduto(@PathVariable Long id) {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto.html");
        produtoService.deleteById(id);
        String msgPraTela = "Produto deletado com sucesso!";

        modelAndView.addObject("msgPraIterar", msgPraTela);
        modelAndView.addObject("objProduto", new Produto());

        return modelAndView;
    }

    @RequestMapping(value = "/gerarrelatorio", method = RequestMethod.GET)
    public String testeRelatorio() {
        try {
            pdfReportService.generateReport("relatorio.pdf");
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado " + e.getMessage());
        }
        System.out.println("relatorio gerado");
        return "index";
    }

    @RequestMapping(value = "pesquisarproduto", method = RequestMethod.GET)
    public ModelAndView pequisarProduto(@RequestParam(name = "nome", required = false) String nome,
            @RequestParam(value = "codBarras", required = false) Long codBarras) {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastroproduto");

        if (!nome.isEmpty()) {
            List<Produto> listaProdutosPorNome = produtoService.findByName(nome);
            modelAndView.addObject("listaProdutosFront", listaProdutosPorNome);
        }

        modelAndView.addObject("objProduto", new Produto());

        return modelAndView;
    }

    @RequestMapping(value = "/descricaoproduto/{id}", method=RequestMethod.GET)
    public ModelAndView exibiDescricao(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView("cadastro/descricaoproduto");
        
        Produto produto = produtoService.findById(id).get();
        modelAndView.addObject("objProduto", produto);

        return modelAndView;
    }
    

}

package com.example.projeto.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.projeto.model.Pessoa;
import com.example.projeto.model.Telefone;
import com.example.projeto.services.PessoaService;
import com.example.projeto.services.TelefoneService;


@Controller
public class PessoaController {

    @Autowired
    private TelefoneService telefoneService;

    @Autowired
    PessoaService pessoaService;

    @RequestMapping(value = "**/iniciocliente", method = RequestMethod.GET)
    public ModelAndView telaDeInicio() {
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa.html");
        modelAndView.addObject("objPessoa", new Pessoa());

        return modelAndView;
    }

    @RequestMapping(value = "**/salvarpessoa", method = RequestMethod.POST, consumes = "multipart/form-data")
    public ModelAndView salvar(@Valid Pessoa pessoa, BindingResult bindingResult) {

        // PARA AS MENSAGENS DE ERRO DOS ATRIBUTOS:
        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa.html");

        if (bindingResult.hasErrors()) {
            // VOLTAR PRA TELA COM OS DADOS DA PESSOA:
            modelAndView.addObject("objPessoa", pessoa);

            // PRA LISTA DE PESSOAS CONTINUAR NA TELA:
            modelAndView.addObject("listaPessoasFront", pessoaService.findAll());

            List<String> listaMensagensErro = new ArrayList<>();
            for (ObjectError objectError : bindingResult.getAllErrors()) {
                listaMensagensErro.add(objectError.getDefaultMessage()); // Mensagem que vem do @NotNull
            }
            modelAndView.addObject("msgPraIterar", listaMensagensErro);

            return modelAndView;
        }

        // SALVAMENTO:
        System.out.println("ID recebido: " + pessoa.getId());
        String msgRetornadaPraTela = pessoa.getId() == null ? "Usuário salvo com sucesso!"
                : "Usuário atualizado com sucesso!";

        pessoaService.save(pessoa);

        modelAndView.addObject("msgPraIterar", msgRetornadaPraTela);

        modelAndView.addObject("objPessoa", new Pessoa());

        return modelAndView;

    }

    @RequestMapping(value = "**/listartodos", method = RequestMethod.GET)
    public ModelAndView listarTodos() {

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

        modelAndView.addObject("listaPessoasFront", pessoaService.findAll());
        modelAndView.addObject("objPessoa", new Pessoa());
        return modelAndView;
    }

    @RequestMapping(value = "**/atualizarpessoa/{id}", method = RequestMethod.GET)
    public ModelAndView editar(@PathVariable("id") Long id) {

        Optional<Pessoa> pessoa = pessoaService.findById(id);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");
        modelAndView.addObject("objPessoa", pessoa.get());
        modelAndView.addObject("listaPessoasFront",
                pessoaService.findAll());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletar/{id}", method = RequestMethod.GET)
    public ModelAndView deletar(@PathVariable("id") Long id) {
        // deleto
        Pessoa pessoa = pessoaService.findById(id).get();
        pessoaService.delete(pessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/cadastropessoa");

        // adiciono a lista completa sem a pessoa deletada
        modelAndView.addObject("listaPessoasFront",
                pessoaService.findAll());

        // pessoa vazia pro formulário de inicio
        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("msgPraIterar", "Usuário deletado com sucesso");

        return modelAndView;
    }

    @RequestMapping(value = "**/pesquisar", method = RequestMethod.GET)
    public ModelAndView pesquisar(@RequestParam(value = "nome", required = false) String nome,
            @RequestParam(value = "id", required = false) Long id) {

        ModelAndView modelAndView = new ModelAndView();

        if (nome != null && !nome.isEmpty()) {
            List<Pessoa> listPorNomes = pessoaService.findByName(nome);
            modelAndView.addObject("listaPessoasFront", listPorNomes);

        } else if (id != null) {
            try {
                Pessoa pessoaPorId = pessoaService.findById(id).get();
                modelAndView.addObject("listaPessoasFront", pessoaPorId);
            } catch (NoSuchElementException e) {
                modelAndView.addObject("msgPraIterar", "");
            }
        }

        modelAndView.addObject("objPessoa", new Pessoa());
        modelAndView.addObject("nomeTelaPesquisa", nome);

        modelAndView.setViewName("cadastro/cadastropessoa.html");
        return modelAndView;
    }

    // OPERAÇÕES DA ENTIDADE FRACA (TELEFONES):
    @RequestMapping(value = "**/telefones/{idPessoa}", method = RequestMethod.GET)
    public ModelAndView telefones(@PathVariable("idPessoa") Long idPessoa) {

        Optional<Pessoa> pessoa = pessoaService.findById(idPessoa);

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objPessoa", pessoa.get());

        // LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", pessoa.get().getListaTelefones());

        return modelAndView;
    }

    @RequestMapping(value = "**/cadastrartelefone/{idPessoa}", method = RequestMethod.POST)
    public ModelAndView cadastrartelefone(@PathVariable("idPessoa") Long idPessoa, @Valid Telefone telefone,
            BindingResult bindingResult) {

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        Optional<Pessoa> pessoa = pessoaService.findById(idPessoa);
        telefone.setPessoa(pessoa.get());
        telefoneService.save(telefone);

        pessoa.get().adicionarTelefoneNaLista(telefone);
        modelAndView.addObject("msgPraIterar", "Telefone adicionado com sucesso!");

        // PRA MOSTRAR A LISTA DE TELEFONES:
        modelAndView.addObject("listaTelefones", pessoa.get().getListaTelefones());

        // PRA CONTINUAR COM OS DADOS DA PESSOA NA TELA
        modelAndView.addObject("objPessoa", pessoa.get());
        return modelAndView;
    }

    @RequestMapping(value = "**/deletartelefone/{idTelefone}", method = RequestMethod.GET)
    public ModelAndView deletarTelefone(@PathVariable("idTelefone") Long idTelefone) {

        ModelAndView modelAndView = new ModelAndView("cadastro/telefones.html");

        Optional<Telefone> telefone = telefoneService.findById(idTelefone);
        telefoneService.delete(telefone.get());

        // DADOS DA PESSOA NA TELA:
        modelAndView.addObject("objPessoa", telefone.get().getPessoa());

        // MOSTRAR LISTA DE TELEFONES
        modelAndView.addObject("listaTelefones", telefone.get().getPessoa().getListaTelefones());
        modelAndView.addObject("msgPraIterar", "Telefone deletado com sucesso!");

        return modelAndView;
    }
}

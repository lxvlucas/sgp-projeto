package com.SgP.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.SgP.demo.models.Projeto;
import com.SgP.demo.models.Tarefa;
import com.SgP.demo.models.Usuario;
import com.SgP.demo.repositories.ProjetoRepository;
import com.SgP.demo.repositories.TarefaRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class TarefaController {
    
    @Autowired
    private ProjetoRepository projetoRepository;
    
    @Autowired
    private TarefaRepository tarefaRepository;
    
    @GetMapping("/projeto/{id}/tarefas")
    public String verTarefas(@PathVariable Long id, Model model) {
        Projeto projeto = projetoRepository.findById(id).orElse(null);
        if (projeto == null) {
            return "redirect:/";
        }
        model.addAttribute("projeto", projeto);
        model.addAttribute("tarefas", projeto.getTarefas());
        return "tarefas";
    }

    @GetMapping("/projeto/{projetoId}/tarefa/nova")
    public String novaTarefaForm(@PathVariable Long projetoId, Model model) {
        Projeto projeto = projetoRepository.findById(projetoId).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado: " + projetoId));
        
        model.addAttribute("projeto", projeto);
        model.addAttribute("tarefa", new Tarefa()); 
        
        return "nova-tarefa";
    }

    @PostMapping("/projeto/{projetoId}/tarefa/salvar")
    public String salvarTarefa(@PathVariable Long projetoId, @ModelAttribute Tarefa tarefa) {
        Projeto projeto = projetoRepository.findById(projetoId).orElseThrow(() -> new IllegalArgumentException("Projeto não encontrado: " + projetoId));
        
        tarefa.setProjeto(projeto);
        tarefa.setStatus("Pendente"); // Mantendo o status padrão da sua tela
        
        tarefaRepository.save(tarefa);
        
        return "redirect:/projeto/" + projetoId + "/tarefas";
    }

    @PostMapping("/tarefa/{id}/executar")
    public String registrarExecucao(@PathVariable Long id) {
        Tarefa tarefa = tarefaRepository.findById(id).orElse(null);
        if (tarefa != null) {
            tarefa.setStatus("Em Validação"); // Mantendo o status exato da sua tela HTML
            tarefaRepository.save(tarefa);
            return "redirect:/projeto/" + tarefa.getProjeto().getId() + "/tarefas";
        }
        return "redirect:/";
    }

    @PostMapping("/tarefa/{id}/validar")
    public String validarTarefa(@PathVariable Long id, HttpSession session) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        if (usuarioLogado != null && "GESTOR".equals(usuarioLogado.getFuncao())) {
            Tarefa tarefa = tarefaRepository.findById(id).orElse(null);
            if (tarefa != null) {
                tarefa.setStatus("CONCLUIDA");
                tarefaRepository.save(tarefa);
                return "redirect:/projeto/" + tarefa.getProjeto().getId() + "/tarefas";
            }
        }
        return "redirect:/";
    }
}	
package com.SgP.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.SgP.demo.models.Projeto;
import com.SgP.demo.models.Usuario;
import com.SgP.demo.repositories.ProjetoRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProjetoController {
	@Autowired
	private ProjetoRepository projetoRepository;
	
	@GetMapping("/")
	public String listarProjetos(Model model) {
		model.addAttribute("projetos", projetoRepository.findAll());
		return "index";
	}
	
	@GetMapping("/projeto/novo")
	public String formNovoProjeto(Model model) {
		model.addAttribute("projeto", new Projeto());
		return "novo-projeto";
	}
	
	@PostMapping("/projeto/salvar")
	public String salvarProjeto(Projeto projeto) {
		projetoRepository.save(projeto);
		return "redirect:/";
	}
	
	@GetMapping("/projeto/{id}/excluir")
	public String excluirProjeto(@PathVariable Long id, HttpSession session) {
	    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
	    
	    if (usuarioLogado != null && "GESTOR".equals(usuarioLogado.getFuncao())) {
	        projetoRepository.deleteById(id);
	    }
	    
	    return "redirect:/";
	}
	
}

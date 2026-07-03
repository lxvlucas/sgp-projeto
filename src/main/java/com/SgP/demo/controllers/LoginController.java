package com.SgP.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.SgP.demo.models.Usuario;
import com.SgP.demo.repositories.UsuarioRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/login")
	public String abrirTelaLogin() {
		return "login";
	}
	
	@PostMapping("/fazer-login")
	public String fazerLogin(String email, String senha, HttpSession session, Model model) {
		Usuario usuarioLogado = usuarioRepository.findByEmailAndSenha(email, senha);
		
		if (usuarioLogado != null) {
			session.setAttribute("usuarioLogado", usuarioLogado);
			return "redirect:/";
		} else {
			model.addAttribute("erro", "E-mail ou senha inválidos!");
			return "login";
		}
	}
	
	@GetMapping("/logout")
	public String fazerLogout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
}

package com.SgP.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.SgP.demo.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Usuario findByEmailAndSenha(String email, String senha);
}

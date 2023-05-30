package com.sistema.blog.repositorio;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.blog.entidades.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario,Long>{
	
	public Optional<Usuario> findByEmail(String email);
	
	public Optional<Usuario> findByNombreOrEmail(String username, String email);
	
	public Boolean existsByNombre(String username);
	
	public Boolean existsByEmail(String email);
}

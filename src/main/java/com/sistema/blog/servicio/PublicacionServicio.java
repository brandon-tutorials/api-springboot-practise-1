package com.sistema.blog.servicio;


import com.sistema.blog.dto.PublicacionDTO;
import com.sistema.blog.dto.PublicacionRespuesta;

public interface PublicacionServicio {
	
	public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO);
	
	public PublicacionRespuesta obtenerAllPublicaciones(int numeroPagina,int medidaPagina,String ordenarPor,String ordenDir);
	
	public PublicacionDTO obtenerPublicacionesPorId(Long id);
	
	public PublicacionDTO actualizarPublicacion(PublicacionDTO publicacionDTO);
	
	public void eliminarPublicacion(long id);
	

}

package com.sistema.blog.servicio;

import java.util.List;

import com.sistema.blog.dto.ComentarioDTO;

public interface ComentarioServicio {
	
	public ComentarioDTO crearComentario(long publicacionId,ComentarioDTO comentario);
	
	public List<ComentarioDTO> obtenerComentariosPorPublicacionId(long publicacionId);
	
	public ComentarioDTO obtenerComentarioPorId(long publicacionId, long comentarioId);
	
	public ComentarioDTO actualizarComentario(long publicacionId,ComentarioDTO solicitudComentario); 

	public void eliminarComentario(long publicacionId,long comentarioId);
	
}

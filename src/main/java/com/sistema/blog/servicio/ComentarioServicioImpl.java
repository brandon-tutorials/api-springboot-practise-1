package com.sistema.blog.servicio;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sistema.blog.dto.ComentarioDTO;
import com.sistema.blog.entidades.Comentario;
import com.sistema.blog.entidades.Publicacion;
import com.sistema.blog.excepciones.BlogAppException;
import com.sistema.blog.excepciones.ResourceNotFoundException;
import com.sistema.blog.repositorio.ComentarioRepositorio;
import com.sistema.blog.repositorio.PublicacionRepositorio;

@Service
public class ComentarioServicioImpl implements ComentarioServicio {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private ComentarioRepositorio comentarioRepositorio;
	
	@Autowired
	private PublicacionRepositorio publicacionRepositorio;
	

	@Override
	public ComentarioDTO crearComentario(long publicacionId, ComentarioDTO comentarioDTO) {
		Comentario comentario = this.mapearEntidad(comentarioDTO);
		Publicacion publicacion = publicacionRepositorio.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",publicacionId));
		comentario.setPublicacion(publicacion);
		return mapearDTO(comentarioRepositorio.save(comentario));
	}
	
	private ComentarioDTO mapearDTO(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioDTO.class);
	} 
	
	private Comentario mapearEntidad(ComentarioDTO comentarioDTO) {
		return modelMapper.map(comentarioDTO, Comentario.class);
	}

	@Override
	public List<ComentarioDTO> obtenerComentariosPorPublicacionId(long publicacionId) 
	{
		List<Comentario> comentarios=comentarioRepositorio.findByPublicacionId(publicacionId);
		return comentarios.stream().map(comentario-> mapearDTO(comentario)).collect(Collectors.toList());
	}

	@Override
	public ComentarioDTO obtenerComentarioPorId(long publicacionId,long comentarioId) {
		Publicacion publicacion = publicacionRepositorio.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",publicacionId));
		Comentario comentario = comentarioRepositorio.findById(comentarioId).orElseThrow(()->new ResourceNotFoundException("Comentario","id",comentarioId));
		if(!comentario.getPublicacion().getId().equals(publicacion.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST,"El comentario no pertenece a la publicacion");
		} 
		
		return mapearDTO(comentario);
	}

	@Override
	public ComentarioDTO actualizarComentario(long publicacionId, ComentarioDTO solicitudComentario) {
		Publicacion publicacion = publicacionRepositorio.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",publicacionId));
		Comentario comentario = comentarioRepositorio.findById(solicitudComentario.getId()).orElseThrow(()->new ResourceNotFoundException("Comentario","id",solicitudComentario.getId()));
		if(!comentario.getPublicacion().getId().equals(publicacion.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST,"El comentario no pertenece a la publicacion");
		} 
		comentario.setNombre(solicitudComentario.getNombre());
		comentario.setEmail(solicitudComentario.getEmail());
		comentario.setCuerpo(solicitudComentario.getCuerpo());
		
		return mapearDTO(comentarioRepositorio.save(comentario));
	}

	@Override
	public void eliminarComentario(long publicacionId, long comentarioId) {
		Publicacion publicacion = publicacionRepositorio.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",publicacionId));
		Comentario comentario = comentarioRepositorio.findById(comentarioId).orElseThrow(()->new ResourceNotFoundException("Comentario","id",comentarioId));
		if(!comentario.getPublicacion().getId().equals(publicacion.getId())) {
			throw new BlogAppException(HttpStatus.BAD_REQUEST,"El comentario no pertenece a la publicacion");
		} 
		comentarioRepositorio.delete(comentario);
	}
	
	
	

}

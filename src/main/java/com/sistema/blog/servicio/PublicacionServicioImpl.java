package com.sistema.blog.servicio;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.sistema.blog.dto.PublicacionDTO;
import com.sistema.blog.dto.PublicacionRespuesta;
import com.sistema.blog.entidades.Publicacion;
import com.sistema.blog.excepciones.ResourceNotFoundException;
import com.sistema.blog.repositorio.PublicacionRepositorio;

@Service
public class PublicacionServicioImpl implements PublicacionServicio {

	@Autowired 
	private ModelMapper modelMapper;
	
	@Autowired 
	private PublicacionRepositorio publicacionRepositorio;
	
	@Override
	public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO) {
		Publicacion publicacion = mapearEntidad(publicacionDTO);
		Publicacion nuevaPublicacion = publicacionRepositorio.save(publicacion);
		return mapearDTO(nuevaPublicacion);
	}

	@Override
	public PublicacionRespuesta obtenerAllPublicaciones(int numeroPagina,int medidaPagina,String ordenarPor,String ordenDir) {
		Sort sort = ordenDir.equalsIgnoreCase(Sort.Direction.ASC.name())? Sort.by(ordenarPor).ascending(): Sort.by(ordenarPor).descending();
		Pageable pageable =  PageRequest.of(numeroPagina, medidaPagina,sort);
		Page<Publicacion> publicaciones = publicacionRepositorio.findAll(pageable);
		List<PublicacionDTO> contenido = publicaciones.getContent().stream().map(publicacion->mapearDTO(publicacion)).collect(Collectors.toList());
		PublicacionRespuesta publicacionRespuesta = new PublicacionRespuesta();
		publicacionRespuesta.setContenido(contenido);
		publicacionRespuesta.setNumeroPagina(numeroPagina);
		publicacionRespuesta.setMedidaPagina(medidaPagina);
		publicacionRespuesta.setTotalElementos(publicaciones.getTotalElements());
		publicacionRespuesta.setTotalPaginas(publicaciones.getTotalPages());
		publicacionRespuesta.setUltima(publicaciones.isLast());	
		return publicacionRespuesta;
	}
	
	
	private PublicacionDTO mapearDTO(Publicacion publicacion) {
		return modelMapper.map(publicacion, PublicacionDTO.class);
	}
	
	private Publicacion mapearEntidad(PublicacionDTO publicacionDTO) {
		return modelMapper.map(publicacionDTO, Publicacion.class);
	}

	@Override
	public PublicacionDTO obtenerPublicacionesPorId(Long id) {
		Publicacion publicacion = publicacionRepositorio.findById(id).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",id));
		return mapearDTO(publicacion);
	}

	@Override
	public PublicacionDTO actualizarPublicacion(PublicacionDTO publicacionDTO) {
		Publicacion publicacion = publicacionRepositorio.findById(publicacionDTO.getId()).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",publicacionDTO.getId()));
		publicacion = mapearEntidad(publicacionDTO);
		Publicacion publicacionActualizada = publicacionRepositorio.save(publicacion);
		return mapearDTO(publicacionActualizada);		
	}

	@Override
	public void eliminarPublicacion(long id) {
		Publicacion publicacion = publicacionRepositorio.findById(id).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",id));
		publicacionRepositorio.delete(publicacion);
	}
	

}

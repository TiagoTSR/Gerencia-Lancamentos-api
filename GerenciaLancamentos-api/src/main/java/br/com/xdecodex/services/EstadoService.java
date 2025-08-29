package br.com.xdecodex.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.stereotype.Service;

import br.com.xdecodex.controllers.EstadoController;
import br.com.xdecodex.data.vo.v1.EstadoVO;
import br.com.xdecodex.exceptions.RequiredObjectIsNullException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Estado;
import br.com.xdecodex.repositories.EstadoRepository;

@Service
public class EstadoService {
		
	private Logger logger = Logger.getLogger(EstadoService.class.getName());

    @Autowired
    private EstadoRepository estadoRepository;
    
    @Autowired
	PagedResourcesAssembler<EstadoVO> assembler;
   

    public List<EstadoVO> findAll() {

		logger.info("Encontrando todas as Estados!");

		List<EstadoVO> estados = DozerMapper.parseListObjects(estadoRepository.findAll(), EstadoVO.class);
		estados
			.stream()
			.forEach(p -> p.add(linkTo(methodOn(EstadoController.class).findById(p.getId())).withSelfRel()));
		return estados;
	}
    
      
    public Page<EstadoVO> findAll(Pageable pageable) {
        Page<Estado> estados = estadoRepository.findAll(pageable);
        return estados.map(e -> {
            EstadoVO vo = DozerMapper.parseObject(e, EstadoVO.class);
            // adicionar links ou outras conversões se necessário
            return vo;
        });
    }


    public EstadoVO findById(Long id) {
		
		logger.info("Encontrando uma estado!");
		
		Estado estado = estadoRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Sem registro para esse ID!"));
		EstadoVO vo = DozerMapper.parseObject(estado, EstadoVO.class);
		vo.add(linkTo(methodOn(EstadoController.class).findById(id)).withSelfRel());
		return vo;
}

    public EstadoVO create(EstadoVO estado) {
    	if (estado == null) {
            throw new RequiredObjectIsNullException("It is not allowed to persist a null object!");
        }
        logger.info("Criando uma estado!");
        Estado entity = DozerMapper.parseObject(estado, Estado.class);
        EstadoVO vo = DozerMapper.parseObject(estadoRepository.save(entity), EstadoVO.class);
        vo.add(linkTo(methodOn(EstadoController.class).findById(vo.getId())).withSelfRel());
        return vo;
    }
    
    public EstadoVO update(EstadoVO estadoVO) {
        logger.info("Updating Estado");
        Estado estado = estadoRepository.findById(estadoVO.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Estado not found for update"));

        estado.setNome(estadoVO.getNome());
        // Atualize outros campos conforme necessário

        Estado updatedEstado = estadoRepository.save(estado);
        return DozerMapper.parseObject(updatedEstado, EstadoVO.class);
    }
    
    public boolean delete(Long id) {
        logger.info("Deleting Estado");
        if (!estadoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estado not found for delete");
        }
        estadoRepository.deleteById(id);
        return true;
    }
}

package br.com.xdecodex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.xdecodex.data.vo.v1.EstadoVO;
import br.com.xdecodex.services.EstadoService;
import br.com.xdecodex.util.MediaType1;

@RestController
@RequestMapping("/estados")
public class EstadoController {
	
	@Autowired
	private EstadoService estadoService;

	@Autowired
	@Qualifier("pagedResourcesAssembler")
	private RepresentationModelAssembler<EstadoVO, EntityModel<EstadoVO>> estadoModelAssembler;
	
	 @GetMapping(produces = { MediaType1.APPLICATION_JSON,
			MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML 
			})
	 
	 public ResponseEntity<PagedModel<EntityModel<EstadoVO>>> findAll(
		        @RequestParam(value = "page", defaultValue = "0") Integer page,
		        @RequestParam(value = "size", defaultValue = "10") Integer size,
		        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

		    Direction sortDirection = "desc".equalsIgnoreCase(direction)
		            ? Direction.DESC
		            : Direction.ASC;

		    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
			return ResponseEntity.ok(estadoService.findAll(pageable));
		}

	 @CrossOrigin(origins = "http://localhost:8080")
	    @GetMapping(value = "/{id}", produces = { MediaType1.APPLICATION_JSON,
				MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML 
				})
	    
	 public ResponseEntity<EstadoVO> findById(@PathVariable("id") Long id) {
	        try {
	            EstadoVO estado = estadoService.findById(id);
	            return ResponseEntity.ok(estado);
	        } catch (Exception e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 
	 @PostMapping(consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  },
		produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  })
	   
	    public ResponseEntity<EstadoVO> create(@RequestBody EstadoVO estado) {
	        try {
	            EstadoVO novoEstado = estadoService.create(estado);
	            return ResponseEntity.status(HttpStatus.CREATED).body(novoEstado);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    }
	 
	 @PutMapping(value = "/{id}", consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  },
		produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  })
	    
	    public ResponseEntity<EstadoVO> update(@PathVariable("id") Long id, @RequestBody EstadoVO estado) {
	        try {
	            estado.setId(id);  
	            EstadoVO estadoAtualizado = estadoService.update(estado);
	            return ResponseEntity.ok(estadoAtualizado);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    }
	 
	 @DeleteMapping(value = "/{id}")
	    
	    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
	        boolean deleted = estadoService.delete(id);
	        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	    }

}

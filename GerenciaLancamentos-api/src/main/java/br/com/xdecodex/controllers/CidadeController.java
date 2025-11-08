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

import br.com.xdecodex.data.vo.v1.CidadeVO;
import br.com.xdecodex.services.CidadeService;
import br.com.xdecodex.util.MediaType1;


@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	 private CidadeService cidadeService;

	 @Autowired
	 @Qualifier("pagedResourcesAssembler")
	 private RepresentationModelAssembler<CidadeVO, EntityModel<CidadeVO>> cidadeModelAssembler;
	
	 @GetMapping(produces = { MediaType1.APPLICATION_JSON,
			MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML 
			})
	 
	 public ResponseEntity<PagedModel<EntityModel<CidadeVO>>> findAll(
	         @RequestParam(value = "page", defaultValue = "0") Integer page,
	         @RequestParam(value = "size", defaultValue = "10") Integer size,
	         @RequestParam(value = "direction", defaultValue = "asc") String direction) {

	     Direction sortDirection = "desc".equalsIgnoreCase(direction)
	             ? Direction.DESC
	             : Direction.ASC;

	     Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
		 return ResponseEntity.ok(cidadeService.findAll(pageable));
	 }

	 
	 @CrossOrigin(origins = "http://localhost:8080")
	    @GetMapping(value = "/{id}", produces = { MediaType1.APPLICATION_JSON,
				MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML 
				})
	 
	 public ResponseEntity<CidadeVO> findById(@PathVariable("id") Long id) {
	        try {
	            CidadeVO cidade = cidadeService.findById(id);
	            return ResponseEntity.ok(cidade);
	        } catch (Exception e) {
	            return ResponseEntity.notFound().build();
	        }
	    }
	 
	 @PostMapping(consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  },
		produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  })
	 
	    public ResponseEntity<CidadeVO> create(@RequestBody CidadeVO cidade) {
	        try {
	            CidadeVO novaCidade = cidadeService.create(cidade);
	            return ResponseEntity.status(HttpStatus.CREATED).body(novaCidade);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    }
	 
	 @PutMapping(value = "/{id}", consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  },
		produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML  })
	    
	    public ResponseEntity<CidadeVO> update(@PathVariable("id") Long id, @RequestBody CidadeVO cidade) {
	        try {
	            cidade.setId(id);  
	            CidadeVO cidadeAtualizado = cidadeService.update(cidade);
	            return ResponseEntity.ok(cidadeAtualizado);
	        } catch (Exception e) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	        }
	    }
	 
	 @DeleteMapping(value = "/{id}")
	    
	    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
	        boolean deleted = cidadeService.delete(id);
	        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	    }

}


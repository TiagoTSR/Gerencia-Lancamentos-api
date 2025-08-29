package br.com.xdecodex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
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
import org.springframework.beans.factory.annotation.Qualifier;


import br.com.xdecodex.data.vo.v1.EstadoVO;
import br.com.xdecodex.services.EstadoService;
import br.com.xdecodex.util.MediaType1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/estados/v1")
@Tag(name = "Estado", description = "Endpoints for person management")
public class EstadoController {
	
	@Autowired
	private EstadoService estadoService;

	@Autowired
	private PagedResourcesAssembler<EstadoVO> pagedResourcesAssembler;

	@Autowired
	@Qualifier("pagedResourcesAssembler")
	private RepresentationModelAssembler<EstadoVO, EntityModel<EstadoVO>> estadoModelAssembler;
	
	 @GetMapping(produces = { MediaType1.APPLICATION_JSON,
			MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML 
			})
     @Operation(summary = "Find all estados", description = "Find all estados",
    		tags = {"Estado"},
    		responses = {
    			@ApiResponse(description = "Success", responseCode = "200",
    				content = {
    				@Content(
    					mediaType = "application/json",
    					array = @ArraySchema(schema = @Schema(implementation = EstadoVO.class))
    				)
    			}),
    		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
    		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
    		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
    		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    	})
	 
	 public ResponseEntity<PagedModel<EntityModel<EstadoVO>>> findAll(
		        @RequestParam(value = "page", defaultValue = "0") Integer page,
		        @RequestParam(value = "size", defaultValue = "10") Integer size,
		        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

		    Direction sortDirection = "desc".equalsIgnoreCase(direction)
		            ? Direction.DESC
		            : Direction.ASC;

		    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));

		    Page<EstadoVO> estadosPage = estadoService.findAll(pageable);

		    PagedModel<EntityModel<EstadoVO>> pagedModel = pagedResourcesAssembler.toModel(estadosPage, estadoModelAssembler);

		    return ResponseEntity.ok(pagedModel);
		}

	 @CrossOrigin(origins = "http://localhost:8080")
	    @GetMapping(value = "/{id}", produces = { MediaType1.APPLICATION_JSON,
				MediaType1.APPLICATION_XML,
				MediaType1.APPLICATION_YML 
				})
	    @Operation(summary = "Find a estado", description = "Find a estado",
		tags = {"Estado"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = EstadoVO.class))
			),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
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
	    @Operation(summary = "Add a new estado",
		description = "Add a new Estado by passing a JSON, XML or YML representation of the estado!",
		tags = {"Estado"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = @Content(schema = @Schema(implementation = EstadoVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		    }
	    )
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
	    @Operation(summary = "Update a estado",
		description = "Update a Estado by passing a JSON, XML or YML representation of the estado!",
		tags = {"Estado"},
		responses = {
			@ApiResponse(description = "Updated", responseCode = "200",
				content = @Content(schema = @Schema(implementation = EstadoVO.class))
			),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		   }
	    )
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
	    @Operation(summary = "Deletes a estado",
		description = "Deletes a Estado by passing a JSON, XML or YML representation of the estado!s",
		tags = {"Estado"},
		responses = {
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
		    }
	    )
	    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
	        boolean deleted = estadoService.delete(id);
	        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	    }

}

package br.com.xdecodex.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.xdecodex.data.vo.v1.CategoriaVO;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.services.CategoriaService;
import br.com.xdecodex.util.MediaType1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/categorias/v1")
@Tag(name = "Categoria", description = "Endpoints para gerenciamento de categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping(produces = { MediaType1.APPLICATION_JSON,
			MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML 
			})
    @Operation(summary = "Find all categories", description = "Find all categories",
		tags = {"Categoria"},
		responses = {
			@ApiResponse(description = "Success", responseCode = "200",
				content = {
				@Content(
					mediaType = "application/json",
					array = @ArraySchema(schema = @Schema(implementation = CategoriaVO.class))
				)
			}),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	  }
	)
    public ResponseEntity<PagedModel<EntityModel<CategoriaVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "5") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) {
		
		 Direction sortDirection = "desc".equalsIgnoreCase(direction)
				? Direction.DESC : Direction.ASC;
			
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "codigo"));
		return ResponseEntity.ok(categoriaService.findAll(pageable));
	}

    @GetMapping(value = "/{id}", produces = { MediaType1.APPLICATION_JSON,
			MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML 
			})
    @Operation(summary = "Find a category", description = "Find a category",
	tags = {"Categoria"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = CategoriaVO.class))
		),
		@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<CategoriaVO> findById(@PathVariable("id") Long id) {
        try {
            CategoriaVO categoria = categoriaService.findById(id);
            return ResponseEntity.ok(categoria);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML  },
	produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML  })
    @Operation(summary = "Add a categoria",
	description = "Add a category by passing a JSON, XML or YML representation of the category!",
	tags = {"Categoria"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = CategoriaVO.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<CategoriaVO> create(@RequestBody CategoriaVO categoria) {
        CategoriaVO novaCategoriaVO = categoriaService.create(categoria);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaCategoriaVO);
    }

    @PutMapping(consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML  },
	produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML  })
    @Operation(summary = "Update a categoria",
	description = "Update a category by passing a JSON, XML or YML representation of the category!",
	tags = {"Categoria"},
	responses = {
		@ApiResponse(description = "Updated", responseCode = "200",
			content = @Content(schema = @Schema(implementation = CategoriaVO.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	   }
    )
    public ResponseEntity<CategoriaVO> update(@RequestBody CategoriaVO categoria) {
        try {
            CategoriaVO categoriaAtualizada = categoriaService.update(categoria);
            return ResponseEntity.ok(categoriaAtualizada);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a category",
	description = "Deletes a category by passing a JSON, XML or YML representation of the category!",
	tags = {"Category"},
	responses = {
		@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        try {
            boolean deleted = categoriaService.delete(id);
            return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
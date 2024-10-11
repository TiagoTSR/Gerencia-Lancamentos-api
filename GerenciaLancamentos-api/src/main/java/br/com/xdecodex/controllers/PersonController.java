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

import br.com.xdecodex.data.vo.v1.PersonVO;
import br.com.xdecodex.data.vo.v2.PersonVOV2;
import br.com.xdecodex.services.PersonService;
import br.com.xdecodex.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/persons/v1")
@Tag(name = "Person", description = "Endpoints for person management")
public class PersonController {
    
    @Autowired
    private PersonService personService; 

    @GetMapping(produces = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML 
			})
    @Operation(summary = "Find all persons", description = "Find all persons",
    		tags = {"Person"},
    		responses = {
    			@ApiResponse(description = "Success", responseCode = "200",
    				content = {
    				@Content(
    					mediaType = "application/json",
    					array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
    				)
    			}),
    		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
    		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
    		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
    		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    	}
    )
    
    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction
			) {
		
		 Direction sortDirection = "desc".equalsIgnoreCase(direction)
				? Direction.DESC : Direction.ASC;
			
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
		return ResponseEntity.ok(personService.findAll(pageable));
	}
    
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML 
			})
    @Operation(summary = "Find a person", description = "Find a person",
	tags = {"Person"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PersonVO.class))
		),
		@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<PersonVO> findById(@PathVariable("id") Long id) {
        try {
            PersonVO person = personService.findById(id);
            return ResponseEntity.ok(person);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    @Operation(summary = "Add a new person",
	description = "Add a new Person by passing a JSON, XML or YML representation of the person!",
	tags = {"Person"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PersonVO.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
        try {
            PersonVO newPerson = personService.create(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping( value = "/v2",consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    @Operation(summary = "Adiciona uma new person",
	description = "Adiciona uma new Person passando uma representação JSON, XML ou YML da person!",
	tags = {"Person"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PersonVOV2.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<PersonVOV2> createV2(@RequestBody PersonVOV2 person) {
        try {
            PersonVOV2 newPerson = personService.createV2(person);
            return ResponseEntity.status(HttpStatus.CREATED).body(newPerson);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    @Operation(summary = "Update a person",
	description = "Update a Person by passing a JSON, XML or YML representation of the person!",
	tags = {"Person"},
	responses = {
		@ApiResponse(description = "Updated", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PersonVO.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	   }
    )
    public ResponseEntity<PersonVO> update(@PathVariable("id") Long id, @RequestBody PersonVO person) {
        try {
            person.setId(id);  
            PersonVO personAtualizada = personService.update(person);
            return ResponseEntity.ok(personAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Deletes a person",
	description = "Deletes a Person by passing a JSON, XML or YML representation of the person!s",
	tags = {"Person"},
	responses = {
		@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = personService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

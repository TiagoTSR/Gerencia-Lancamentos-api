package br.com.xdecodex.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.data.vo.v2.PessoaVOV2;
import br.com.xdecodex.services.PessoaService;
import br.com.xdecodex.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/pessoas/v1")
@Tag(name = "Pessoa", description = "Endpoints para gerenciamento de pessoas")
public class PessoaController {

    @Autowired
    private PessoaService service;

    @GetMapping(produces = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML 
			})
    @Operation(summary = "Encontra todas as pessoas", description = "Encontra todas as pessoas",
    		tags = {"Pessoa"},
    		responses = {
    			@ApiResponse(description = "Success", responseCode = "200",
    				content = {
    				@Content(
    					mediaType = "application/json",
    					array = @ArraySchema(schema = @Schema(implementation = PessoaVO.class))
    				)
    			}),
    		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
    		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
    		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
    		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    	}
    )
    
    public ResponseEntity<List<PessoaVO>> findAll() {
        List<PessoaVO> pessoas = service.findAll();
        return ResponseEntity.ok(pessoas);
    }
    
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML 
			})
    @Operation(summary = "Encontra uma pessoa", description = "Encontra uma pessoa",
	tags = {"Pessoa"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PessoaVO.class))
		),
		@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<PessoaVO> findById(@PathVariable("id") Long id) {
        try {
            PessoaVO pessoa = service.findById(id);
            return ResponseEntity.ok(pessoa);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    @Operation(summary = "Adiciona uma nova pessoa",
	description = "Adiciona uma nova Pessoa passando uma representação JSON, XML ou YML da pessoa!",
	tags = {"Pessoa"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PessoaVO.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<PessoaVO> create(@RequestBody PessoaVO pessoa) {
        try {
            PessoaVO novaPessoa = service.create(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PostMapping( value = "/v2",consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    @Operation(summary = "Adiciona uma nova pessoa",
	description = "Adiciona uma nova Pessoa passando uma representação JSON, XML ou YML da pessoa!",
	tags = {"Pessoa"},
	responses = {
		@ApiResponse(description = "Success", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PessoaVOV2.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<PessoaVOV2> createV2(@RequestBody PessoaVOV2 pessoa) {
        try {
            PessoaVOV2 novaPessoa = service.createV2(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    @Operation(summary = "Atualiza uma pessoa",
	description = "Atualiza uma Pessoa passando uma representação JSON, XML ou YML da pessoa!",
	tags = {"Pessoa"},
	responses = {
		@ApiResponse(description = "Updated", responseCode = "200",
			content = @Content(schema = @Schema(implementation = PessoaVO.class))
		),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	   }
    )
    public ResponseEntity<PessoaVO> update(@PathVariable("id") Long id, @RequestBody PessoaVO pessoa) {
        try {
            pessoa.setCodigo(id);  
            PessoaVO pessoaAtualizada = service.update(pessoa);
            return ResponseEntity.ok(pessoaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Exclui uma pessoa",
	description = "Exclui uma Pessoa passando uma representação JSON, XML ou YML da pessoa!",
	tags = {"Pessoa"},
	responses = {
		@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
		@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
		@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
		@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	    }
    )
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

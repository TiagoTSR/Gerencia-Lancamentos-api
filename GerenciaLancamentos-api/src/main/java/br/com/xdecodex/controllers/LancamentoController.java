package br.com.xdecodex.controllers;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import org.springframework.web.multipart.MultipartFile;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.dto.Anexo;
import br.com.xdecodex.dto.LancamentoEstatisticaCategoria;
import br.com.xdecodex.dto.LancamentoEstatisticaDia;
import br.com.xdecodex.repositories.filter.LancamentoFilter;
import br.com.xdecodex.repositories.launch.LancamentoRepositoryQuery;
import br.com.xdecodex.services.LancamentoService;
import br.com.xdecodex.storage.S3;
import br.com.xdecodex.util.MediaType1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lancamentos/v1")
@Tag(name = "Lancamento", description = "Endpoints for lancamentos management")
public class LancamentoController {
	
	@Autowired
	private LancamentoRepositoryQuery lancamentoRepositoryQuery;
	
	@Lazy
	@Autowired
	private LancamentoService lancamentoService;

	PagedResourcesAssembler<LancamentoVO> assembler;
	
	@Autowired
	private S3 s3;
	
	@PostMapping(value = "/anexo", consumes = "multipart/form-data")
	public Anexo uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {
		String nome = s3.salvarTemporariamente(anexo);
		return new Anexo(nome, s3.configurarUrl(nome));
	}
	/*
	public String uploadAnexo(@RequestParam MultipartFile anexo) throws IOException {
		OutputStream out = new FileOutputStream("C:\\Tiago\\anexos--" + anexo.getOriginalFilename());
		out.write(anexo.getBytes());
		out.close();
		return "ok";
	}
	*/
	@GetMapping("/relatorios/por-pessoa")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	public ResponseEntity<byte[]> relatorioPorPessoa(
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate inicio,
			@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fim) throws Exception {
		byte[] relatorio = lancamentoService.relatorioPorPessoa(inicio, fim);

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(relatorio);
	}
	
	@GetMapping("/estatisticas/por-dia")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	public List<LancamentoEstatisticaDia> porDia() {
		return this.lancamentoRepositoryQuery.porDia(LocalDate.now());
	}
	
	@GetMapping("/estatisticas/por-categoria")
	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	public List<LancamentoEstatisticaCategoria> porCategoria() {
		return this.lancamentoRepositoryQuery.porCategoria(LocalDate.now());
	}	

	@GetMapping(produces = { MediaType1.APPLICATION_JSON,
			MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML
	})
	@Operation(summary = "Find all launches", description = "Find all launches", tags = { "Lancamento" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LancamentoVO.class)))
			}),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	})

	public ResponseEntity<PagedModel<EntityModel<LancamentoVO>>> findAll(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@RequestParam(value = "size", defaultValue = "10") Integer size,
			@RequestParam(value = "direction", defaultValue = "asc") String direction) {

		Direction sortDirection = "desc".equalsIgnoreCase(direction)
				? Direction.DESC
				: Direction.ASC;

		Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
		return ResponseEntity.ok(lancamentoService.findAll(pageable));
	}

	public ResponseEntity<List<LancamentoVO>> findAll(LancamentoFilter lancamentoFilter, Pageable pageable) {
        List<LancamentoVO> launchs = lancamentoService.filtrar(lancamentoFilter,pageable);
        return ResponseEntity.ok(launchs);
    }
	
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping(value = "/{id}", produces = { MediaType1.APPLICATION_JSON,
			MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML
	})
	@Operation(summary = "Find a lancamento", description = "Find a lancamento", tags = { "Lancamento" }, responses = {
			@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = LancamentoVO.class))),
			@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
			@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
			@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
			@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
			@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
	})
	public ResponseEntity<LancamentoVO> findById(@PathVariable("id") Long id) {
		try {
			LancamentoVO lancamento = lancamentoService.findById(id);
			return ResponseEntity.ok(lancamento);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML }, produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
					MediaType1.APPLICATION_YML })
	@Operation(summary = "Add a release", description = "Adds a new release by passing a JSON, XML or YML representation of the release!", tags = {
			"Lancamento" }, responses = {
					@ApiResponse(description = "Success", responseCode = "200", content = @Content(schema = @Schema(implementation = LancamentoVO.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<LancamentoVO> create(@Valid @RequestBody LancamentoVO lancamento) {
		try {
			LancamentoVO novoLancamento = lancamentoService.create(lancamento);
			return ResponseEntity.status(HttpStatus.CREATED).body(novoLancamento);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@PutMapping(value = "/{id}", consumes = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
			MediaType1.APPLICATION_YML }, produces = { MediaType1.APPLICATION_JSON, MediaType1.APPLICATION_XML,
					MediaType1.APPLICATION_YML })
	@Operation(summary = "Update a release", description = "Updates a release by passing a JSON, XML or YML representation of the release!", tags = {
			"Lancamento" }, responses = {
					@ApiResponse(description = "Updated", responseCode = "200", content = @Content(schema = @Schema(implementation = LancamentoVO.class))),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<LancamentoVO> update(@PathVariable("id") Long id, @Valid @RequestBody LancamentoVO lancamento) {
		try {
			lancamento.setId(id);
			LancamentoVO lancamentoAtualizado = lancamentoService.update(lancamento);
			return ResponseEntity.ok(lancamentoAtualizado);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}

	@DeleteMapping(value = "/{id}")
	@Operation(summary = "Deletes a release", description = "Deletes a release by passing a JSON, XML or YML representation of the release!", tags = {
			"Lancamento" }, responses = {
					@ApiResponse(description = "No Content", responseCode = "204", content = @Content),
					@ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
					@ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
					@ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
					@ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
			})
	public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
		boolean deleted = lancamentoService.delete(id);
		return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
	}
}

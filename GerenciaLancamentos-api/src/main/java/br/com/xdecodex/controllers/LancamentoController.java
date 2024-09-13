package br.com.xdecodex.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.services.LancamentoService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoController {

    @Autowired
    private LancamentoService lancamentoService;

    @GetMapping(produces = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML 
			})
    public ResponseEntity<List<LancamentoVO>> findAll() {
        List<LancamentoVO> lancamentos = lancamentoService.findAll();
        return ResponseEntity.ok(lancamentos);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON,
			MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML 
			})
    public ResponseEntity<LancamentoVO> findById(@PathVariable("id") Long id) {
        try {
            LancamentoVO lancamento = lancamentoService.findById(id);
            return ResponseEntity.ok(lancamento);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    public ResponseEntity<LancamentoVO> create(@Valid @RequestBody LancamentoVO lancamento) {
        try {
            LancamentoVO novoLancamento = lancamentoService.save(lancamento);
            return ResponseEntity.status(HttpStatus.CREATED).body(novoLancamento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  },
	produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML,
			MediaType.APPLICATION_YML  })
    public ResponseEntity<LancamentoVO> update(@PathVariable("id") Long id, @Valid @RequestBody LancamentoVO lancamento) {
        try {
            lancamento.setCodigo(id);
            LancamentoVO lancamentoAtualizado = lancamentoService.update(lancamento);
            return ResponseEntity.ok(lancamentoAtualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = lancamentoService.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

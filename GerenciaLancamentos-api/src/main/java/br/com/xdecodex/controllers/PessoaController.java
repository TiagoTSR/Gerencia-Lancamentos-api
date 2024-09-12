package br.com.xdecodex.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.services.PessoaService;

@RestController
@RequestMapping("/pessoas")  
public class PessoaController {

    @Autowired
    private PessoaService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<PessoaVO>> findAll() {
        List<PessoaVO> pessoas = service.findAll();
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PessoaVO> findById(@PathVariable("id") Long id) {
        try {
            PessoaVO pessoa = service.findById(id);
            return ResponseEntity.ok(pessoa);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PessoaVO> create(@RequestBody PessoaVO pessoa) {
        try {
            PessoaVO novaPessoa = service.create(pessoa);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaPessoa);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        boolean deleted = service.delete(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
package br.com.xdecodex.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xdecodex.controllers.LancamentoController;
import br.com.xdecodex.data.vo.v1.LancamentoVO;
import br.com.xdecodex.exceptions.PessoaInexistenteOuInativaException;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.LancamentoRepository;
import br.com.xdecodex.repositories.PessoaRepository;

@Service
public class LancamentoService {

    private Logger logger = Logger.getLogger(LancamentoService.class.getName());

    @Autowired
    private LancamentoRepository lancamentoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    
    public List<LancamentoVO> findAll() {
        logger.info("Encontrando todos os Lancamentos!");

        List<LancamentoVO> lancamentos = DozerMapper.parseListObjects(lancamentoRepository.findAll(), LancamentoVO.class);
        
        lancamentos
            .stream()
            .forEach(l -> l.add(linkTo(methodOn(LancamentoController.class).findById(l.getCodigo())).withSelfRel()));
        
        return lancamentos;
    }


    public LancamentoVO findById(Long id) {
        logger.info("Encontrando Lancamento pelo ID");
        Lancamento lancamento = lancamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento not encontrado pelo ID: " + id));
        LancamentoVO vo = DozerMapper.parseObject(lancamento, LancamentoVO.class);
		vo.add(linkTo(methodOn(LancamentoController.class).findById(id)).withSelfRel());
		return vo;
    }

    public LancamentoVO create(LancamentoVO lancamentoVO) {
        logger.info("Salvando um novo Lancamento");

        // Busca a pessoa pelo código no VO e verifica se está inativa
        Pessoa pessoa = pessoaRepository.findById(lancamentoVO.getPessoa().getCodigo())
            .orElseThrow(() -> new PessoaInexistenteOuInativaException());

        if (pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        // Conversão do VO para a entidade Lancamento
        Lancamento entity = DozerMapper.parseObject(lancamentoVO, Lancamento.class);
        
        // Salvamento da entidade e conversão de volta para o VO
        Lancamento savedEntity = lancamentoRepository.save(entity);
        LancamentoVO vo = DozerMapper.parseObject(savedEntity, LancamentoVO.class);
        
        // Adição de link HATEOAS
        vo.add(linkTo(methodOn(LancamentoController.class).findById(vo.getCodigo())).withSelfRel());
        
        return vo;
    }


    public LancamentoVO update(LancamentoVO lancamentoVO) {
        logger.info("Atualizando Lancamento");

        // Busca o Lancamento pelo código no VO
        Lancamento lancamento = lancamentoRepository.findById(lancamentoVO.getCodigo())
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento not found for update"));

        // Atualiza os campos do Lancamento
        lancamento.setDescricao(lancamentoVO.getDescricao());
        // Outros campos a serem atualizados...

        // Salva o Lancamento atualizado no repositório
        Lancamento updatedLancamento = lancamentoRepository.save(lancamento);

        // Converte a entidade atualizada para o VO
        LancamentoVO vo = DozerMapper.parseObject(updatedLancamento, LancamentoVO.class);
        
        // Adiciona o link HATEOAS
        vo.add(linkTo(methodOn(LancamentoController.class).findById(vo.getCodigo())).withSelfRel());

        return vo;
    }


    public boolean delete(Long id) {
        logger.info("Deletando Lancamento");
        var entity = lancamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento não encontrado para deletar"));

        lancamentoRepository.delete(entity);
        return true;
    }
}

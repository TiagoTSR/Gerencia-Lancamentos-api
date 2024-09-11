package br.com.xdecodex.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        logger.info("Finding all Lancamentos");
        return DozerMapper.parseListObjects(lancamentoRepository.findAll(), LancamentoVO.class);
    }

    public LancamentoVO findById(Long id) {
        logger.info("Finding Lancamento by ID");
        var lancamento = lancamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento not found for ID: " + id));
        return DozerMapper.parseObject(lancamento, LancamentoVO.class);
    }

    public LancamentoVO save(LancamentoVO lancamentoVO) {
        logger.info("Saving a new Lancamento");
        Pessoa pessoa = pessoaRepository.findById(lancamentoVO.getPessoa().getCodigo())
            .orElseThrow(() -> new PessoaInexistenteOuInativaException());

        if (pessoa.isInativo()) {
            throw new PessoaInexistenteOuInativaException();
        }

        var entity = DozerMapper.parseObject(lancamentoVO, Lancamento.class);
        var savedEntity = lancamentoRepository.save(entity);
        return DozerMapper.parseObject(savedEntity, LancamentoVO.class);
    }

    public LancamentoVO update(LancamentoVO lancamentoVO) {
        logger.info("Updating Lancamento");
        var entity = lancamentoRepository.findById(lancamentoVO.getCodigo())
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento not found for update"));

        entity.setDescricao(lancamentoVO.getDescricao());
        // Outros campos a serem atualizados...

        var updatedEntity = lancamentoRepository.save(entity);
        return DozerMapper.parseObject(updatedEntity, LancamentoVO.class);
    }

    public boolean delete(Long id) {
        logger.info("Deleting Lancamento");
        var entity = lancamentoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Lancamento not found for delete"));

        lancamentoRepository.delete(entity);
        return true;
    }
}

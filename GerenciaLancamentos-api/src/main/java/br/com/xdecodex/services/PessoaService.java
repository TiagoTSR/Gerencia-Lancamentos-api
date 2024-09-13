package br.com.xdecodex.services;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.xdecodex.model.Endereco;
import br.com.xdecodex.data.vo.v1.PessoaVO;
import br.com.xdecodex.data.vo.v2.PessoaVOV2;
import br.com.xdecodex.model.Pessoa;
import br.com.xdecodex.repositories.PessoaRepository;
import br.com.xdecodex.exceptions.ResourceNotFoundException;
import br.com.xdecodex.mapper.DozerMapper;
import br.com.xdecodex.mapper.custom.PessoaMapper;

@Service
public class PessoaService {

    private Logger logger = Logger.getLogger(PessoaService.class.getName());

    @Autowired
    private PessoaRepository repository;
    
    @Autowired
    private PessoaMapper mapper;

    public List<PessoaVO> findAll() {
        logger.info("Finding all people!");
        return DozerMapper.parseListObjects(repository.findAll(), PessoaVO.class);
    }

    public PessoaVO findById(Long id) {
        logger.info("Finding one person!");
        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return DozerMapper.parseObject(entity, PessoaVO.class);
    }

    public PessoaVO create(PessoaVO pessoa) {
        logger.info("Creating one person!");
        var entity = DozerMapper.parseObject(pessoa, Pessoa.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PessoaVO.class);
        return vo;
    }
    
    public PessoaVOV2 createV2(PessoaVOV2 pessoa) {
        logger.info("Creating one person with V2!");
        var entity = mapper.convertyVoToEntity(pessoa);
        var vo = mapper.convertyEntityToVo(repository.save(entity));
        return vo;
    }

    public PessoaVO update(PessoaVO pessoa) {
        logger.info("Updating one person!");
        var entity = repository.findById(pessoa.getCodigo())
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setNome(pessoa.getNome());

        // Atualizando o endereço
        Endereco endereco = pessoa.getEndereco();
        if (endereco != null) {
            entity.setEndereco(endereco);
        }

        var vo = DozerMapper.parseObject(repository.save(entity), PessoaVO.class);
        return vo;
    }

    public boolean delete(Long id) {
        logger.info("Deleting one person!");
        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
        return true;
    }
}

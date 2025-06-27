package br.com.xdecodex.repositories.listener;

import org.springframework.util.StringUtils;

import br.com.xdecodex.GerenciaApplication;
import br.com.xdecodex.model.Lancamento;
import br.com.xdecodex.storage.S3;
import jakarta.persistence.PostLoad;

public class LancamentoAnexoListener {
	
	@PostLoad
	public void postLoad(Lancamento lancamento) {
		if (StringUtils.hasText(lancamento.getAnexo())) {
			S3 s3 = GerenciaApplication.getBean(S3.class);
			lancamento.setUrlAnexo(s3.configurarUrl(lancamento.getAnexo()));
		}
	}

}

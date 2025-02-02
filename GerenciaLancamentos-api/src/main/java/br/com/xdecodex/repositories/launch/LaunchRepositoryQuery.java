package br.com.xdecodex.repositories.launch;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.dto.LaunchStatisticCategory;
import br.com.xdecodex.repositories.filter.LaunchFilter;
import br.com.xdecodex.repositories.projection.ResumeLaunch;

public interface LaunchRepositoryQuery {

	public List<LaunchStatisticCategory> porCategoria(LocalDate mesReferencia);

	List<LaunchVO> filtrar(LaunchFilter launchFilter,Pageable pageable);

	public Page<ResumeLaunch> resumir(LaunchFilter launchFilter, Pageable pageable);
}
package br.com.xdecodex.repositories.launch;

import java.util.List;

import br.com.xdecodex.data.vo.v1.LaunchVO;
import br.com.xdecodex.repositories.filter.LaunchFilter;

public interface LaunchRepositoryQuery {
    List<LaunchVO> filtrar(LaunchFilter launchFilter);
}
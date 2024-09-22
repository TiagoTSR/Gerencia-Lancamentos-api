package br.com.xdecodex.model;

import jakarta.annotation.Generated;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;
import java.math.BigDecimal;
import java.time.LocalDate;

@StaticMetamodel(Lancamento.class)
@Generated("org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
public abstract class Lancamento_ {

	
	/**
	 * @see br.com.xdecodex.model.Lancamento#codigo
	 **/
	public static volatile SingularAttribute<Lancamento, Long> codigo;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#observacao
	 **/
	public static volatile SingularAttribute<Lancamento, String> observacao;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#tipo
	 **/
	public static volatile SingularAttribute<Lancamento, TipoLancamento> tipo;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#dataPagamento
	 **/
	public static volatile SingularAttribute<Lancamento, LocalDate> dataPagamento;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#pessoa
	 **/
	public static volatile SingularAttribute<Lancamento, Pessoa> pessoa;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#dataVencimento
	 **/
	public static volatile SingularAttribute<Lancamento, LocalDate> dataVencimento;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#categoria
	 **/
	public static volatile SingularAttribute<Lancamento, Categoria> categoria;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#valor
	 **/
	public static volatile SingularAttribute<Lancamento, BigDecimal> valor;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento
	 **/
	public static volatile EntityType<Lancamento> class_;
	
	/**
	 * @see br.com.xdecodex.model.Lancamento#descricao
	 **/
	public static volatile SingularAttribute<Lancamento, String> descricao;

	public static final String CODIGO = "codigo";
	public static final String OBSERVACAO = "observacao";
	public static final String TIPO = "tipo";
	public static final String DATA_PAGAMENTO = "dataPagamento";
	public static final String PESSOA = "pessoa";
	public static final String DATA_VENCIMENTO = "dataVencimento";
	public static final String CATEGORIA = "categoria";
	public static final String VALOR = "valor";
	public static final String DESCRICAO = "descricao";

}


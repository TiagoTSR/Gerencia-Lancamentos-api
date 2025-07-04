package br.com.xdecodex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class GerenciaApplication {
	
	private static ApplicationContext APPLICATION_CONTEXT;

	public static void main(String[] args) {
		APPLICATION_CONTEXT = SpringApplication.run(GerenciaApplication.class, args);
	}

	public static <T> T getBean(Class<T> type) {
		return APPLICATION_CONTEXT.getBean(type);
	}
}

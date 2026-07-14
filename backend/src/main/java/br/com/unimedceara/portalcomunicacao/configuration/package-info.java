/**
 * Configurações compartilhadas da aplicação.
 *
 * <p>Esta camada centraliza beans de infraestrutura transversal, sem regras de negócio,
 * sem dependência de Features e sem configuração de autenticação, autorização ou integrações externas.</p>
 *
 * <p>O Bean Validation é provido automaticamente pelo Spring Boot via
 * {@code spring-boot-starter-validation}. Não há {@code ValidationConfiguration} nesta Sprint,
 * pois não existe customização adicional necessária além da auto-configuração padrão.</p>
 */
package br.com.unimedceara.portalcomunicacao.configuration;

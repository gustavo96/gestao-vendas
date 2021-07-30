package com.gvendas.gestaovendas.dto.produto;

import com.gvendas.gestaovendas.entity.Categoria;
import com.gvendas.gestaovendas.entity.Produto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("Produto Requisicao DTO")
public class ProdutoRequestDTO {

    @ApiModelProperty(value = "Descrição")
    @Length(min = 3, max = 100, message = "descricao")
    private String descricao;

    @ApiModelProperty(value = "Quantidade")
    @NotNull(message = "quantidade")
    private Integer quantidade;

    @ApiModelProperty(value = "Preço Custo")
    @NotNull(message = "preco custo")
    private BigDecimal precoCusto;

    @ApiModelProperty(value = "Preço Venda")
    @NotNull(message = "preco venda")
    private BigDecimal precoVenda;

    @ApiModelProperty(value = "Observação")
    @Length(max = 500, message = "observacao")
    private String observacao;

    public Produto converterParaEntidade(Long codigoCategoria) {
        return new Produto(descricao, quantidade, precoCusto, precoVenda, observacao, new Categoria(codigoCategoria));

    }

    public Produto converterParaEntidade(Long codigoCategoria, Long codigoProduto) {
        return new Produto(codigoProduto,descricao, quantidade, precoCusto, precoVenda, observacao, new Categoria(codigoCategoria));

    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoCusto() {
        return precoCusto;
    }

    public void setPrecoCusto(BigDecimal precoCusto) {
        this.precoCusto = precoCusto;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
}

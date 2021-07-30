package com.gvendas.gestaovendas.dto.venda;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("Item Venda requisicao DTO")
public class ItemVendaRequestDTO {

    @ApiModelProperty(value = "Quantidade")
    @NotNull(message = "Quantidade")
    @Min(value = 1, message = "Quantidade")
    private Integer quantidade;

    @ApiModelProperty(value = "Preco Vendido")
    @NotNull(message = "Preco Vendido")
    private BigDecimal precoVendido;

    @ApiModelProperty(value = "Codigo Produto")
    @NotNull(message = "Codigo Produto")
    private Long codigoProduto;

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoVendido() {
        return precoVendido;
    }

    public void setPrecoVendido(BigDecimal precoVendido) {
        this.precoVendido = precoVendido;
    }

    public Long getCodigoProduto() {
        return codigoProduto;
    }

    public void setCodigoProduto(Long codigoProduto) {
        this.codigoProduto = codigoProduto;
    }
}

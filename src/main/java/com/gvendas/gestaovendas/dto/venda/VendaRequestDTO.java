package com.gvendas.gestaovendas.dto.venda;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@ApiModel("Venda requisicao DTO")
public class VendaRequestDTO {

    @ApiModelProperty(value = "Data")
    @NotNull(message = "Data")
    private LocalDate data;

    @ApiModelProperty(value = "Itens da Venda")
    @NotNull(message = "Itens Venda")
    @Valid
    private List<ItemVendaRequestDTO> itemVendaRequestDTOS;

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ItemVendaRequestDTO> getItemVendaRequestDTOS() {
        return itemVendaRequestDTOS;
    }

    public void setItemVendaRequestDTOS(List<ItemVendaRequestDTO> itemVendaRequestDTOS) {
        this.itemVendaRequestDTOS = itemVendaRequestDTOS;
    }
}

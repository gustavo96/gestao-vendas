package com.gvendas.gestaovendas.dto.venda;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.time.LocalDate;
import java.util.List;

@ApiModel("Venda retorno DTO")
public class VendaResponseDTO {

    @ApiModelProperty(value = "Codigo")
    private Long codigo;

    @ApiModelProperty(value = "Data")
    private LocalDate data;

    @ApiModelProperty(value = "Itens da Venda")
    private List<ItemVendaResponseDTO> itemVendaDTOs;

    public VendaResponseDTO(Long codigo, LocalDate data, List<ItemVendaResponseDTO> itemVendaDTOs) {
        this.codigo = codigo;
        this.data = data;
        this.itemVendaDTOs = itemVendaDTOs;
    }

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ItemVendaResponseDTO> getItemVendaDTOs() {
        return itemVendaDTOs;
    }

    public void setItemVendaDTOs(List<ItemVendaResponseDTO> itemVendaDTOs) {
        this.itemVendaDTOs = itemVendaDTOs;
    }
}

package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.ItemVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entity.ItemVenda;
import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.entity.Venda;
import com.gvendas.gestaovendas.repository.ItemVendaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractVendaService {

    protected ClienteVendaResponseDTO retornandoClienteVendaResponseDto(Venda venda, List<ItemVenda> itemVendaList){
        return new ClienteVendaResponseDTO(venda.getCliente().getNome(), Arrays.asList(
                criandoVendaResponseDTO(venda,itemVendaList)));
    }

    protected VendaResponseDTO criandoVendaResponseDTO(Venda venda, List<ItemVenda> itemVendaList){
        List<ItemVendaResponseDTO> itemVendas = itemVendaList.stream()
                .map(this::criandoItemVendaResponseDto).collect(Collectors.toList());
        return new VendaResponseDTO(venda.getCodigo(),venda.getData(),itemVendas);
    }

    protected ItemVendaResponseDTO criandoItemVendaResponseDto(ItemVenda itemVenda){
        return new ItemVendaResponseDTO(itemVenda.getCodigo(),itemVenda.getQuantidade(),itemVenda.getPrecoVendido(),
                itemVenda.getProduto().getCodigo(),itemVenda.getProduto().getDescricao());
    }

    protected ItemVenda criandoItemVenda(ItemVendaRequestDTO itemVendaDto, Venda venda){
        return new ItemVenda(itemVendaDto.getQuantidade(), itemVendaDto.getPrecoVendido(),
                new Produto(itemVendaDto.getCodigoProduto()),venda);
    }
}

package com.gvendas.gestaovendas.service;

import com.gvendas.gestaovendas.dto.venda.*;
import com.gvendas.gestaovendas.entity.Cliente;
import com.gvendas.gestaovendas.entity.ItemVenda;
import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.entity.Venda;
import com.gvendas.gestaovendas.exception.RegraNegocioException;
import com.gvendas.gestaovendas.repository.ItemVendaRepositorio;
import com.gvendas.gestaovendas.repository.VendaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VendaService extends AbstractVendaService {

    private VendaRepositorio vendaRepositorio;
    private ItemVendaRepositorio itemVendaRepositorio;
    private ClienteService clienteService;
    private ProdutoService produtoService;

    @Autowired
    public VendaService(VendaRepositorio vendaRepositorio, ItemVendaRepositorio itemVendaRepositorio,
                        ClienteService clienteService, ProdutoService produtoService) {
        this.vendaRepositorio = vendaRepositorio;
        this.itemVendaRepositorio = itemVendaRepositorio;
        this.clienteService = clienteService;
        this.produtoService = produtoService;
    }

    public ClienteVendaResponseDTO listaVendaPorCliente(Long codigoCliente) {
        Cliente cliente = validarClienteVendaExiste(codigoCliente);
        List<VendaResponseDTO> vendaResponseDTOs = vendaRepositorio.findByClienteCodigo(codigoCliente).stream()
                .map(venda -> criandoVendaResponseDTO(venda, itemVendaRepositorio.findByVendaPorCodigo(venda.getCodigo()))).collect(Collectors.toList());
        return new ClienteVendaResponseDTO(cliente.getNome(), vendaResponseDTOs);
    }

    public ClienteVendaResponseDTO listarVendaPorCodigo(Long codigoVenda) {
        Venda venda = validarVendaExiste(codigoVenda);
        return retornandoClienteVendaResponseDto(venda, itemVendaRepositorio.findByVendaPorCodigo(venda.getCodigo()));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public ClienteVendaResponseDTO salvar(Long codigoCliente, VendaRequestDTO vendaRequestDTO) {
        Cliente cliente = validarClienteVendaExiste(codigoCliente);
        validarProdutoExiste(vendaRequestDTO.getItemVendaRequestDTOS());
        Venda vendaSalva = salvarVenda(cliente, vendaRequestDTO);
        return retornandoClienteVendaResponseDto(vendaSalva, itemVendaRepositorio.findByVendaPorCodigo(vendaSalva.getCodigo()));
    }

    public ClienteVendaResponseDTO atualizar(Long codigoVenda, Long codigoCliente, VendaRequestDTO vendaDto) {
        validarVendaExiste(codigoVenda);
        Cliente cliente = validarClienteVendaExiste(codigoCliente);
        List<ItemVenda> itemVendaList = itemVendaRepositorio.findByVendaPorCodigo(codigoVenda);
        validarProdutoExisteEDevolverEstoque(itemVendaList);
        validarProdutoExiste(vendaDto.getItemVendaRequestDTOS());
        itemVendaRepositorio.deleteAll(itemVendaList);
        Venda vendaAtualizada = atualizarVenda(codigoVenda, cliente, vendaDto);
        return retornandoClienteVendaResponseDto(vendaAtualizada,
                itemVendaRepositorio.findByVendaPorCodigo(vendaAtualizada.getCodigo()));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void deletar(Long codigoVenda) {
        Venda venda = validarVendaExiste(codigoVenda);
        List<ItemVenda> itemVendas = itemVendaRepositorio.findByVendaPorCodigo(codigoVenda);
        validarProdutoExisteEDevolverEstoque(itemVendas);
        itemVendaRepositorio.deleteAll(itemVendas);
        vendaRepositorio.deleteById(codigoVenda);
    }

    private void validarProdutoExisteEDevolverEstoque(List<ItemVenda> itemVendas) {
        itemVendas.forEach(itemVenda -> {
            Produto produto = produtoService.validarProdutoExiste(itemVenda.getProduto().getCodigo());
            produto.setQuantidade(produto.getQuantidade() + itemVenda.getQuantidade());
        });
    }

    private Venda salvarVenda(Cliente cliente, VendaRequestDTO vendaDto) {
        Venda vendaSalva = vendaRepositorio.save(new Venda(vendaDto.getData(), cliente));
        vendaDto.getItemVendaRequestDTOS().stream().map(itemVendaDto -> criandoItemVenda(itemVendaDto, vendaSalva)).
                forEach(itemVendaRepositorio::save);
        return vendaSalva;
    }

    private Venda atualizarVenda(Long codigoVenda, Cliente cliente, VendaRequestDTO vendaDto) {
        Venda vendaSalva = vendaRepositorio.save(new Venda(codigoVenda, vendaDto.getData(), cliente));
        vendaDto.getItemVendaRequestDTOS().stream().map(itemVendaDto -> criandoItemVenda(itemVendaDto, vendaSalva)).
                forEach(itemVendaRepositorio::save);
        return vendaSalva;
    }

    private void validarProdutoExiste(List<ItemVendaRequestDTO> itemVendaDto) {
        itemVendaDto.forEach(item -> {
            Produto produto = produtoService.validarProdutoExiste(item.getCodigoProduto());
            validarQuantidadeProdutoExiste(produto, item.getQuantidade());
            produto.setQuantidade(produto.getQuantidade() - item.getQuantidade());
            produtoService.atualizarQuantidadeAposVenda(produto);
        });
    }

    private Venda validarVendaExiste(Long codigoVenda) {
        Optional<Venda> venda = vendaRepositorio.findById(codigoVenda);
        if (venda.isEmpty()) {
            throw new RegraNegocioException(String.format("Venda de código %s nao encontrada", codigoVenda));
        }
        return venda.get();
    }

    private void validarQuantidadeProdutoExiste(Produto produto, Integer qtdeVendaDto) {
        if (produto.getQuantidade() >= qtdeVendaDto) {
            throw new RegraNegocioException(String.format("A quantidade %s informada para " +
                    "o produto %s nao esta disponivel em estoque", qtdeVendaDto, produto.getDescricao()));
        }
    }

    private Cliente validarClienteVendaExiste(Long codigoCliente) {
        Optional<Cliente> cliente = clienteService.buscaPorId(codigoCliente);
        if (cliente.isEmpty()) {
            throw new RegraNegocioException(String.format("O cliente do código %s informado nao existe no cadastro", codigoCliente));
        }
        return cliente.get();
    }

}

package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.produto.ProdutoRequestDTO;
import com.gvendas.gestaovendas.dto.produto.ProdutoResponseDTO;
import com.gvendas.gestaovendas.dto.venda.ClienteVendaResponseDTO;
import com.gvendas.gestaovendas.dto.venda.VendaRequestDTO;
import com.gvendas.gestaovendas.dto.venda.VendaResponseDTO;
import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.entity.Venda;
import com.gvendas.gestaovendas.service.VendaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "Venda")
@RestController
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    private VendaService vendaService;

    @ApiOperation(value = "Listar Vendas por cliente")
    @GetMapping("/cliente/{codigoCliente}")
    public ResponseEntity<ClienteVendaResponseDTO> listarVendaPorCliente(@PathVariable Long codigoCliente) {
        return ResponseEntity.ok(vendaService.listaVendaPorCliente(codigoCliente));
    }

    @ApiOperation(value = "Listar Vendas por codigo")
    @GetMapping("/{codigoVenda}")
    public ResponseEntity<ClienteVendaResponseDTO> listarVendaPorCodigo(@PathVariable Long codigoVenda) {
        return ResponseEntity.ok(vendaService.listarVendaPorCodigo(codigoVenda));
    }

    @ApiOperation(value = "Salvar Venda")
    @PostMapping("/cliente/{codigoCliente}")
    public ResponseEntity<ClienteVendaResponseDTO> salvar(@PathVariable Long codigoCliente, @Valid @RequestBody VendaRequestDTO vendaDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vendaService.salvar(codigoCliente, vendaDto));
    }

    @ApiOperation(value = "Atualizar Venda")
    @PutMapping("/{codigoVenda}/cliente/{codigoCliente}")
    public ResponseEntity<ClienteVendaResponseDTO> atualizar(@PathVariable Long codigoVenda,
                                                             @PathVariable Long codigoCliente, @Valid @RequestBody VendaRequestDTO vendaDto) {
        return ResponseEntity.ok(vendaService.atualizar(codigoVenda, codigoCliente, vendaDto));
    }

    @ApiOperation(value = "Deletar Venda")
    @DeleteMapping("/{codigoVenda}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long codigoVenda) {
        vendaService.deletar(codigoVenda);
    }
}

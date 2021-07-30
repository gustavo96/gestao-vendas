package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.produto.ProdutoRequestDTO;
import com.gvendas.gestaovendas.dto.produto.ProdutoResponseDTO;
import com.gvendas.gestaovendas.entity.Produto;
import com.gvendas.gestaovendas.service.ProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api(tags = "Produto")
@RestController
@RequestMapping("categoria/{codigoCategoria}/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @ApiOperation(value = "Listar Todos Produtos")
    @GetMapping
    public List<ProdutoResponseDTO> listarTodos(@PathVariable long codigoCategoria) {
        return produtoService.listarTodos(codigoCategoria).stream().map(produto ->
                ProdutoResponseDTO.converteParaProduto(produto)).collect(Collectors.toList());
    }

    @ApiOperation(value = "Busca por codigo produto")
    @GetMapping("/{codigo}")
    public ResponseEntity<ProdutoResponseDTO> buscaPorId(@PathVariable Long codigo, @PathVariable Long codigoCategoria) {
        Optional<Produto> produto = produtoService.buscaPorId(codigo, codigoCategoria);
        return produto.isPresent() ? ResponseEntity.ok(ProdutoResponseDTO.converteParaProduto(produto.get())) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar Produto")
    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(Long codigoCategoria, @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoSalvo = produtoService.salvar(codigoCategoria, produtoRequestDTO.converterParaEntidade(codigoCategoria));
        return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponseDTO.converteParaProduto(produtoSalvo));
    }

    @ApiOperation(value = "Alterar Produto")
    @PutMapping("/{codigo}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long codigoProduto,
                                                        @PathVariable Long codigoCategoria, @Valid @RequestBody ProdutoRequestDTO produtoRequestDTO) {
        Produto produtoAtualizado = produtoService.atualizar(codigoCategoria, codigoProduto, produtoRequestDTO.converterParaEntidade(codigoCategoria, codigoProduto));
        return ResponseEntity.ok(ProdutoResponseDTO.converteParaProduto(produtoAtualizado));
    }

    @ApiOperation(value = "Deletar Produto")
    @DeleteMapping("/{codigoProduto}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long codigoCategoria, @PathVariable Long codigoProduto) {
        produtoService.deletar(codigoCategoria, codigoProduto);
    }
}

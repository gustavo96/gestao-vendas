package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.categoria.CategoriaRequestDTO;
import com.gvendas.gestaovendas.dto.categoria.CategoriaResponseDTO;
import com.gvendas.gestaovendas.entity.Categoria;
import com.gvendas.gestaovendas.service.CategoriaService;
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

@Api(tags = "Categoria")
@RestController
@RequestMapping("/categoria")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @ApiOperation(value = "Listar Tudo")
    @GetMapping
    public List<CategoriaResponseDTO> listarTodas(){
         return categoriaService.listarTodas().stream().map(categoria ->
                 CategoriaResponseDTO.converterParaCategoriaDTO(categoria)).collect(Collectors.toList());
    }

    @ApiOperation(value = "Listar por id")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscaPorId(@PathVariable Long id){
        Optional<Categoria> categoria = categoriaService.buscaPorId(id);
        return categoria.isPresent() ? ResponseEntity.ok(CategoriaResponseDTO.converterParaCategoriaDTO(categoria.get())) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar")
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> salvar(@Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO){
        Categoria categoriaSalva = categoriaService.salvar(categoriaRequestDTO.converterParaEntidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponseDTO.converterParaCategoriaDTO(categoriaSalva));
    }

    @ApiOperation(value = "Alterar")
    @PutMapping("/{codigo}")
    public ResponseEntity<CategoriaResponseDTO> atualizar(@PathVariable Long codigo, @Valid @RequestBody CategoriaRequestDTO categoriaRequestDTO){
                Categoria categoriaAtualizada = categoriaService.atualizar(codigo,categoriaRequestDTO.converterParaEntidade(codigo));
        return ResponseEntity.ok(CategoriaResponseDTO.converterParaCategoriaDTO(categoriaAtualizada));
    }

    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long codigo){
        categoriaService.deletar(codigo);
    }
}

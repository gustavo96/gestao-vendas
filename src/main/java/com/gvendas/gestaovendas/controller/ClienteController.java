package com.gvendas.gestaovendas.controller;

import com.gvendas.gestaovendas.dto.categoria.CategoriaRequestDTO;
import com.gvendas.gestaovendas.dto.categoria.CategoriaResponseDTO;
import com.gvendas.gestaovendas.dto.cliente.ClienteRequestDTO;
import com.gvendas.gestaovendas.dto.cliente.ClienteResponseDTO;
import com.gvendas.gestaovendas.entity.Categoria;
import com.gvendas.gestaovendas.entity.Cliente;
import com.gvendas.gestaovendas.service.ClienteService;
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

@Api(tags = "Cliente")
@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @ApiOperation(value = "Listar Todos Cliente")
    @GetMapping
    public List<ClienteResponseDTO> listarTodos(){
        return clienteService.listarTodos().stream().map(cliente ->
                ClienteResponseDTO.converterParaClienteDTO(cliente)).collect(Collectors.toList());
    }

    @ApiOperation(value = "Buscar cliente por id")
    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscaPorId(@PathVariable Long id){
        Optional<Cliente> cliente = clienteService.buscaPorId(id);
        return cliente.isPresent() ? ResponseEntity.ok(ClienteResponseDTO.converterParaClienteDTO(cliente.get())) : ResponseEntity.notFound().build();
    }

    @ApiOperation(value = "Salvar Cliente")
    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteRequestDTO clienteRequestDTO){
        Cliente clienteSalvo = clienteService.salvar(clienteRequestDTO.converterParaEntidade());
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteResponseDTO.converterParaClienteDTO(clienteSalvo));
    }

    @ApiOperation(value = "Atualizar Cliente")
    @PutMapping("/{codigo}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long codigo, @Valid @RequestBody ClienteRequestDTO clienteDto){
      Cliente clienteAtualizado = clienteService.atualizar(codigo, clienteDto.converterParaEntidade(codigo));
      return ResponseEntity.ok(ClienteResponseDTO.converterParaClienteDTO(clienteAtualizado));
    }

    @ApiOperation(value = "Deletar Cliente")
    @DeleteMapping("/{codigo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long codigo){
        clienteService.deletar(codigo);
    }

}

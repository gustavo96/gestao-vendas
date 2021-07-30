package com.gvendas.gestaovendas.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "venda")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo")
    private Long codigo;

    @Column(name = "data")
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "codigo_cliente", referencedColumnName = "codigo")
    @NotNull(message = "Codigo Cliente")
    private Cliente cliente;

    public Venda() {
    }

    public Venda(LocalDate data, Cliente cliente) {
        this.data = data;
        this.cliente = cliente;
    }

    public Venda(Long codigo, LocalDate data, Cliente cliente) {
        this.codigo = codigo;
        this.data = data;
        this.cliente = cliente;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Objects.equals(codigo, venda.codigo) && Objects.equals(data, venda.data) && Objects.equals(cliente, venda.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo, data, cliente);
    }
}

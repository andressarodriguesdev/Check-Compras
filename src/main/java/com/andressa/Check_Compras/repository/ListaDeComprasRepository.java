package com.andressa.Check_Compras.repository;

import com.andressa.Check_Compras.model.ListaDeCompras;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListaDeComprasRepository extends JpaRepository<ListaDeCompras, Long> {
    List<ListaDeCompras> findByMesAnoContainingIgnoreCase(String mesAno);
}

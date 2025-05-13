package com.andressa.Check_Compras.repository;

import com.andressa.Check_Compras.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto,Long> {
    List<Produto> findByListaId(Long listaId);
}

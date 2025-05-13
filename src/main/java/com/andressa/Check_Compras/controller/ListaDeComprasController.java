package com.andressa.Check_Compras.controller;

import com.andressa.Check_Compras.model.ListaDeCompras;
import com.andressa.Check_Compras.model.Produto;
import com.andressa.Check_Compras.repository.ListaDeComprasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/listas")
@CrossOrigin(origins = "*")
public class ListaDeComprasController {

    private final ListaDeComprasRepository listaRepository;

    @Autowired
    public ListaDeComprasController(ListaDeComprasRepository listaRepository) {
        this.listaRepository = listaRepository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ListaDeCompras criarLista(@RequestBody ListaDeCompras lista) {
        lista.setId(null); // Garante que está criando e não atualizando
        for (Produto produto : lista.getProdutos()) {
            produto.setLista(lista); // associa os produtos à lista
        }
        return listaRepository.save(lista);
    }

    // Buscar todas as listas
    @GetMapping
    public List<ListaDeCompras> listarTodas() {
        return listaRepository.findAll();
    }

    // Buscar lista por ID
    @GetMapping("/{id}")
    public ListaDeCompras buscarPorId(@PathVariable Long id) {
        return listaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Lista não encontrada."));
    }

    // Buscar listas por mesAno (ex: "05-2025")
    @GetMapping("/mes/{mesAno}")
    public List<ListaDeCompras> buscarPorMesAno(@PathVariable String mesAno) {
        List<ListaDeCompras> listas = listaRepository.findByMesAnoContainingIgnoreCase(mesAno);
        if (listas.isEmpty()) {
            throw new ResponseStatusException(NOT_FOUND, "Nenhuma lista encontrada para o mês informado.");
        }
        return listas;
    }

    @PutMapping("/{id}")
    public ListaDeCompras atualizarLista(@PathVariable Long id, @RequestBody ListaDeCompras listaAtualizada) {
        ListaDeCompras listaExistente = listaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lista não encontrada"));

        // Atualiza os dados da lista
        listaExistente.setNome(listaAtualizada.getNome());
        listaExistente.setMesAno(listaAtualizada.getMesAno());

        // Remove os produtos antigos e adiciona os novos
        listaExistente.getProdutos().clear();
        for (Produto produto : listaAtualizada.getProdutos()) {
            produto.setLista(listaExistente); // associa o produto à lista
            listaExistente.getProdutos().add(produto);
        }

        return listaRepository.save(listaExistente);
    }


    // Deletar uma lista
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletarLista(@PathVariable Long id) {
        if (!listaRepository.existsById(id)) {
            throw new ResponseStatusException(NOT_FOUND, "Lista não encontrada.");
        }
        listaRepository.deleteById(id);
    }
}

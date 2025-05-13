package com.andressa.Check_Compras.controller;

import com.andressa.Check_Compras.model.Produto;
import com.andressa.Check_Compras.repository.ProdutoRepository;
import com.andressa.Check_Compras.repository.ListaDeComprasRepository; // Adicionando o repositório da ListaDeCompras
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ListaDeComprasRepository listaRepository; // Para verificar se a lista existe

    // Listar todos os produtos
    @GetMapping
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Buscar produto por ID
    @GetMapping("/{id}")
    public Produto buscarPorId(@PathVariable Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado."));
    }

    // Criar novo produto
    @PostMapping
    @ResponseStatus(CREATED)
    public Produto criarProduto(@RequestBody @Valid Produto produto) {
        if (produto.getLista() != null) {
            // Verificar se a lista existe antes de associar ao produto
            if (!listaRepository.existsById(produto.getLista().getId())) {
                throw new ResponseStatusException(NOT_FOUND, "Lista não encontrada.");
            }
        }

        // Se a lista foi associada corretamente, salvar o produto
        return produtoRepository.save(produto);
    }

    // Atualizar produto existente
    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Long id, @RequestBody @Valid Produto produtoAtualizado) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado."));

        // Atualizar as informações do produto
        produto.setNome(produtoAtualizado.getNome());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setQuantidade(produtoAtualizado.getQuantidade());
        produto.setMarca(produtoAtualizado.getMarca());

        if (produtoAtualizado.getLista() != null) {
            // Verificar se a lista existe antes de associar
            if (!listaRepository.existsById(produtoAtualizado.getLista().getId())) {
                throw new ResponseStatusException(NOT_FOUND, "Lista não encontrada.");
            }
            produto.setLista(produtoAtualizado.getLista());
        }

        return produtoRepository.save(produto);
    }

    // Deletar produto
    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    public void deletar(@PathVariable Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Produto não encontrado."));
        produtoRepository.delete(produto);
    }

    // Listar produtos de uma lista específica
    @GetMapping("/lista/{listaId}")
    public List<Produto> listarProdutosDaLista(@PathVariable Long listaId) {
        return produtoRepository.findByListaId(listaId);
    }
}

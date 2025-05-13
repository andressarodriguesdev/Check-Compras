package com.andressa.Check_Compras.controller;


import com.andressa.Check_Compras.model.Usuario;
import com.andressa.Check_Compras.repository.UsuarioRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) {
        // Verifica se nome de usuário já existe
        if (usuarioRepository.findByNome(usuario.getNome()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Usuario saved = usuarioRepository.save(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest request) {
        return usuarioRepository.findByNome(request.getNome())
                .filter(u -> u.getSenha().equals(request.getSenha()))
                .map(u -> ResponseEntity.ok("Login bem-sucedido"))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas"));
    }

    @Data
    private static class AuthRequest {
        private String nome;
        private String senha;
    }
}

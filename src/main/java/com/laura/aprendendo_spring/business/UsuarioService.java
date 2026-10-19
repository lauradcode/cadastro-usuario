package com.laura.aprendendo_spring.business;

import com.laura.aprendendo_spring.infrastructure.entity.Usuario;
import com.laura.aprendendo_spring.infrastructure.exceptions.ConflictException;
import com.laura.aprendendo_spring.infrastructure.exceptions.ResourceNotFoundException;
import com.laura.aprendendo_spring.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class UsuarioService {

    private final UsuarioRepository usuarioRepository; /*vai ser iniciada*/
    private final PasswordEncoder passwordEncoder; /*ACHO QUE ADICIONEI ERRADO*/

    public Usuario salvaUsuario (Usuario usuario){ /*p salvar usuario*/
        try {
            emailExiste(usuario.getEmail()); /*regra de negócio p ver se já existe*/
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha())); /*ACHO QUE ADICIONEI ERRADO*/
            return usuarioRepository.save(usuario);
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado", e.getCause());

        }

    }
    public void emailExiste (String email){ /*aqui vai verificar se o email ja existe e vai gerar uma exception*/
        try {
            boolean existe = verificarEmailExistente(email);
            if (existe){
                throw new ConflictException( "Email já cadastrado" + email);
            }
        } catch (ConflictException e){
            throw new ConflictException("Email já cadastrado" + e.getCause());
        }

    }

    public boolean verificarEmailExistente (String email){/*responsável apenas por chamar o metodo na repository*/

        return usuarioRepository.existsByEmail(email);
    }

    public Usuario buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
                "email não encontrado" + email));
    }

    public void deletaUsuarioPorEmail(String email) {
        usuarioRepository.deleteByEmail(email);
    }
}
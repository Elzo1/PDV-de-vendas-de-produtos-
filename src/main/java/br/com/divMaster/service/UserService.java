package br.com.divMaster.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.divMaster.Enum.Role;
import br.com.divMaster.entity.User;
import br.com.divMaster.entity.UserDetailsImp;
import br.com.divMaster.repositorio.UserRepository;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Carrega o usuário com tratamento de erro caso não seja encontrado
        User user = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado."));
        
        return new UserDetailsImp(user);
    }

    public User createUser(User user) {
        // Codifica a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Define o papel padrão caso nenhum seja fornecido
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(Set.of(Role.USER));  // Papel padrão: USER
        }
        
        return repository.save(user);
    }

    public User updateUser(User user) {
        // Codifica a senha antes de atualizar o usuário
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repository.save(user);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}

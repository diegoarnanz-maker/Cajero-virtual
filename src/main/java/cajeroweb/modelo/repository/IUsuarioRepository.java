package cajeroweb.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cajeroweb.modelo.entidades.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, Integer> {
    Usuario findByUsername(String username);

}

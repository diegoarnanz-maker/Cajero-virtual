package cajeroweb.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cajeroweb.modelo.entidades.Cuenta;

@Repository
public interface ICuentaRepository extends JpaRepository<Cuenta, Integer> {

    List<Cuenta> findByUsuario_IdUsuario(int idUsuario);
    
}

package cajeroweb.modelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import cajeroweb.modelo.entidades.Rol;

public interface IRolRepository extends JpaRepository<Rol, Integer> {
    Rol findByNombreRol(String nombreRol);

}

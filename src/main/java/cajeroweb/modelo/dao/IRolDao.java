package cajeroweb.modelo.dao;

import cajeroweb.modelo.entidades.Rol;

public interface IRolDao extends IGenericoCrud<Rol, Integer> {

    // Método que busca un rol por su nombre
    Rol findByNombre(String nombre);
}

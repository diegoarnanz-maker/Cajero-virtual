package cajeroweb.modelo.dao;

import cajeroweb.modelo.entidades.Usuario;

public interface IUsuarioDao extends IGenericoCrud<Usuario, Integer> {

    Usuario findByUsername(String username);
}

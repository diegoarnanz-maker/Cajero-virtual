package cajeroweb.modelo.dao;

import java.util.List;

import cajeroweb.modelo.entidades.Cuenta;

public interface ICuentaDao extends IGenericoCrud<Cuenta, Integer> {

    List<Cuenta> findByUsuarioId(int usuarioId);
}

package cajeroweb.modelo.dao;

import java.util.List;

import cajeroweb.modelo.entidades.Movimiento;

public interface IMovimientoDao extends IGenericoCrud<Movimiento, Integer> {

    //Listar movimientos de una cuenta
    List<Movimiento> findByCuentaId(int idCuenta);

    // Método para dar de alta un movimiento, en funcion del tipo de operacion creo un tipo de movimiento
    boolean altaMovimiento(int idCuenta, double cantidad, String operacion);

    boolean transferirDinero(int idCuentaOrigen, int idCuentaDestino, double cantidad);

}

package cajeroweb.modelo.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cajeroweb.modelo.entidades.Cuenta;
import cajeroweb.modelo.entidades.Movimiento;
import cajeroweb.modelo.repository.IMovimientoRepository;

@Repository
public class MovimientoDaoImplJpaMy8 implements IMovimientoDao {
    @Autowired
    private IMovimientoRepository movimientoRepository;

    @Autowired
    private CuentaDaoImplJpaMy8 cuentaDaoImplJpaMy8;

    @Override
    public Movimiento buscarUno(Integer clavePk) {
        try {
            Optional<Movimiento> movimiento = movimientoRepository.findById(clavePk);
            return movimiento.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Movimiento> buscarTodos() {
        try {
            return movimientoRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Movimiento insertUno(Movimiento entidad) {
        try {
            return movimientoRepository.save(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUno(Movimiento entidad) {
        try {
            if (movimientoRepository.existsById(entidad.getIdMovimiento())) {
                movimientoRepository.save(entidad);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int deleteUno(Integer clavePk) {
        try {
            if (movimientoRepository.existsById(clavePk)) {
                movimientoRepository.deleteById(clavePk);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Movimiento> findByCuentaId(int idCuenta) {
        try {
            return movimientoRepository.findByCuenta_IdCuenta(idCuenta);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean altaMovimiento(int idCuenta, double cantidad, String operacion) {
        // Busca la cuenta en la base de datos
        Cuenta cuenta = cuentaDaoImplJpaMy8.buscarUno(idCuenta);
        if (cuenta == null) {
            return false;
        }

        // Decido dar de alta el movimiento en función de la operación de la operacion que elija el cliente
        if ("Ingreso".equalsIgnoreCase(operacion)) {
            cuenta.ingresarDinero(cantidad); //uso los metodos propios de la clase
        } else if ("Extraccion".equalsIgnoreCase(operacion)) {
            if (cuenta.getSaldo() < cantidad) {
                return false; // No dejo dejar en numeros rojos la cuenta
            }
            cuenta.retirarDinero(cantidad);
        } else {
            return false; // Si sale una operacion no definida
        }

        // Guarda la cuenta actualizada en la base de datos
        cuentaDaoImplJpaMy8.insertUno(cuenta);

        // Creo el movimiento y lo guarda en la base de datos
        Movimiento movimiento = new Movimiento();
        movimiento.setCuenta(cuenta);
        movimiento.setCantidad(cantidad);
        movimiento.setOperacion(operacion);
        movimiento.setFecha(new Date());
        movimientoRepository.save(movimiento);

        return true;
    }

    @Override
    public boolean transferirDinero(int idCuentaOrigen, int idCuentaDestino, double cantidad) {
        //primero verifico que la cantidad sea mayor a 0
        if (cantidad <= 0) {
            return false;
        }
        // Asigno y verifico las cuentas que van a participar en la transferencia
        Cuenta cuentaOrigen = cuentaDaoImplJpaMy8.buscarUno(idCuentaOrigen);
        Cuenta cuentaDestino = cuentaDaoImplJpaMy8.buscarUno(idCuentaDestino);

        //Permito dejar la cuenta de origen en 0 pero no en numeros rojos
        if (cuentaOrigen == null || cuentaDestino == null || cuentaOrigen.getSaldo() < cantidad) {
            return false;
        }

        //Logica de la transferencia
        cuentaOrigen.retirarDinero(cantidad);
        cuentaDaoImplJpaMy8.insertUno(cuentaOrigen);

        cuentaDestino.ingresarDinero(cantidad);
        cuentaDaoImplJpaMy8.insertUno(cuentaDestino);

        //Creo los movimientos en ambas cuentas para mostrarlo en la cartilla
        Movimiento movimientoOrigen = new Movimiento();
        movimientoOrigen.setCuenta(cuentaOrigen);
        movimientoOrigen.setCantidad(-cantidad); //Pongo el - porque es extraccion, detalles
        movimientoOrigen.setOperacion("Extraccion por transferencia a cuenta: " + cuentaDestino.getIdCuenta());
        movimientoOrigen.setFecha(new Date());
        //Lo guardo
        movimientoRepository.save(movimientoOrigen);

        Movimiento movimientoDestino = new Movimiento();
        movimientoDestino.setCuenta(cuentaDestino);
        movimientoDestino.setCantidad(cantidad);
        movimientoDestino.setOperacion("Ingreso por transferencia de cuenta: " + cuentaOrigen.getIdCuenta());
        movimientoDestino.setFecha(new Date());
        //Lo guardo
        movimientoRepository.save(movimientoDestino);
        return true;
    }
}

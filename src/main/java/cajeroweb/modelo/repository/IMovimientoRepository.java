package cajeroweb.modelo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;

import cajeroweb.modelo.entidades.Movimiento;

public interface IMovimientoRepository extends JpaRepository<Movimiento, Integer> {

    // Metodos deribados de JpaRepository o query methods
    List<Movimiento> findByCuenta_IdCuenta(int idCuenta);

    // @Query("SELECT m FROM Movimiento m WHERE m.cuenta.idCuenta = ?1")
    // public List<Movimiento> findByMovimientosPorCuenta(int idCuenta);
}

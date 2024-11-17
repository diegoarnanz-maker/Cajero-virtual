package cajeroweb.modelo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cajeroweb.modelo.entidades.Cuenta;
import cajeroweb.modelo.repository.ICuentaRepository;

@Repository
public class CuentaDaoImplJpaMy8 implements ICuentaDao {
    @Autowired
    private ICuentaRepository cuentaRepository;

    @Override
    public Cuenta buscarUno(Integer clavePk) {
        try {
            Optional<Cuenta> cuenta = cuentaRepository.findById(clavePk);
            return cuenta.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Cuenta> buscarTodos() {
        try {
            return cuentaRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Cuenta insertUno(Cuenta entidad) {
        try {
            return cuentaRepository.save(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUno(Cuenta entidad) {
        try {
            if (cuentaRepository.existsById(entidad.getIdCuenta())) {
                cuentaRepository.save(entidad);
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
            if (cuentaRepository.existsById(clavePk)) {
                cuentaRepository.deleteById(clavePk);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Cuenta> findByUsuarioId(int usuarioId) {
        try {
            return cuentaRepository.findByUsuario_IdUsuario(usuarioId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

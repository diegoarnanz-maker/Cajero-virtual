package cajeroweb.modelo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cajeroweb.modelo.entidades.Rol;
import cajeroweb.modelo.repository.IRolRepository;

@Repository
public class RolDaoImplJpaMy8 implements IRolDao {

    @Autowired
    private IRolRepository rolRepository;

    @Override
    public Rol buscarUno(Integer clavePk) {
        try {
            Optional<Rol> rol = rolRepository.findById(clavePk);
            return rol.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Rol> buscarTodos() {
        try {
            return rolRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Rol insertUno(Rol entidad) {
        try {
            return rolRepository.save(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUno(Rol entidad) {
        try {
            if (rolRepository.existsById(entidad.getIdRol())) {
                rolRepository.save(entidad);
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
            if (rolRepository.existsById(clavePk)) {
                rolRepository.deleteById(clavePk);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Rol findByNombre(String nombre) {
        try {
            return rolRepository.findByNombreRol(nombre);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

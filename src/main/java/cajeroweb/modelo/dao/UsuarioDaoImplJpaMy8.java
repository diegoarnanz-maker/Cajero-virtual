package cajeroweb.modelo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cajeroweb.modelo.entidades.Usuario;
import cajeroweb.modelo.repository.IUsuarioRepository;

@Repository
public class UsuarioDaoImplJpaMy8 implements IUsuarioDao {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario buscarUno(Integer clavePk) {
        try {
            Optional<Usuario> usuario = usuarioRepository.findById(clavePk);
            return usuario.orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Usuario> buscarTodos() {
        try {
            return usuarioRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Usuario insertUno(Usuario entidad) {
        try {
            return usuarioRepository.save(entidad);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public int updateUno(Usuario entidad) {
        try {
            if (usuarioRepository.existsById(entidad.getIdUsuario())) {
                usuarioRepository.save(entidad);
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
            if (usuarioRepository.existsById(clavePk)) {
                usuarioRepository.deleteById(clavePk);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Usuario findByUsername(String username) {
        try {
            return usuarioRepository.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package cajeroweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cajeroweb.modelo.dao.ICuentaDao;
import cajeroweb.modelo.dao.IRolDao;
import cajeroweb.modelo.dao.IUsuarioDao;
import cajeroweb.modelo.entidades.Cuenta;
import cajeroweb.modelo.entidades.Rol;
import cajeroweb.modelo.entidades.Usuario;

@Controller
@RequestMapping("/admin")

public class AdminController {
    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private ICuentaDao cuentaDao;

    @Autowired
    private IRolDao rolDao;

    @GetMapping("/menu")
    public String menuAdmin() {
        return "menuAdmin";
    }

    // Hago uso de model porque solo necesito que me dure una tarea, no voy a
    // almacenar nada en la sesión y asi no le doy tanta cana al servidor
    @GetMapping("/listar-usuarios")
    public String listarUsuarios(Model model) {
        model.addAttribute("usuarios", usuarioDao.buscarTodos());
        return "listarUsuariosAdmin";
    }

    @GetMapping("/alta-usuario-cuenta")
    public String altaUsuario() {
        return "altaUsuario";
    }

    @PostMapping("/alta-usuario-cuenta")
    public String altaUsuarioCuenta(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("rol") String rolNombre,
            @RequestParam("tipoCuenta") String tipoCuenta,
            @RequestParam("saldo") double saldo,
            RedirectAttributes ratt) {

        // Obtiene el rol desde la base de datos según el nombre proporcionado
        Rol rol = rolDao.findByNombre(rolNombre);
        if (rol == null) {
            ratt.addFlashAttribute("error", "Rol no encontrado.");
            return "redirect:/admin/menu";
        }

        // Crea y guarda el usuario
        Usuario usuarioAlta = new Usuario();
        usuarioAlta.setUsername(username);
        usuarioAlta.setPassword(password);
        usuarioAlta.setRol(rol);

        usuarioDao.insertUno(usuarioAlta);

        // Crea y guarda la cuenta asociada al usuario ya guardado
        Cuenta cuentaAlta = new Cuenta();
        cuentaAlta.setTipoCuenta(tipoCuenta);
        cuentaAlta.setSaldo(saldo);
        cuentaAlta.setUsuario(usuarioAlta);

        cuentaDao.insertUno(cuentaAlta);

        // Actualiza el usuario con la cuenta asignada
        usuarioAlta.setCuenta(cuentaAlta);
        usuarioDao.updateUno(usuarioAlta);

        ratt.addFlashAttribute("mensaje", "Usuario y cuenta creados correctamente.");
        return "redirect:/admin/listar-usuarios";
    }

}

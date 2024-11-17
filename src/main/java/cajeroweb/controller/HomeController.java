package cajeroweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import cajeroweb.modelo.dao.IUsuarioDao;
import cajeroweb.modelo.entidades.Cuenta;
import cajeroweb.modelo.entidades.Usuario;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cuenta")
public class HomeController {

    @Autowired
    private IUsuarioDao usuarioDao;

    @GetMapping({ "/", "/home", "" })
    public String home() {
        return "home";
    }

    @GetMapping("/menu")
    public String mostrarMenu(HttpSession session, RedirectAttributes ratt) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            ratt.addFlashAttribute("error", "Debe iniciar sesión para acceder al menú.");
            return "redirect:/cuenta/login";
        }

        if (usuario.getRol().getNombreRol().equals("Administrador")) {
            return "redirect:/admin/menu";
        }

        return "menu";
    }

    @GetMapping("/login")
    public String login() {
        return "FormLogin";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes ratt) {
        Usuario usuario = usuarioDao.findByUsername(username);

        if (usuario != null && usuario.getPassword().equals(password)) {
            session.setAttribute("usuario", usuario);

            // Obtener la cuenta del usuario autenticado directamente
            Cuenta cuenta = usuario.getCuenta();
            if (cuenta != null) {
                session.setAttribute("cuenta", cuenta);
            } else {
                ratt.addFlashAttribute("error", "No se encontró ninguna cuenta asociada al usuario.");
                return "redirect:/cuenta/login";
            }

            ratt.addFlashAttribute("success", "Bienvenido, " + username + "!");
            return "redirect:/cuenta/menu";
        } else {
            ratt.addFlashAttribute("error", "Credenciales incorrectas, por favor inténtelo de nuevo.");
            return "redirect:/cuenta/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/cuenta/login";
    }
}

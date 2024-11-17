package cajeroweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cajeroweb.modelo.dao.ICuentaDao;
import cajeroweb.modelo.dao.IMovimientoDao;
import cajeroweb.modelo.entidades.Cuenta;
import cajeroweb.modelo.entidades.Movimiento;
import cajeroweb.modelo.entidades.Usuario;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {
    @Autowired
    private IMovimientoDao movimientoDao;

    @Autowired
    private ICuentaDao cuentaDao;

    @GetMapping("/listar")
    public String listarMovimientos(HttpSession session, RedirectAttributes ratt) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            ratt.addFlashAttribute("error", "Debe iniciar sesión para ver sus movimientos.");
            return "redirect:/cuenta/login";
        }

        // Obtener la cuenta del usuario
        Cuenta cuenta = usuario.getCuenta();
        if (cuenta == null) {
            ratt.addFlashAttribute("error", "No se encontró ninguna cuenta para el usuario.");
            return "redirect:/cuenta/login";
        }

        // Verifico que este la cuenta en la base de datos para evitar problemas
        cuenta = cuentaDao.buscarUno(cuenta.getIdCuenta());
        if (cuenta == null) {
            ratt.addFlashAttribute("error", "Cuenta no encontrada.");
            return "redirect:/cuenta/login";
        }

        // Podria hacerse asi tambien, pero prefiero hacer una consulta separada para
        // evitar posibles problemas de lazy loading
        // session.setAttribute("cuenta", cuenta);
        // session.setAttribute("movimientos", cuenta.getMovimientos());
        // return "movimientos";

        List<Movimiento> movimientos = movimientoDao.findByCuentaId(cuenta.getIdCuenta());
        session.setAttribute("cuenta", cuenta);
        session.setAttribute("movimientos", movimientos);
        return "movimientos";
    }

    @GetMapping("/ingresar")
    public String ingresarDinero() {
        return "formIngresar";
    }

    @PostMapping("/ingresar")
    public String ingresarDinero(@RequestParam double cantidad, HttpSession session, RedirectAttributes ratt) {
        Cuenta cuenta = (Cuenta) session.getAttribute("cuenta");
        if (cuenta == null) {
            ratt.addFlashAttribute("error", "No se encontró ninguna cuenta para el usuario.");
            return "redirect:/cuenta/login";
        } else if (cantidad <= 0) {
            ratt.addFlashAttribute("error", "La cantidad a ingresar debe ser mayor a 0.");
            return "redirect:/movimientos/ingresar";
        } else {
            if (movimientoDao.altaMovimiento(cuenta.getIdCuenta(), cantidad, "Ingreso")) {
                ratt.addFlashAttribute("info", "Ingreso realizado con éxito.");
                return "redirect:/movimientos/listar";
            } else {
                ratt.addFlashAttribute("error", "Error al ingresar dinero.");
            }
            return "redirect:/movimientos/ingresar";
        }
    }

    @GetMapping("/retirar")
    public String retirarDinero() {
        return "formRetirar";
    }

    @PostMapping("/retirar")
    public String retirarDinero(@RequestParam("cantidad") double cantidad, HttpSession session,
            RedirectAttributes ratt) {
        Cuenta cuenta = (Cuenta) session.getAttribute("cuenta");
        if (cuenta == null) {
            ratt.addFlashAttribute("error", "No se encontró ninguna cuenta para el usuario.");
            return "redirect:/cuenta/login";
        } else if (cantidad <= 0) {
            ratt.addFlashAttribute("error", "La cantidad a retirar debe ser mayor a 0.");
            return "redirect:/movimientos/retirar";
        } else {
            if (movimientoDao.altaMovimiento(cuenta.getIdCuenta(), cantidad, "Extraccion")) {
                ratt.addFlashAttribute("info", "Retiro realizado con éxito.");
                return "redirect:/movimientos/listar";
            } else {
                ratt.addFlashAttribute("error", "Error al realizar el retiro o saldo insuficiente.");
                return "redirect:/movimientos/retirar";
            }
        }
    }

    @GetMapping("/transferir")
    public String transferirDinero() {
        return "formTransferencia";
    }

    @PostMapping("/transferir")
    public String transferirDinero(@RequestParam int destinoCuenta ,@RequestParam double cantidad, HttpSession session, RedirectAttributes ratt) {

        Cuenta cuenta = (Cuenta) session.getAttribute("cuenta");

        if(cuenta == null){
            ratt.addFlashAttribute("error", "Inicie sesión para realizar movimientos");
            return "redirect:/cuenta/login";
        }

        if(cantidad <= 0){
            ratt.addFlashAttribute("error", "La cantidad a transferir debe ser mayor a 0");
            return "redirect:/movimientos/transferir";
        }

        if(cantidad > cuenta.getSaldo()){
            ratt.addFlashAttribute("error", "No tiene suficiente saldo para realizar la operación");
            return "redirect:/movimientos/transferir";
        }

        if(cuenta.getIdCuenta() == destinoCuenta){
            ratt.addFlashAttribute("error", "No puede transferir dinero a la misma cuenta");
            return "redirect:/movimientos/transferir";
        }

        if(movimientoDao.transferirDinero(cuenta.getIdCuenta(), destinoCuenta, cantidad)){
            cuenta = cuentaDao.buscarUno(cuenta.getIdCuenta());
            session.setAttribute("cuenta", cuenta);
            session.setAttribute("saldo", cuenta.getSaldo());
            session.setAttribute("movimientos", movimientoDao.findByCuentaId(cuenta.getIdCuenta()));
            ratt.addFlashAttribute("success", "Dinero transferido.");
            return "redirect:/movimientos/listar";
        } else {
            ratt.addFlashAttribute("error", "Error al transferir dinero.");
        }
        return "redirect:/movimientos/transferir";
    }

}

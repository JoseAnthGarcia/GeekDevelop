package com.example.demo.controller;

import com.example.demo.entities.Usuario;
import com.example.demo.repositories.MovilidadRepository;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    MovilidadRepository movilidadRepository;

    @GetMapping("/solicitudes")
    public String listaDeSolicitudes(Model model) {
        model.addAttribute("listaMovilidades", movilidadRepository.findAll());
        model.addAttribute("listaUsuariosSolicitudes", usuarioRepository.findByEstado("pendiente"));
        return "/AdminGen/solicitudes";
    }

    @GetMapping("/usuarios")
    public String listaDeUsuarios(Model model) {
        model.addAttribute("listaUsuarios", usuarioRepository.listaUsuariosAceptados());
        return "/AdminGen/lista";
    }

    @GetMapping("/detalle")
    public String detalleUsuario(@RequestParam("idUsuario") int idUsuario,
                                 Model model) {

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            switch (usuario.getRol()) {
                case "administrador":

                   // return "/AdminGen/visualizarCliente";
                case "repartidor":

                   // return "/AdminGen/visualizarCliente";
                case "cliente":
                    model.addAttribute("cliente",usuario);
                    return "/AdminGen/visualizarCliente";
                case "administradorRestaurante":

                   // return "/AdminGen/visualizarCliente";
                default:
                    //TODO ver si enviar con mensaje de alerta
                    return "redirect:/admin/usuarios";
            }
        }else {
            //TODO ver si enviar con mensaje de alerta
            return "redirect:/admin/usuarios";
        }
    }

}

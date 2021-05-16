package com.example.demo.controller;

import com.example.demo.entities.*;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/restaurante")
public class AdminRestController {
    @Autowired
    UsuarioRepository adminRestRepository;

    @Autowired
    RolRepository rolRepository;
    @Autowired
    DistritosRepository distritosRepository;

    @Autowired
    RestauranteRepository restauranteRepository;

    @Autowired
    CategoriasRestauranteRepository categoriasRestauranteRepository;

    //oe ya pes ><
    /*@GetMapping("/login")
    public String loginAdminRest() {
        return "AdminRestaurante/loginAR";
    }*/

    @GetMapping("/registro")
    public String nuevoAdminRest(@ModelAttribute("adminRest") Usuario adminRest, Model model) {
        model.addAttribute("adminRest", new Usuario());
        return "AdminRestaurante/registroAR";
    }


    @PostMapping("/guardarAdminR")
    public String guardarAdminRest(@ModelAttribute("adminRest") @Valid Usuario adminRest, BindingResult bindingResult,
                                   @RequestParam("confcontra") String contra2) {

        System.out.println(contra2);
        System.out.println(adminRest.getContrasenia());
        //se agrega rol:
        adminRest.setRol(rolRepository.findById(3).get());
        adminRest.setEstado(2);
        String fechanacimiento = LocalDate.now().toString();
        adminRest.setFecharegistro(fechanacimiento);
        if(bindingResult.hasErrors()||!contra2.equalsIgnoreCase(adminRest.getContrasenia())){
            return "/AdminRestaurante/registroAR";
        }else {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(adminRest.getContrasenia());
            adminRest.setContrasenia(hashedPassword);
            adminRestRepository.save(adminRest);
            return "redirect:/login";
        }
    }
    @PostMapping("/guardarRestaurante")
    public String guardarRestaurante(@ModelAttribute("restaurante") @Valid  Restaurante restaurante,
                                     BindingResult bindingResult, HttpSession session, Model model, @RequestParam("photo") MultipartFile file){
        String fileName ="";
        if (file!=null){
            if(file.isEmpty()){
                model.addAttribute("mensajefoto", "Debe subir una imagen");
                return "/AdminRestaurante/registroResturante";
            }
            fileName = file.getOriginalFilename();
            if (fileName.contains("..")){
                model.addAttribute("mensajefoto","No se premite '..' een el archivo");
                return "/AdminRestaurante/registroResturante";
            }
        }

        Usuario adminRest=(Usuario)session.getAttribute("usuario");
        restaurante.setAdministrador(adminRest);
        List<Categorias> listaCategorias =restaurante.getCategoriasRestaurante();
        if(bindingResult.hasErrors() || listaCategorias.size()!=4){
            model.addAttribute("listaDistritos", distritosRepository.findAll());
            model.addAttribute("listaCategorias", categoriasRestauranteRepository.findAll());
            if(listaCategorias.size()!=4){
                model.addAttribute("msg", "Se deben seleccionar 4 categorías");
            }
            return "/AdminRestaurante/registroResturante";
        }else {
            try{
                restaurante.setFoto(file.getBytes());
                restaurante.setFotonombre(fileName);
                restaurante.setFotocontenttype(file.getContentType());
                restauranteRepository.save(restaurante);
            } catch (IOException e){
                e.printStackTrace();
                model.addAttribute("mensajeFoto", "Ocurrió un error al subir el archivo");
                return "/AdminRestaurante/registroResturante";
            }

            return "redirect:/plato/";
        }
    }
    @GetMapping("/registroRest")
    public String registrarRestaurante(@ModelAttribute("restaurante") Restaurante restaurante,Model model){
        model.addAttribute("listaDistritos", distritosRepository.findAll());
        model.addAttribute("listaCategorias", categoriasRestauranteRepository.findAll());
        return "AdminRestaurante/registroResturante";
    }
    @GetMapping("/paginabienvenida")
    public String paginaBienvenida(){
        return "AdminRestaurante/adminCreado";
    }

    @GetMapping("/imagenRest/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") int id){
        Optional<Restaurante> optional=restauranteRepository.findById(id);
        if (optional.isPresent()){
            Restaurante p = optional.get();
            byte[] imagenBytes=p.getFoto();
            HttpHeaders httpHeaders= new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType(p.getFotocontenttype()));
            return new ResponseEntity<>(imagenBytes,httpHeaders, HttpStatus.OK);
        }else {
            return null;
        }
    }
}

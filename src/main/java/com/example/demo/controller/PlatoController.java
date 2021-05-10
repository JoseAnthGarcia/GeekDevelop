package com.example.demo.controller;

import com.example.demo.entities.CategoriaExtra;
import com.example.demo.entities.Plato;
import com.example.demo.repositories.CategoriaExtraRepository;
import com.example.demo.repositories.PlatoRepository;
import com.example.demo.service.PlatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;


import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/plato")
public class PlatoController {

    @Autowired
    PlatoRepository platoRepository;

    @Autowired
    PlatoService platoService;
    @Autowired
    CategoriaExtraRepository categoriaExtraRepository;

    @GetMapping("/lista")
    public String listaPlatos(Model model) {
        return findPaginated(1, model);
    }

    /*@PostMapping("/textSearch")
    public String buscador(@RequestParam("textBuscador") String textBuscador,
                           @RequestParam("textDisponible") Integer inputDisponible,
                           @RequestParam("textPrecio") Integer inputPrecio, Model model){
        model.addAttribute("texto", textBuscador);
        model.addAttribute("textoD", inputDisponible);
        model.addAttribute("textoP", inputPrecio);
        return findPaginated(textBuscador, inputDisponible, inputPrecio, 1, model);
    }*/

    @GetMapping("/page")
    public String findPaginated(@ModelAttribute @RequestParam(value = "textBuscador", required = false) String textBuscador,
                                @ModelAttribute @RequestParam(value = "textDisponible", required = false) Integer inputDisponible,
                                @ModelAttribute @RequestParam(value = "textPrecio", required = false) Integer inputPrecio,
                                @RequestParam(value = "pageNo", required = false) Integer pageNo, Model model){

        if(pageNo==null || pageNo==0){
            pageNo=1;
        }

        int inputID = 1;
        int pageSize = 5;
        Page<Plato> page;
        List<Plato> listaPlatos;
        System.out.println(textBuscador);
        if(textBuscador==null){
            textBuscador="";
        }
        if(inputDisponible==null){
            inputDisponible=1;
        }
        boolean disponibilidad;
        disponibilidad= inputDisponible != 0;
        System.out.println(inputPrecio);
        if(inputPrecio==null){
            inputPrecio=0;
        }
        int inputPMax;
        int inputPMin;
        if (inputPrecio==0){
            inputPMin=0;
            inputPMax=100;
        }else {
            inputPMax=inputPrecio;
            inputPMin=inputPrecio;
        }
        page = platoService.findPaginated2(pageNo, pageSize, textBuscador, disponibilidad, inputPMin*5-5, inputPMax*5);
        listaPlatos= page.getContent();


        model.addAttribute("texto", textBuscador);
        model.addAttribute("textoD", inputDisponible);
        model.addAttribute("textoP", inputPrecio);

        System.out.println(pageNo + "\n" + pageSize + "\n" + textBuscador + "\n" + disponibilidad + "\n" + inputPMin + "\n" +inputPMax);

        model.addAttribute("currentPage",pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("listaPlatos", listaPlatos);

        return "AdminRestaurante/listaPlatos";

    }

    @GetMapping("/nuevo")
    public String crearPlato(@ModelAttribute("plato") Plato plato,
                             Model model) {
        model.addAttribute("listaCategoria",categoriaExtraRepository.findAll());
        return "/AdminRestaurante/nuevoPlato";
    }

    @PostMapping("/guardar")
    public RedirectView guardarPlato(@ModelAttribute("plato") @Valid Plato plato,
                                     BindingResult bindingResult, RedirectAttributes attr, Model model, @RequestParam("foto") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println(fileName+ "FOTOOOOOOOOOOOOOOOO");
        plato.setFoto(fileName);
        int tamanio=0;
        ArrayList<Plato> lsita = (ArrayList<Plato>) platoRepository.findAll();
        for(int i =0; i<lsita.size(); i++){
            System.out.println(lsita.get(i).getIdplato());}
        tamanio=lsita.get(lsita.size()-1).getIdplato();
        //Plato savedUser = platoRepository.save(plato);
        String uploadDir = "user-photos/" + (tamanio+1);
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);




        plato.setIdrestaurante(1); //Jarcodeado
        plato.setIdcategoriaplato(2); //Jarcodeado
        plato.setDisponible(true); //default expresion !!!!
        platoRepository.save(plato);
        model.addAttribute("listaCategoria",categoriaExtraRepository.findAll());
        for(int i =0; i<plato.getCategoriaExtraList().size(); i++){
        System.out.println(plato.getCategoriaExtraList().get(i).getTipo());}

        /*if(bindingResult.hasErrors()){
            if (plato.getIdplato() == 0) {
                //return "/AdminRestaurante/nuevoPlato";
                return new RedirectView("/plato/nuevo", true);
            } else {
                Optional<Plato> optPlato = platoRepository.findById(plato.getIdplato());
                if (optPlato.isPresent()) {
                    //return "/AdminRestaurante/nuevoPlato";
                    return new RedirectView("/plato/nuevo", true);
                }else{

                    //return "redirect:/plato/lista";
                    return new RedirectView("/plato/lista", true);
                }
            }
        }else{
            plato.setIdrestaurante(1); //Jarcodeado
            plato.setIdcategoriaplato(3); //Jarcodeado
            plato.setDisponible(true); //default expresion !!!!

            if (plato.getIdplato() == 0) {

                attr.addFlashAttribute("msg", "Plato creado exitosamente");
                attr.addFlashAttribute("tipo", "saved");
                String uploadDir = "user-photos/" + platoRepository.save(plato).getIdplato();
                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
                platoRepository.save(plato);
            } else {
                Optional<Plato> optPlato = platoRepository.findById(plato.getIdplato());
                if (optPlato.isPresent()) {
                    platoRepository.save(plato);
                    attr.addFlashAttribute("tipo", "saved");
                    attr.addFlashAttribute("msg", "Plato actualizado exitosamente");
                }
            }
            //return "redirect:/plato/lista";
            return new RedirectView("/plato/lista", true);
        }*/
        return new RedirectView("/plato/lista", true);
    }



    @GetMapping("/editar")
    public String editarPlato(@RequestParam("id") int id,
                              Model model,
                              @ModelAttribute("plato") Plato plato) {
        Optional<Plato> platoOptional = platoRepository.findById(id);
        if (platoOptional.isPresent()) {
            plato = platoOptional.get();
            model.addAttribute("plato", plato);
            model.addAttribute("listaCategoria",categoriaExtraRepository.findAll());
            return "/AdminRestaurante/nuevoPlato";
        } else {
            return "redirect:/plato/lista";
        }
    }

    @GetMapping("/borrar")
    public String borrarPlato(@RequestParam("id") int id ,RedirectAttributes attr) {
        Optional<Plato> platoOptional = platoRepository.findById(id);
        if (platoOptional.isPresent()) {
            Plato plato = platoOptional.get();
            plato.setDisponible(false);
            platoRepository.save(plato);
            attr.addFlashAttribute("msg", "Plato borrado exitosamente");
            attr.addFlashAttribute("tipo", "borrado");
        }

        return "redirect:/plato/lista";
    }

    @GetMapping("/prueba")
    public String borrarPlato() {
        return "/AdminRestaurante/prueba";
    }

// IMAGEN
public static String directoriofoto= System.getProperty("user.dir")+"/src/main/resources/static/imagenDeRestaurante";

}

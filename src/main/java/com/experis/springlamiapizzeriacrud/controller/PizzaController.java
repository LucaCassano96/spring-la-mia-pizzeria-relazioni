package com.experis.springlamiapizzeriacrud.controller;

import com.experis.springlamiapizzeriacrud.model.Pizza;
import com.experis.springlamiapizzeriacrud.repository.PizzaRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    @Autowired
    private PizzaRepository PizzaRepository;


    @GetMapping
    public String index(@RequestParam Optional<String> search, Model model) {
        List<Pizza> pizzaList;
        if (search.isPresent()) {
            // se il parametro di ricerca è presente filtro la lista dei libri
            pizzaList = PizzaRepository.findByNameContainingIgnoreCase( search.get() );
        } else {
            // altrimenti prendo tutti i libri non filtrati
            // bookRepository recupera da database la lista di tutti i libri
            pizzaList = PizzaRepository.findAll();
        }

        model.addAttribute( "pizzaList", pizzaList );
        return "pizzas/pizzas-list";
    }

    //SHOW

    @GetMapping("/show/{id}")
    public String show(@PathVariable Integer id, Model model) {
        Optional<Pizza> result = PizzaRepository.findById( id );
        if (result.isPresent()) {
            model.addAttribute( "pizza", result.get() );
            return "pizzas/show";
        } else {
            throw new ResponseStatusException( HttpStatus.NOT_FOUND, "book with id " + id + " not found" );
        }

    }

    //CREATE
    @GetMapping("/create")
    public String create(Model model) {
        model.addAttribute( "pizza", new Pizza() );
        return "pizzas/create";
    }

    //STORE

    @PostMapping("/create/store")
    public String store(@Valid @ModelAttribute("pizza") Pizza formPizza, BindingResult BindingResult) {

        if (BindingResult.hasErrors()) {
            return "pizzas/create";
        }

        formPizza.setCreated_at( LocalDateTime.now() );
        PizzaRepository.save( formPizza );
        return "redirect:/pizzas";
    }
}

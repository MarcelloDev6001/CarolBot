package com.marc.discordbot.carol.website.backend.test;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CarolTestController {

    @GetMapping("/hello")
    public String hello() {
        return "Olá, mundo! 🚀 Meu backend Java com Gradle está rodando!";
    }

    @GetMapping("/saudacao/{nome}")
    public String saudacao(@PathVariable String nome) {
        return "Olá, " + nome + "! Bem-vindo ao meu backend.";
    }

    @PostMapping("/echo")
    public String echo(@RequestBody String body) {
        return "Você me enviou: " + body;
    }
}


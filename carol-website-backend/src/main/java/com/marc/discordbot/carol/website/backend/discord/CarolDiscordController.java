package com.marc.discordbot.carol.website.backend.discord;

import com.marc.discordbot.carol.website.backend.CarolBackendApplication;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CarolDiscordController {
    @PostMapping("/sendmessage")
    public ResponseEntity<String> sendmessage(@RequestBody String message) {
        if (message == null || message.trim().isEmpty()) {
            return ResponseEntity
                    .status(400)
                    .body("A mensagem não pode ser vazia");
        }

        JDA jda = CarolBackendApplication.jda;

        try {
            System.out.println(message);
            TextChannel testChannel = jda.getGuildById(1195352504553193562L).getTextChannelById(1195352506348359702L);

            if (testChannel == null) {
                return ResponseEntity
                        .status(404)
                        .body("Canal ou guild não encontrado");
            }

            testChannel.sendMessage(message).queue();
            return ResponseEntity.ok("Mensagem enviada com sucesso!");
        } catch (NullPointerException e) {
            return ResponseEntity
                    .status(500)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}

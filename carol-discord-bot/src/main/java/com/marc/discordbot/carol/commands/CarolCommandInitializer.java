package com.marc.discordbot.carol.commands;

import org.reflections.Reflections;
import java.util.Set;

public class CarolCommandInitializer {
    public static void initializeCommands() { // i admit, this was made by ChatGPT
        Reflections reflections = new Reflections("com.marc.discordbot.carol.commands");

        Set<Class<? extends CarolCommand>> classes = reflections.getSubTypesOf(CarolCommand.class);

        for (Class<? extends CarolCommand> clazz : classes) {
            try {
                clazz.getDeclaredConstructor().newInstance();
                System.out.println("Command initialized: " + clazz.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

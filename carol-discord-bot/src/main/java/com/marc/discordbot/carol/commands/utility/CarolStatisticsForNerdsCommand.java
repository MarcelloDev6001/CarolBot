package com.marc.discordbot.carol.commands.utility;

import com.marc.discordbot.carol.commands.CarolCommand;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;

import java.text.DecimalFormat;

public class CarolStatisticsForNerdsCommand extends CarolCommand {
    public CarolStatisticsForNerdsCommand() {
        super("estatisticas-para-nerds", "Ehm, na verdade esse comando está errado. O correto é \"estatÍsticas para Nerdolas\" \uD83E\uDD13☝");
    }

    private static final SystemInfo si = new SystemInfo();
    private static final CentralProcessor cpu = si.getHardware().getProcessor();
    private static final GlobalMemory memory = si.getHardware().getMemory();
    private static long[][] prevCpuTicks = cpu.getProcessorCpuLoadTicks(); // ticks anteriores

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction) {
        //CPU
        double[] loadPerCore = cpu.getProcessorCpuLoadBetweenTicks(prevCpuTicks);
        prevCpuTicks = cpu.getProcessorCpuLoadTicks();

        double totalCpu = 0;
        for (double load : loadPerCore) totalCpu += load;
        totalCpu = totalCpu / loadPerCore.length * 100; // Total memory of CPU

        // Memory
        long totalMem = memory.getTotal() / (1024 * 1024);
        long availableMem = memory.getAvailable() / (1024 * 1024);
        long usedMem = totalMem - availableMem;

        // JVM Memory
        Runtime runtime = Runtime.getRuntime();

        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        DecimalFormat ramFormat = new DecimalFormat("#.0");

        interaction.reply(
                "Estatísticas da Lori-\n" +
                        "Ehm, quero dizer... Carol :nerd: :point_up:\n\n" +
                        "Quantidade de Guilds carregadas: `" + interaction.getJDA().getGuilds().size() + "`.\n" +
                        "Quantidade de Membros carregados: `" + interaction.getJDA().getUsers().size() + "`.\n" +
                        "Uso da CPU total: " + decimalFormat.format(totalCpu) + "%.\n" +
                        "Memória RAM em uso: " + ramFormat.format((double) usedMem / totalMem * 100) + "% (" + usedMem + "MB/" + totalMem + "MB).\n" +
                        "Memória da JVM: \n- Máxima: " + runtime.maxMemory()/1048576 + "MB.\n" +
                        "- Total: " + runtime.totalMemory()/1048576 + "MB.\n" +
                        "- Livre: " + runtime.freeMemory()/1048576 + "MB."
        ).setEphemeral(true).queue();
    }
}

package com.marc.discordbot.carol.commands.utility;

import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.image.CarolImageUtils;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageEditAction;
import net.dv8tion.jda.api.utils.FileUpload;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.*;

public class CarolTextToPeriodicCommand extends CarolCommand {
    String[][] elements = {
            {"H", "Hydrogen"}, {"He", "Helium"},
            {"Li", "Lithium"}, {"Be", "Beryllium"}, {"B", "Boron"}, {"C", "Carbon"}, {"N", "Nitrogen"}, {"O", "Oxygen"}, {"F", "Fluorine"}, {"Ne", "Neon"},
            {"Na", "Sodium"}, {"Mg", "Magnesium"}, {"Al", "Aluminium"}, {"Si", "Silicon"}, {"P", "Phosphorus"}, {"S", "Sulfur"}, {"Cl", "Chlorine"}, {"Ar", "Argon"},
            {"K", "Potassium"}, {"Ca", "Calcium"}, {"Sc", "Scandium"}, {"Ti", "Titanium"}, {"V", "Vanadium"}, {"Cr", "Chromium"}, {"Mn", "Manganese"}, {"Fe", "Iron"}, {"Co", "Cobalt"}, {"Ni", "Nickel"}, {"Cu", "Copper"}, {"Zn", "Zinc"},
            {"Ga", "Gallium"}, {"Ge", "Germanium"}, {"As", "Arsenic"}, {"Se", "Selenium"}, {"Br", "Bromine"}, {"Kr", "Krypton"},
            {"Rb", "Rubidium"}, {"Sr", "Strontium"}, {"Y", "Yttrium"}, {"Zr", "Zirconium"}, {"Nb", "Niobium"}, {"Mo", "Molybdenum"}, {"Tc", "Technetium"}, {"Ru", "Ruthenium"}, {"Rh", "Rhodium"}, {"Pd", "Palladium"}, {"Ag", "Silver"}, {"Cd", "Cadmium"},
            {"In", "Indium"}, {"Sn", "Tin"}, {"Sb", "Antimony"}, {"Te", "Tellurium"}, {"I", "Iodine"}, {"Xe", "Xenon"},
            {"Cs", "Caesium"}, {"Ba", "Barium"}, {"La", "Lanthanum"}, {"Ce", "Cerium"}, {"Pr", "Praseodymium"}, {"Nd", "Neodymium"}, {"Pm", "Promethium"}, {"Sm", "Samarium"}, {"Eu", "Europium"}, {"Gd", "Gadolinium"},
            {"Tb", "Terbium"}, {"Dy", "Dysprosium"}, {"Ho", "Holmium"}, {"Er", "Erbium"}, {"Tm", "Thulium"}, {"Yb", "Ytterbium"}, {"Lu", "Lutetium"},
            {"Hf", "Hafnium"}, {"Ta", "Tantalum"}, {"W", "Tungsten"}, {"Re", "Rhenium"}, {"Os", "Osmium"}, {"Ir", "Iridium"}, {"Pt", "Platinum"}, {"Au", "Gold"}, {"Hg", "Mercury"},
            {"Tl", "Thallium"}, {"Pb", "Lead"}, {"Bi", "Bismuth"}, {"Po", "Polonium"}, {"At", "Astatine"}, {"Rn", "Radon"},
            {"Fr", "Francium"}, {"Ra", "Radium"}, {"Ac", "Actinium"}, {"Th", "Thorium"}, {"Pa", "Protactinium"}, {"U", "Uranium"}, {"Np", "Neptunium"}, {"Pu", "Plutonium"}, {"Am", "Americium"}, {"Cm", "Curium"},
            {"Bk", "Berkelium"}, {"Cf", "Californium"}, {"Es", "Einsteinium"}, {"Fm", "Fermium"}, {"Md", "Mendelevium"}, {"No", "Nobelium"}, {"Lr", "Lawrencium"},
            {"Rf", "Rutherfordium"}, {"Db", "Dubnium"}, {"Sg", "Seaborgium"}, {"Bh", "Bohrium"}, {"Hs", "Hassium"}, {"Mt", "Meitnerium"}, {"Ds", "Darmstadtium"}, {"Rg", "Roentgenium"}, {"Cn", "Copernicium"},
            {"Nh", "Nihonium"}, {"Fl", "Flerovium"}, {"Mc", "Moscovium"}, {"Lv", "Livermorium"}, {"Ts", "Tennessine"}, {"Og", "Oganesson"}
    };

    Set<String> elementsAbbreviations = new HashSet<>(Arrays.asList(
            "h","he","li","be","b","c","n","o","f","ne",
            "na","mg","al","si","p","s","cl","ar","k","ca",
            "sc","ti","v","cr","mn","fe","co","ni","cu","zn",
            "ga","ge","as","se","br","kr","rb","sr","y","zr",
            "nb","mo","tc","ru","rh","pd","ag","cd","in","sn",
            "sb","te","i","xe","cs","ba","la","ce","pr","nd",
            "pm","sm","eu","gd","tb","dy","ho","er","tm","yb",
            "lu","hf","ta","w","re","os","ir","pt","au","hg",
            "tl","pb","bi","po","at","rn","fr","ra","ac","th",
            "pa","u","np","pu","am","cm","bk","cf","es","fm",
            "md","no","lr","rf","db","sg","bh","hs","mt","ds",
            "rg","cn","fl","lv","ts","og"
    ));


    public String elementImageURL = "https://images-of-elements.com/{ELEMENT}.jpg";

    public CarolTextToPeriodicCommand() {
        super("texto-pra-elementos-periodicos", "Converta seu texto para elementos da tabela periódica e marque um amigo!");

        addOption(OptionType.STRING, "texto", "O texto a ser convertido", true, null);
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        String text = interaction.getOption("texto").getAsString();
        if (text.length() >= 10)
        {
            interaction.reply("O texto precisa ser menor (ou igual) a 10 caracteres!").setEphemeral(true).queue();
            return;
        }

        interaction.deferReply(false).queue();

        List<String> result = getStringAsPeriodicElements(text.toLowerCase());
        List<FileUpload> images = new ArrayList<>();
        List<String> elementsCompleteName = new ArrayList<>();

        if (result.isEmpty())
        {
            interaction.getHook().editOriginal("Um erro estranho ocorreu.").queue();
            return;
        }

        for (String element : result)
        {
            String elementName = "";
            for (String[] elementArray : elements)
            {
                if (elementArray[0].equalsIgnoreCase(element))
                {
                    elementName = elementArray[1];
                    break;
                }
            }
            BufferedImage elementImage = CarolImageUtils.getImageFromURL(elementImageURL.replace("{ELEMENT}", elementName.toLowerCase()).toLowerCase());
            if (elementImage != null)
            {
                try {
                    images.add(FileUpload.fromData(CarolImageUtils.toByteArray(elementImage), elementName + ".jpg"));
                    elementsCompleteName.add(elementName);
                } catch (Exception _) {}
            }
        }

        StringBuilder messageContent = new StringBuilder("Seu texto em elementos ficou: ");
        for (String elementCompleteName : elementsCompleteName)
        {
            messageContent.append(elementCompleteName).append(", ");
        }

        interaction.getHook().editOriginal(messageContent.toString()).setFiles(images).queue();
    }

    @NotNull
    private List<String> getStringAsPeriodicElements(String text) {
        List<String> result = new ArrayList<>();

        int i = 0;
        while (i < text.length()) {
            String two = (i + 1 < text.length()) ? text.substring(i, i + 2).toLowerCase() : "";
            if (!two.isEmpty() && elementsAbbreviations.contains(two)) {
                result.add(two);
                i += 2;
            } else {
                String one = text.substring(i, i + 1).toLowerCase();
                if (elementsAbbreviations.contains(one)) {
                    result.add(one);
                }
                i++;
            }
        }
        return result;
    }
}

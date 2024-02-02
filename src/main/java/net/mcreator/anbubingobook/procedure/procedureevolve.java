package net.mcreator.anbubingobook.procedure;

import net.mcreator.anbubingobook.ElementsAnbubingobookMod;
import net.minecraft.entity.Entity;

import java.util.Map;

@ElementsAnbubingobookMod.ModElement.Tag
public class procedureevolve extends ElementsAnbubingobookMod.ModElement {
    public procedureevolve(ElementsAnbubingobookMod instance) {
        super(instance, 57);
    }

    public static void executeProcedure(Map<String, Object> dependencies) {

        if (dependencies.get("entity") == null) {
            System.err.println("Failed to load entity");
            return;
        }

        Entity entity = (Entity) dependencies.get("entity");
        entity.onKillCommand();

    }
}
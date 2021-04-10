package net.mrscauthd.boss_tools.procedures;

import net.mrscauthd.boss_tools.BossToolsModElements;
import net.mrscauthd.boss_tools.BossToolsMod;

import net.minecraft.entity.Entity;

import java.util.Map;

@BossToolsModElements.ModElement.Tag
public class RocketTank21Procedure extends BossToolsModElements.ModElement {
	public RocketTank21Procedure(BossToolsModElements instance) {
		super(instance, 233);
	}

	public static boolean executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				BossToolsMod.LOGGER.warn("Failed to load dependency entity for procedure RocketTank21!");
			return false;
		}
		Entity entity = (Entity) dependencies.get("entity");
		return ((entity.getPersistentData().getDouble("fuel")) >= 357);
	}
}

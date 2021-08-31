
package net.mrscauthd.boss_tools.gui;

import net.minecraft.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.mrscauthd.boss_tools.entity.RocketTier1Entity;
import net.mrscauthd.boss_tools.gui.guihelper.GuiHelper;
import org.lwjgl.opengl.GL11;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.World;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.Minecraft;

import java.util.List;
import java.util.ArrayList;

import com.mojang.blaze3d.matrix.MatrixStack;

@OnlyIn(Dist.CLIENT)
public class RocketTier1GUIFuelGuiWindow extends ContainerScreen<RocketTier1GUIFuelGui.GuiContainerMod> {
	private World world;
	private int x, y, z;
	private PlayerEntity entity;
	public RocketTier1GUIFuelGuiWindow(RocketTier1GUIFuelGui.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.xSize = 176;
		this.ySize = 167;
	}

	private static final ResourceLocation texture = new ResourceLocation("boss_tools:textures/rocket_tier_1_gui_fuel.png");
	private static final ResourceLocation fuel_bar = new ResourceLocation("boss_tools:textures/rocket_fuel_bar.png");

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(ms, mouseX, mouseY);

		int fuel = 0;
		if(container.rocket != null) {
			fuel = container.rocket.getDataManager().get(RocketTier1Entity.FUEL) / 3;
		}

		List<ITextComponent> fuel2 = new ArrayList<ITextComponent>();

		if (mouseX > guiLeft + 65 && mouseX < guiLeft + 114 && mouseY > guiTop + 20 && mouseY < guiTop + 69) {
			if (fuel >= 1) {
				fuel2.add(ITextComponent.getTextComponentOrEmpty("\u00A79Fluid: \u00A77Fuel"));
			} else {
				fuel2.add(ITextComponent.getTextComponentOrEmpty("\u00A79Fluid: \u00A77Empty"));
			}
			fuel2.add(ITextComponent.getTextComponentOrEmpty(fuel + "%"));
			this.func_243308_b(ms, fuel2, mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float par1, int par2, int par3) {
		GL11.glColor4f(1, 1, 1, 1);
		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.blit(ms, k, l, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);

		// Tank
		int tank = 0;
		if(container.rocket != null) {
			tank = container.rocket.getDataManager().get(RocketTier1Entity.FUEL);
		}

		// Textures
		Minecraft.getInstance().getTextureManager().bindTexture(fuel_bar);
		this.blit(ms, this.guiLeft + 66, this.guiTop + 21, 0, 0, 48, 48, 48, 48);

		GuiHelper.drawFuel(ms, this.guiLeft + 66, this.guiTop + 21, (double) tank / 300);
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeScreen();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void tick() {
		super.tick();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		this.font.drawString(ms, "Tier 1 Rocket", 56, 5, -13421773);
		this.font.drawString(ms, "Inventory", 6, 72, -13421773);
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardListener.enableRepeatEvents(false);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);
		minecraft.keyboardListener.enableRepeatEvents(true);
	}

}

package net.mrscauthd.boss_tools.gui.screens.planetselection;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mrscauthd.boss_tools.BossToolsMod;
import net.mrscauthd.boss_tools.gui.helper.GuiHelper;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PlanetSelectionGuiWindow extends ContainerScreen<PlanetSelectionGui.GuiContainer> {

	private static final ResourceLocation texture = new ResourceLocation("boss_tools:textures/screens/planet_selection_gui.png");

	public float rotationMars = 0;
	public float rotationEarth = 90;
	public float rotationVenus = 180;
	public float rotationMercury = 270;

	public ResourceLocation overworldCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/red_button.png");

	public ResourceLocation marsCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/red_button.png");

	public PlanetSelectionGuiWindow(PlanetSelectionGui.GuiContainer container, PlayerInventory inventory, ITextComponent text) {
		super(container, inventory, text);
		this.xSize = 512;
		this.ySize = 512;
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(ms, mouseX, mouseY);

		String rocketType = container.rocket;
		String level = "c";
		List<ITextComponent> Categories = new ArrayList<ITextComponent>();

		rotationMars = (rotationMars + partialTicks * 0.6f) % 360;
		rotationEarth = (rotationEarth + partialTicks * 1.2f) % 360;
		rotationVenus = (rotationVenus + partialTicks * 1.1f) % 360;
		rotationMercury = (rotationMercury + partialTicks * 0.9f) % 360;

		//OVERWORLD
		if (checkRocket(rocketType, 1)) {
			level = "a";
			overworldCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/green_button_2.png");
		} else {
			level = "c";
			overworldCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/red_button_2.png");
		}

		if (GuiHelper.isHover(this.getBounds(10, (this.height / 2) - 60 / 2, 70, 20), mouseX, mouseY)) {

			//ToolTip
			Categories.add(ITextComponent.getTextComponentOrEmpty("\u00A79Category: " + "\u00A7" + level + "Overworld"));
			Categories.add(ITextComponent.getTextComponentOrEmpty("\u00A79Provided: \u00A7bTier 1 Rocket"));
			this.func_243308_b(ms, Categories, mouseX, mouseY);

		} else {
			if (checkRocket(rocketType, 1)) {
				overworldCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/green_button.png");
			} else {
				overworldCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/red_button.png");
			}
		}


		//MARS
		if (checkRocket(rocketType, 2)) {
			level = "a";
			marsCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/green_button_2.png");
		} else {
			level = "c";
			marsCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/red_button_2.png");
		}

		if (GuiHelper.isHover(this.getBounds(10, (this.height / 2) - 16 / 2, 70, 20), mouseX, mouseY)) {

			//ToolTip
			Categories.add(ITextComponent.getTextComponentOrEmpty("\u00A79Category: " + "\u00A7" + level + "Mars"));
			Categories.add(ITextComponent.getTextComponentOrEmpty("\u00A79Provided: \u00A7bTier 2 Rocket"));
			this.func_243308_b(ms, Categories, mouseX, mouseY);

		} else {
			if (checkRocket(rocketType, 2)) {
				marsCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/green_button.png");
			} else {
				marsCategoryButtonTex = new ResourceLocation("boss_tools:textures/buttons/red_button.png");
			}
		}

		//RENDER FONTS
		this.font.drawString(ms, "CATALOG", 21, (this.height / 2) - 126 / 2, -1);
		this.font.drawString(ms, "Overworld", 19, (this.height / 2) - 51 / 2, -1);
		this.font.drawString(ms, "Mars", 33, (this.height / 2) - 6 / 2, -1);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float par1, int par2, int par3) {
		RenderSystem.color4f(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();

		Minecraft.getInstance().getTextureManager().bindTexture(texture);
		this.blit(ms, 0, 0, 0, 0, this.width, this.height, this.width, this.height);

		Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("boss_tools:textures/rocket_menu_list.png"));
		this.blit(ms, 0, (this.height / 2) - 160 / 2, 0, 0, 160, 160, 160, 160);

		Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("boss_tools:textures/milky_way.png"));
		GuiHelper.blit(ms, (this.width - 185) / 2, (this.height - 185) / 2, 0, 0, 185, 185, 185, 185);

		//SUN
		Minecraft.getInstance().getTextureManager().bindTexture(new ResourceLocation("boss_tools:textures/sky/sun_no_light.png"));
		GuiHelper.blit(ms, (this.width - 15) / 2, (this.height - 15) / 2, 0, 0, 15, 15, 15, 15);

		//PLANETS
		this.addPlanet(ms, new ResourceLocation("boss_tools:textures/sky/mars.png"), -70, -70, 10, 10, rotationMars);
		this.addPlanet(ms, new ResourceLocation("boss_tools:textures/sky/earth.png"), -54, -54, 10, 10, rotationEarth);
		this.addPlanet(ms, new ResourceLocation("boss_tools:textures/sky/venus.png"), -37, -37, 10, 10, rotationVenus);
		this.addPlanet(ms, new ResourceLocation("boss_tools:textures/sky/mercury.png"), -20.5F, -20.5F, 10, 10, rotationMercury);

		//overworld button
		this.addButton(new ImageButton(10, (this.height / 2) - 60 / 2, 70, 20, 0, 0, 0, overworldCategoryButtonTex, 70, 20, (p_2130901) -> {
			if (checkRocket(container.rocket, 1)) {
				BossToolsMod.PACKET_HANDLER.sendToServer(new PlanetSelectionGui.NetworkMessage(playerInventory.player.getPosition(), 0));
			}
		}));

		//mars button
		this.addButton(new ImageButton(10, (this.height / 2) - 16 / 2, 70, 20, 0, 0, 0, marsCategoryButtonTex, 70, 20, (p_2130901) -> {
			if (checkRocket(container.rocket, 2)) {
				BossToolsMod.PACKET_HANDLER.sendToServer(new PlanetSelectionGui.NetworkMessage(playerInventory.player.getPosition(), 1));
			}
		}));

		RenderSystem.disableBlend();
	}

	public void addPlanet(MatrixStack ms, ResourceLocation planet, float x, float y, int width, int height, float rotation) {
		ms.push();

		ms.translate(this.width / 2, this.height / 2, 0);
		ms.rotate(new Quaternion(Vector3f.ZP, rotation, true));

		Minecraft.getInstance().getTextureManager().bindTexture(planet);
		GuiHelper.blit(ms, x, y, 0, 0, width, height, width, height);

		ms.translate(-this.width / 2, -this.height / 2, 0);
		ms.pop();
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
	}

	public Rectangle2d getBounds(int left, int top, int width, int height) {
		return GuiHelper.getBounds(left, top, width, height);
	}

	public static boolean checkRocket(String rocketType, int stage) {
		if (stage == 1 && rocketType.equals("entity.boss_tools.rocket_t1") || rocketType.equals("entity.boss_tools.rocket_t2") || rocketType.equals("entity.boss_tools.rocket_t3")) {
			return true;
		}
		if (stage == 2 && rocketType.equals("entity.boss_tools.rocket_t2") || rocketType.equals("entity.boss_tools.rocket_t3")) {
			return true;
		}
		if (stage == 3 && rocketType.equals("entity.boss_tools.rocket_t3")) {
			return true;
		}

		return false;
	}
}

package net.mrscauthd.boss_tools.gui;

import java.text.NumberFormat;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.mrscauthd.boss_tools.BossToolsMod;
import net.mrscauthd.boss_tools.gauge.GaugeDataHelper;
import net.mrscauthd.boss_tools.gauge.GaugeTextHelper;
import net.mrscauthd.boss_tools.gui.guihelper.GuiHelper;
import net.mrscauthd.boss_tools.machines.OxygenBubbleDistributorBlock;
import net.mrscauthd.boss_tools.machines.OxygenBubbleDistributorBlock.CustomTileEntity;

@OnlyIn(Dist.CLIENT)
public class OxygenBubbleDistributorGUIWindow extends ContainerScreen<OxygenBubbleDistributorGUI.GuiContainerMod> {
	public static final ResourceLocation texture = new ResourceLocation("boss_tools:textures/oxygen_bullet_generator_gui.png");
	public static final int OXYGEN_LEFT = 76;
	public static final int OXYGEN_TOP = 28;
	public static final int ENERGY_LEFT = 144;
	public static final int ENERGY_TOP = 21;

	private CustomTileEntity tileEntity;

	public OxygenBubbleDistributorGUIWindow(OxygenBubbleDistributorGUI.GuiContainerMod container, PlayerInventory inventory, ITextComponent text) {
		super(container, inventory, text);
		this.tileEntity = container.getTileEntity();
		this.xSize = 176;
		this.ySize = 167;
		this.playerInventoryTitleY = this.ySize - 92;
	}

	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderHoveredTooltip(ms, mouseX, mouseY);

		if (this.getEnergyBounds().contains(mouseX, mouseY)) {
			this.renderTooltip(ms, GaugeDataHelper.getEnergy(this.getTileEntity()).getText(), mouseX, mouseY);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(MatrixStack ms, float par1, int par2, int par3) {
		CustomTileEntity tileEntity = this.getTileEntity();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.minecraft.getTextureManager().bindTexture(texture);
		AbstractGui.blit(ms, this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);

		GuiHelper.drawEnergy(ms, this.guiLeft + ENERGY_LEFT, this.guiTop + ENERGY_TOP, tileEntity.getPrimaryEnergyStorage());
		GuiHelper.drawOxygen(ms, this.guiLeft + OXYGEN_LEFT, this.guiTop + OXYGEN_TOP, tileEntity.getOxygenPowerSystem().getStoredRatio());

		this.minecraft.getTextureManager().bindTexture(new ResourceLocation("boss_tools:textures/oxygenlarge6.png"));
		AbstractGui.blit(ms, this.guiLeft + 10, this.guiTop - 10, 0, 0, 21, 11, 21, 11);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(MatrixStack ms, int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(ms, mouseX, mouseY);
		
		double range = this.getTileEntity().getRange();
		NumberFormat numberInstance = NumberFormat.getNumberInstance();
		numberInstance.setMaximumFractionDigits(2);
		String rangeToString = numberInstance.format(range);

		ITextComponent oxygenText = GaugeTextHelper.getUsingText(GaugeDataHelper.getOxygen(this.getTileEntity().getOxygenPowerSystem().getPowerForOperation()));
		int oxygenWidth = this.font.getStringPropertyWidth(oxygenText);

		this.font.drawString(ms, String.format("%sx%s", rangeToString, rangeToString), 12, -8, 0x339900);
		this.font.func_243248_b(ms, oxygenText, this.xSize - oxygenWidth - 5, this.playerInventoryTitleY, 0x333333);
	}

	@Override
	public void init(Minecraft minecraft, int width, int height) {
		super.init(minecraft, width, height);

		this.addButton(new Button(this.guiLeft - 20, this.guiTop + 25, 20, 20, new StringTextComponent("-"), e -> {
			BlockPos pos = this.getTileEntity().getPos();
			BossToolsMod.PACKET_HANDLER.sendToServer(new OxygenBubbleDistributorBlock.SetLargeMessage(pos, false));
		}));
		this.addButton(new Button(this.guiLeft - 20, this.guiTop + 5, 20, 20, new StringTextComponent("+"), e -> {
			BlockPos pos = this.getTileEntity().getPos();
			BossToolsMod.PACKET_HANDLER.sendToServer(new OxygenBubbleDistributorBlock.SetLargeMessage(pos, true));
		}));
	}

	public CustomTileEntity getTileEntity() {
		return this.tileEntity;
	}

	public Rectangle2d getEnergyBounds() {
		return GuiHelper.getEnergyBounds(this.guiLeft + ENERGY_LEFT, this.guiTop + ENERGY_TOP);
	}

}
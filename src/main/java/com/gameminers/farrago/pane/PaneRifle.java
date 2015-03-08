package com.gameminers.farrago.pane;

import gminers.glasspane.GlassPane;
import gminers.glasspane.component.PaneImage;
import gminers.kitchensink.Rendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.GuiIngameForge;

import org.lwjgl.opengl.GL11;

import com.gameminers.farrago.FarragoMod;
import com.gameminers.farrago.RifleMode;

public class PaneRifle extends GlassPane {
	private static final ResourceLocation[] crosshairs = new ResourceLocation[26];
	static {
		for (int i = 0; i < crosshairs.length; i++) {
			crosshairs[i] = new ResourceLocation("farrago", "textures/crosshairs/rifle_crosshair_"+i+".png");
		}
	}
	@Override
	protected void doRender(int mouseX, int mouseY, float partialTicks) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.thePlayer != null) {
			if (mc.thePlayer.getHeldItem() != null) {
				ItemStack held = mc.thePlayer.getHeldItem();
				if (held.getItem() == FarragoMod.RIFLE) {
					GuiIngameForge.renderCrosshairs = false;
					int idx = 0;
					boolean overloadImminent = false;
					boolean ready = false;
					RifleMode mode = FarragoMod.RIFLE.getMode(held);
					if (mc.thePlayer.isUsingItem()) {
						float useTime = mc.thePlayer.getItemInUseDuration()+partialTicks;
						idx = (int)(((float)useTime)/((float)held.getItem().getMaxItemUseDuration(held))*25f);
						overloadImminent = (idx > 22);
						ready = (useTime >= FarragoMod.RIFLE.getTicksToFire(held)); 
					}
					if (!(overloadImminent || ready)) {
						GL11.glEnable(GL11.GL_BLEND);
				        OpenGlHelper.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
					}
					PaneImage.render(crosshairs[Math.min(25, idx)], (getWidth()/2)-8, (getHeight()/2)-8, 0, 0, 16, 16, 256, 256, overloadImminent ? 0xFFFF0000 : ready ? 0xFF00FF00 : -1, 1.0f, true);
					GL11.glPushMatrix();
			        	GL11.glScalef(0.5f, 0.5f, 1.0f);
			        	if (overloadImminent) {
			        		Rendering.drawCenteredString(mc.fontRenderer, "\u00A7lOVERLOAD IMMINENT", getWidth()-2, getHeight()-28, 0xFF0000);
			        	} else if (ready) {
			        		Rendering.drawCenteredString(mc.fontRenderer, "\u00A7lREADY", getWidth()-2, getHeight()-28, 0x00FF00);
			        	}
			        	Rendering.drawCenteredString(mc.fontRenderer, mode.getDisplayName(), getWidth()-2, getHeight()+20, 0xFFFFFF);
			        GL11.glPopMatrix();
					if (!(overloadImminent || ready)) {
						OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
						GL11.glDisable(GL11.GL_BLEND);
					}
					return;
				}
			}
		}
		GuiIngameForge.renderCrosshairs = true;
	}
}
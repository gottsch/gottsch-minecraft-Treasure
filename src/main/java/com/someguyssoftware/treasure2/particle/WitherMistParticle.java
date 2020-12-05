package com.someguyssoftware.treasure2.particle;

import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.network.WitherMistMessageToServer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class WitherMistParticle extends AbstractMistParticle {
	protected static final int MAX_AGE = 450;
	protected static final float ALPHA_VALUE = 0.5F;
	protected static final float MAX_SCALE_VALUE = 12;

	private static ResourceLocation mistParticlesSprites[] = new ResourceLocation[4];

	static {
		mistParticlesSprites[0] = new ResourceLocation(Treasure.MODID, "particle/wither_mist_particle");
		mistParticlesSprites[1] = new ResourceLocation(Treasure.MODID, "particle/wither_mist_particle2");
		mistParticlesSprites[2] = new ResourceLocation(Treasure.MODID, "particle/wither_mist_particle3");
		mistParticlesSprites[3] = new ResourceLocation(Treasure.MODID, "particle/wither_mist_particle4");
	}

	/**
	 * 
	 * @param world
	 * @param x
	 * @param y
	 * @param z
	 * @param velocityX
	 * @param velocityY
	 * @param velocityZ
	 * @param parentCoords
	 */
	public WitherMistParticle(World world, double x, double y, double z, double velocityX, double velocityY,
			double velocityZ, ICoords parentCoords) {
		super(world, x, y, z);
		this.setParentEmitterCoords(parentCoords);

		// randomly select a mist sprite
		Random random = new Random();
		int index = random.nextInt(4);
		TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks()
				.getAtlasSprite(mistParticlesSprites[index].toString());
		// set the particle texture
		setParticleTexture(sprite);
	}

	@Override
	public void inflictEffectOnPlayer(EntityPlayer player) {
		if (WorldInfo.isServerSide(Minecraft.getMinecraft().world)) {
			return;
		}
		PotionEffect potionEffect = player.getActivePotionEffect(MobEffects.WITHER);
		// if player does not have Wither effect, add it
		if (potionEffect == null) {
			WitherMistMessageToServer messageToServer = new WitherMistMessageToServer(player.getName());
			Treasure.simpleNetworkWrapper.sendToServer(messageToServer);
		}
	}

	@Override
	public int provideMaxAge() {
		return MAX_AGE;
	}

	@Override
	public float provideMaxScale() {
		return MAX_SCALE_VALUE;
	}

	@Override
	public float provideAlpha() {
		return ALPHA_VALUE;
	}
}
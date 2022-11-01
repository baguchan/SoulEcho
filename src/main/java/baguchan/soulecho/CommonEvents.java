package baguchan.soulecho;

import baguchan.soulecho.api.IEcho;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SoulEcho.MODID)
public class CommonEvents {
	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		ItemStack itemStack = event.getEntity().getItemInHand(event.getHand());
		if (itemStack.is(Items.ECHO_SHARD) && event.getLevel().getLevelData().isHardcore() && !((IEcho) event.getEntity()).hasSoulEcho()) {
			RandomSource source = event.getEntity().getRandom();
			itemStack.shrink(1);
			for(int i = 0; i < 6; i++) {
				event.getEntity().level.addParticle(ParticleTypes.SCULK_SOUL, event.getEntity().getX() + source.nextFloat() - 0.5D, event.getEntity().getY() + source.nextFloat() * 2.0F, event.getEntity().getZ() + source.nextFloat() - 0.5D, 0.0F, 0.0F, 0.0F);
			}
			event.getEntity().level.playSound(null, event.getPos(), SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 1.0F, 1.0F);
			((IEcho) event.getEntity()).setSoulEcho(true);

			event.setCancellationResult(InteractionResult.SUCCESS);
		}
	}
}

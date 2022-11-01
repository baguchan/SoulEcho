package baguchan.soulecho.mixin.client;

import baguchan.soulecho.SoulEcho;
import baguchan.soulecho.api.IEcho;
import baguchan.soulecho.message.CustomRespawnMessage;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.GenericDirtMessageScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DeathScreen.class)
public class DeathScreenMixin extends Screen {

	@Shadow
	@Final
	private List<Button> exitButtons;
	@Shadow
	@Final
	private boolean hardcore;

	protected DeathScreenMixin(Component p_96550_) {
		super(p_96550_);
	}

	@Inject(
			at = @At("TAIL"),
			method = "init"
	)
	protected void init(CallbackInfo callbackInfo) {
		this.exitButtons.removeIf((button -> {
			return true;
		}));
		this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 72, 200, 20, !((IEcho)this.minecraft.player).hasSoulEcho() && this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn"), (p_95930_) -> {
			if(!((IEcho)this.minecraft.player).hasSoulEcho()) {
				this.minecraft.player.respawn();
				this.minecraft.setScreen((Screen) null);
			}else {
				SoulEcho.CHANNEL.sendToServer(new CustomRespawnMessage(this.minecraft.player.getId()));
				this.minecraft.setScreen((Screen) null);
			}
		})));

		this.exitButtons.add(this.addRenderableWidget(new Button(this.width / 2 - 100, this.height / 4 + 96, 200, 20, Component.translatable("deathScreen.titleScreen"), (p_95925_) -> {
			if (this.hardcore) {
				confirmResult(true);
				this.exitToTitleScreen();
			} else {
				ConfirmScreen confirmscreen = new ConfirmScreen(this::confirmResult, Component.translatable("deathScreen.quit.confirm"), CommonComponents.EMPTY, Component.translatable("deathScreen.titleScreen"), Component.translatable("deathScreen.respawn"));
				this.minecraft.setScreen(confirmscreen);
				confirmscreen.setDelay(20);
			}
		})));
	}

	@Shadow
	private void confirmResult(boolean p_95932_) {
	}

	@Shadow
	private void exitToTitleScreen() {
	}
}

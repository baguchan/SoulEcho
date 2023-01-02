package baguchan.soulecho.mixin.client;

import baguchan.soulecho.SoulEcho;
import baguchan.soulecho.api.IEcho;
import baguchan.soulecho.message.CustomRespawnMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.client.gui.screens.Screen;
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

	@Shadow
	private Button exitToTitleButton;

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
		Component component = !((IEcho) this.minecraft.player).hasSoulEcho() && this.hardcore ? Component.translatable("deathScreen.spectate") : Component.translatable("deathScreen.respawn");
		this.exitButtons.add(this.addRenderableWidget(Button.builder(component, (p_95930_) -> {
			if (!((IEcho) this.minecraft.player).hasSoulEcho()) {
				this.minecraft.player.respawn();
				this.minecraft.setScreen((Screen) null);
			} else {
				SoulEcho.CHANNEL.sendToServer(new CustomRespawnMessage(this.minecraft.player.getId()));
				this.minecraft.setScreen((Screen) null);
			}
		}).bounds(this.width / 2 - 100, this.height / 4 + 72, 200, 20).build()));
		this.exitToTitleButton = this.addRenderableWidget(Button.builder(Component.translatable("deathScreen.titleScreen"), (p_262871_) -> {
			this.minecraft.getReportingContext().draftReportHandled(this.minecraft, this, this::handleExitToTitleScreen, true);
		}).bounds(this.width / 2 - 100, this.height / 4 + 96, 200, 20).build());
		this.exitButtons.add(this.exitToTitleButton);

		for (Button button : this.exitButtons) {
			button.active = false;
		}
	}

	@Shadow
	private void handleExitToTitleScreen() {

	}
}

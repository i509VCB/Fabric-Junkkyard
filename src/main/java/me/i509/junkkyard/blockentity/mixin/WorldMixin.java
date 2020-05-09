package me.i509.junkkyard.blockentity.mixin;

import me.i509.junkkyard.blockentity.api.ServerBlockEntityEvents;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(World.class)
public abstract class WorldMixin {
	@Shadow public abstract boolean isClient();

	@Inject(method = "addBlockEntity", at = @At("TAIL"))
	protected void onLoadBlockEntity(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir) {
		//System.out.println("LD");
		if (!this.isClient()) {
			ServerBlockEntityEvents.LOAD.invoker().onLoadBlockEntity(blockEntity, (ServerWorld) (Object) this);
		}
	}

	@Inject(method = "removeBlockEntity", at = @At(value = "INVOKE", target = "Ljava/util/List;remove(Ljava/lang/Object;)Z", ordinal = 1), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	protected void onUnloadBlockEntity(BlockPos pos, CallbackInfo ci, BlockEntity blockEntity) {
		//System.out.println("UL");
		if (!this.isClient()) {
			ServerBlockEntityEvents.UNLOAD.invoker().onUnloadBlockEntity(blockEntity, (ServerWorld) (Object) this);
		}
	}
}

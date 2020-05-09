package me.i509.junkkyard.blockentity.mixin.client;

import me.i509.junkkyard.blockentity.client.api.ClientBlockEntityEvents;
import me.i509.junkkyard.blockentity.mixin.WorldMixin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends WorldMixin {
	// We override our injection on the clientworld so only the client's block entity invocation will run
	@Override
	protected void onLoadBlockEntity(BlockEntity blockEntity, CallbackInfoReturnable<Boolean> cir) {
		//System.out.println("LDC");
		ClientBlockEntityEvents.LOAD.invoker().onLoadBlockEntity(blockEntity, (ClientWorld) (Object) this);
	}

	// We override our injection on the clientworld so only the client's block entity invocation will run
	@Override
	protected void onUnloadBlockEntity(BlockPos pos, CallbackInfo ci, BlockEntity blockEntity) {
		//System.out.println("ULC");
		ClientBlockEntityEvents.UNLOAD.invoker().onUnloadBlockEntity(blockEntity, (ClientWorld) (Object) this);
	}
}

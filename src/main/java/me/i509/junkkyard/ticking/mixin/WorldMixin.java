package me.i509.junkkyard.ticking.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.World;

@Mixin(World.class)
public abstract class WorldMixin {
	/*@Shadow
	public abstract Profiler getProfiler();

	@Inject(method = "tickEntity", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"))
	private void beforeTickEntity(Consumer<Entity> tickConsumer, Entity entity, CallbackInfo ci) {
		TickEventManager.ENTITY.beforeTick(this.getProfiler(), entity);
	}

	@Inject(method = "tickEntity", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V", shift = At.Shift.AFTER))
	private void afterEntityTick(Consumer<Entity> tickConsumer, Entity entity, CallbackInfo ci) {
		TickEventManager.ENTITY.afterTick(this.getProfiler(), entity);
	}

	@Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Tickable;tick()V"), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void beforeBlockEntityTick(CallbackInfo ci, Profiler profiler, Iterator<BlockEntity> iterator, BlockEntity blockEntity) {
		TickEventManager.BLOCK_ENTITY.beforeTick(profiler, blockEntity);
	}

	@Inject(method = "tickBlockEntities", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Tickable;tick()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	private void afterBlockEntityTick(CallbackInfo ci, Profiler profiler, Iterator<BlockEntity> iterator, BlockEntity blockEntity) {
		TickEventManager.BLOCK_ENTITY.afterTick(profiler, blockEntity);
	}*/
}

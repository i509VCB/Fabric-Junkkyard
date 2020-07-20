package me.i509.junkkyard.ticking.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.block.entity.BlockEntity;

import me.i509.junkkyard.ticking.api.TickHandler;
import me.i509.junkkyard.ticking.impl.TickManagerSubject;

@Mixin(BlockEntity.class)
public abstract class BlockEntityMixin implements TickManagerSubject<BlockEntity, TickHandler.BlockEntityHandler> {
	@Override
	public void refresh(Set<TickHandler.BlockEntityHandler> handlers) {

	}
}

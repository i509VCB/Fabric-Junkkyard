package me.i509.junkkyard.ticking.mixin;

import java.util.Set;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.entity.Entity;

import me.i509.junkkyard.ticking.api.TickHandler;
import me.i509.junkkyard.ticking.impl.TickManagerSubject;

@Mixin(Entity.class)
public abstract class EntityMixin implements TickManagerSubject<Entity, TickHandler.EntityHandler> {
	@Override
	public void refresh(Set<TickHandler.EntityHandler> handlers) {

	}
}

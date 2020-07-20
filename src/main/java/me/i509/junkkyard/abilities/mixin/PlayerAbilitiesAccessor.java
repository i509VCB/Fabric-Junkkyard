package me.i509.junkkyard.abilities.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.entity.player.PlayerAbilities;

@Mixin(PlayerAbilities.class)
public interface PlayerAbilitiesAccessor {
	@Accessor("flySpeed") float accessor$getFlySpeed();

	@Accessor("flySpeed") void accessor$setFlySpeed(float flySpeed);

	@Accessor("walkSpeed") float accessor$getWalkSpeed();

	@Accessor("walkSpeed") void accessor$setWalkSpeed(float walkSpeed);
}

package mcjty.intwheel.playerdata;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class PropertiesDispatcher implements ICapabilityProvider, INBTSerializable<CompoundTag> {

    private PlayerWheelConfiguration config;
    private LazyOptional<PlayerWheelConfiguration> playerWheelConfiguration = LazyOptional.of(this::create);

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == PlayerProperties.PLAYER_WHEEL_CONFIGURATION) {
            return playerWheelConfiguration.cast();
        }
        return LazyOptional.empty();
    }

    @Nonnull
    private PlayerWheelConfiguration create() {
        if (config == null) {
            config = new PlayerWheelConfiguration();
        }
        return config;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        create().loadNBTData(nbt);

    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        create().saveNBTData(tag);
        return tag;
    }
}

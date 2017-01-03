package mcjty.intwheel.playerdata;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

public class PropertiesDispatcher implements ICapabilityProvider, INBTSerializable<NBTTagCompound> {

    private PlayerWheelConfiguration playerWheelConfiguration = new PlayerWheelConfiguration();

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capability == PlayerProperties.PLAYER_WHEEL_CONFIGURATION;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == PlayerProperties.PLAYER_WHEEL_CONFIGURATION) {
            return (T) playerWheelConfiguration;
        }
        return null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        playerWheelConfiguration.saveNBTData(nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        playerWheelConfiguration.loadNBTData(nbt);
    }
}

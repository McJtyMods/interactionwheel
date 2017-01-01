package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActionProvider;
import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DefaultWheelActionProvider implements IWheelActionProvider {

    @Override
    @Nonnull
    public List<WheelActionElement> getActions(EntityPlayer player, World world, @Nullable BlockPos pos) {
        List<WheelActionElement> actions = new ArrayList<>();
        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
        if (ItemStackTools.isValid(heldItem)) {
            actions.add(StandardWheelActions.SEARCH.createWheelAction());
        }
        if (pos != null) {
            actions.add(StandardWheelActions.ROTATE.createWheelAction());
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof IWheelActions) {
                actions.addAll(((IWheelActions) block).getActions());
            } else {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof IInventory || (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))) {
                    actions.add(StandardWheelActions.DUMP.createWheelAction());
                    actions.add(StandardWheelActions.EXTRACT.createWheelAction());
                }
            }
        }
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
//        actions.add(new WheelActionElement(StandardWheelActions.ID_ROTATE, "rot", "intwheel:textures/gui/wheel_hilight.png", 128, 128));
        return actions;
    }
}

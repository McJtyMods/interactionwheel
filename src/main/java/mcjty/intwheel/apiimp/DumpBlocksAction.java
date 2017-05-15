package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import mcjty.intwheel.varia.InventoryHelper;
import mcjty.lib.tools.ItemStackTools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.Set;

public class DumpBlocksAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMPBLOCKS;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.DUMPBLOCKS.createElement();
    }

    @Override
    public boolean isDefaultEnabled() {
        return true;
    }

    private static Set<String> blocks = new HashSet<>();
    static {
        blocks.add("stone");
        blocks.add("stoneGranite");
        blocks.add("stoneGranitePolished");
        blocks.add("stoneDiorite");
        blocks.add("stoneDioritePolished");
        blocks.add("stoneAndesite");
        blocks.add("stoneAndesitePolished");
        blocks.add("dirt");
        blocks.add("grass");
        blocks.add("gravel");
        blocks.add("sand");
        blocks.add("cobblestone");
        blocks.add("sandstone");
        blocks.add("netherrack");
        blocks.add("endstone");
    }

    private boolean isBlock(ItemStack stack) {
        int[] ids = OreDictionary.getOreIDs(stack);
        for (int id : ids) {
            String oreName = OreDictionary.getOreName(id);
            if (oreName != null && blocks.contains(oreName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void performServer(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        TileEntity te = world.getTileEntity(pos);
        if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
            IItemHandler inventory = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (ItemStackTools.isValid(stack) && isBlock(stack)) {
                    stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                    player.inventory.setInventorySlotContents(i, stack);
                }
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (ItemStackTools.isValid(stack) && isBlock(stack)) {
                    int failed = InventoryHelper.mergeItemStackSafe(inventory, null, stack, 0, inventory.getSizeInventory(), null);
                    if (failed > 0) {
                        ItemStack putBack = stack.copy();
                        ItemStackTools.setStackSize(putBack, failed);
                        player.inventory.setInventorySlotContents(i, putBack);
                    } else {
                        player.inventory.setInventorySlotContents(i, ItemStackTools.getEmptyStack());
                    }
                }
            }
        }
    }
}

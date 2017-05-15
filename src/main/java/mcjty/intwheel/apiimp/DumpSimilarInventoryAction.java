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

public class DumpSimilarInventoryAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMPSIMILARINV;
    }

    @Override
    public boolean performClient(EntityPlayer player, World world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.DUMPSIMILARINV.createElement();
    }

    @Override
    public boolean isDefaultEnabled() {
        return true;
    }

    private boolean isSimilar(IItemHandler handler, ItemStack stack) {
        Set<Integer> ores = getOres(stack);
        for (int i = 0 ; i < handler.getSlots() ; i++) {
            ItemStack s = handler.getStackInSlot(i);
            if (matchStack(stack, ores, s)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSimilar(IInventory handler, ItemStack stack) {
        Set<Integer> ores = getOres(stack);
        for (int i = 0 ; i < handler.getSizeInventory() ; i++) {
            ItemStack s = handler.getStackInSlot(i);
            if (matchStack(stack, ores, s)) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> getOres(ItemStack stack) {
        Set<Integer> ores = new HashSet<>();
        for (int id : OreDictionary.getOreIDs(stack)) {
            ores.add(id);
        }
        return ores;
    }

    private boolean matchStack(ItemStack stack, Set<Integer> ores, ItemStack s) {
        if (ItemStackTools.isValid(s)) {
            if (s.getItem() == stack.getItem()) {
                return true;
            }
            if (!ores.isEmpty()) {
                int[] iDs = OreDictionary.getOreIDs(s);
                for (int d : iDs) {
                    if (ores.contains(d)) {
                        return true;
                    }
                }
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
                if (ItemStackTools.isValid(stack) && isSimilar(inventory, stack)) {
                    stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                    player.inventory.setInventorySlotContents(i, stack);
                }
            }
        } else if (te instanceof IInventory) {
            IInventory inventory = (IInventory) te;
            for (int i = 0 ; i < player.inventory.getSizeInventory() ; i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (ItemStackTools.isValid(stack) && isSimilar(inventory, stack)) {
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

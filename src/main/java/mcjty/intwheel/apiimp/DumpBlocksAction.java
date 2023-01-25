package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;
import java.util.Set;

public class DumpBlocksAction implements IWheelAction {

    @Override
    public String getId() {
        return StandardWheelActions.ID_DUMPBLOCKS;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
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
        // @todo 1.19.2 use tags!
//        int[] ids = OreDictionary.getOreIDs(stack);
//        for (int id : ids) {
//            String oreName = OreDictionary.getOreName(id);
//            if (oreName != null && blocks.contains(oreName)) {
//                return true;
//            }
//        }
        return false;
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te != null && te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).isPresent()) {
            te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).ifPresent(inventory -> {
                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);
                    if (!stack.isEmpty() && isBlock(stack)) {
                        stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                        player.getInventory().setItem(i, stack);
                    }
                }
            });
        }
    }
}

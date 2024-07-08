package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;

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

    private static final Set<TagKey<Item>> TAGS = new HashSet<>();

    private static Set<TagKey<Item>> getTags() {
        if (TAGS.isEmpty()) {
            TAGS.add(Tags.Items.STONES);
            TAGS.add(Tags.Items.SANDSTONE_BLOCKS);
            TAGS.add(Tags.Items.END_STONES);
            TAGS.add(Tags.Items.NETHERRACKS);
            TAGS.add(Tags.Items.COBBLESTONES);
            TAGS.add(Tags.Items.GRAVELS);
            TAGS.add(ItemTags.SAND);
            TAGS.add(ItemTags.DIRT);
        }
        return TAGS;
    }

    private boolean isBlock(ItemStack stack) {
        for (TagKey<Item> tag : getTags()) {
            if (stack.is(tag)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
        IItemHandler inventory = world.getCapability(Capabilities.ItemHandler.BLOCK, pos, null);
        if (inventory != null) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (!stack.isEmpty() && isBlock(stack)) {
                    stack = ItemHandlerHelper.insertItem(inventory, stack, false);
                    player.getInventory().setItem(i, stack);
                }
            }
        }
    }
}

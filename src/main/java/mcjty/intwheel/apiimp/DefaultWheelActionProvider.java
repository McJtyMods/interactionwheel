package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelActionProvider;
import mcjty.intwheel.api.IWheelBlockSupport;
import mcjty.intwheel.api.StandardWheelActions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

public class DefaultWheelActionProvider implements IWheelActionProvider {

    @Override
    public String getID() {
        return "interactionwheel:default";
    }

    @Override
    public void updateWheelActions(@Nonnull Set<String> actions, @Nonnull Player player, Level world, @Nullable BlockPos pos) {
        ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!heldItem.isEmpty()) {
            actions.add(StandardWheelActions.ID_SEARCH);
        }
        if (pos != null) {
            actions.add(StandardWheelActions.ID_ROTATE);
            Block block = world.getBlockState(pos).getBlock();
            BlockEntity te = world.getBlockEntity(pos);
            if (te != null && te.getCapability(ForgeCapabilities.ITEM_HANDLER, null).isPresent()) {
                actions.add(StandardWheelActions.ID_DUMP);
                actions.add(StandardWheelActions.ID_EXTRACT);
                actions.add(StandardWheelActions.ID_DUMPORES);
                actions.add(StandardWheelActions.ID_DUMPBLOCKS);
                actions.add(StandardWheelActions.ID_DUMPSIMILARINV);
                if (!heldItem.isEmpty()) {
                    actions.add(StandardWheelActions.ID_DUMP1);
                    actions.add(StandardWheelActions.ID_DUMPSIMILAR);
                }
            }
            actions.add(StandardWheelActions.ID_PICKTOOL);

            if (block instanceof IWheelBlockSupport) {
                ((IWheelBlockSupport) block).updateWheelActions(actions);
            }
        }
//        actions.add("std.dummy0");
//        actions.add("std.dummy1");
//        actions.add("std.dummy2");
//        actions.add("std.dummy3");
//        actions.add("std.dummy4");
//        actions.add("std.dummy5");
//        actions.add("std.dummy7");
//        actions.add("std.dummy8");
//        actions.add("std.dummy9");
//        actions.add("std.dummy10");
    }
}

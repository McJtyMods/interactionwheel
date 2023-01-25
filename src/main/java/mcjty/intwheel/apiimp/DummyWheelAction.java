package mcjty.intwheel.apiimp;

import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.StandardWheelActions;
import mcjty.intwheel.api.WheelActionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DummyWheelAction implements IWheelAction {

    private final String id;

    public DummyWheelAction(String id) {
        this.id = id;
    }

    @Override
    public boolean isDefaultEnabled() {
        return false;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean performClient(Player player, Level world, BlockPos pos, boolean extended) {
        return true;
    }

    @Override
    public WheelActionElement createElement() {
        return StandardWheelActions.GENERIC.createElement().description(id, null);
    }

    @Override
    public void performServer(Player player, Level world, BlockPos pos, boolean extended) {
    }
}

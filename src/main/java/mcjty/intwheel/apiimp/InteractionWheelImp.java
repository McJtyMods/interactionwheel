package mcjty.intwheel.apiimp;

import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IInteractionWheel;
import mcjty.intwheel.api.IWheelAction;
import mcjty.intwheel.api.IWheelActionProvider;
import mcjty.intwheel.api.IWheelActionRegistry;
import mcjty.intwheel.playerdata.PlayerProperties;
import mcjty.intwheel.playerdata.PlayerWheelConfiguration;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class InteractionWheelImp implements IInteractionWheel {

    private List<IWheelActionProvider> providers = new ArrayList<>();

    public InteractionWheelImp() {
    }

    private int findProvider(String id) {
        for (int i = 0 ; i < providers.size() ; i++) {
            if (id.equals(providers.get(i).getID())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void registerProvider(IWheelActionProvider provider) {
        int idx = findProvider(provider.getID());
        if (idx != -1) {
            providers.set(idx, provider);
        } else {
            providers.add(provider);
        }
    }

    public List<IWheelActionProvider> getProviders() {
        return providers;
    }

    private IWheelActionProvider getProviderByID(String id) {
        for (IWheelActionProvider provider : providers) {
            if (provider.getID().equals(id)) {
                return provider;
            }
        }
        return null;
    }

    @Override
    public IWheelActionRegistry getRegistry() {
        return InteractionWheel.registry;
    }

    @Nonnull
    public List<String> getActions(@Nonnull EntityPlayer player, World world, @Nullable BlockPos pos) {
        List<String> actions = new ArrayList<>();
        for (IWheelActionProvider provider : providers) {
            provider.updateWheelActions(actions, player, world, pos);
        }
        // Only keep enabled actions
        PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(player);
        List<String> newactions = new ArrayList<>();
        for (String id : actions) {
            Boolean enabled = config.isEnabled(id);
            if (enabled == null) {
                // Don't know yet, use default
                IWheelAction action = InteractionWheel.registry.get(id);
                if (action == null) {
                    enabled = Boolean.FALSE;
                } else {
                    enabled = action.isDefaultEnabled();
                }
            }
            if (enabled) {
                newactions.add(id);
            }
        }
        return newactions;
    }
}

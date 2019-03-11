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
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // Get all sorted actions. Disabled and enabled
    public List<String> getSortedActions(@Nonnull EntityPlayer player) {
        PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(player);
        List<String> orderedActions = new ArrayList<>(config.getOrderedActions());

        // Add all ids that are missing
        for (String id : InteractionWheel.registry.getRegistrationOrder()) {
            if (!orderedActions.contains(id)) {
                orderedActions.add(id);
            }
        }


        // Clean up all invalid actions
        List<String> valid = new ArrayList<>();
        for (String action : orderedActions) {
            if (InteractionWheel.registry.get(action) != null) {
                valid.add(action);
            }
        }

        return valid;
    }


    @Nonnull
    public List<String> getActions(@Nonnull EntityPlayer player, World world, @Nullable BlockPos pos) {
        Set<String> actions = new HashSet<>();
        for (IWheelActionProvider provider : providers) {
            try {
                provider.updateWheelActions(actions, player, world, pos);
            } catch (Exception e) {
                InteractionWheel.setup.getLogger().log(Level.ERROR, "The provider " + provider.getID() + " caused a crash!", e);
            }
        }
        // Only keep enabled actions
        PlayerWheelConfiguration config = PlayerProperties.getWheelConfig(player);
        List<String> newactions = new ArrayList<>();
        for (String id : getSortedActions(player)) {
            if (actions.contains(id)) {
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
        }

        return newactions;
    }
}

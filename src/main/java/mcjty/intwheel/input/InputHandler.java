package mcjty.intwheel.input;


import mcjty.intwheel.InteractionWheel;
import mcjty.intwheel.api.IWheelActions;
import mcjty.intwheel.apiimp.InventoryWheelActions;
import mcjty.intwheel.apiimp.ItemHandlerWheelActions;
import mcjty.lib.tools.MinecraftTools;
import mcjty.theoneprobe.proxy.GuiProxy;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.items.CapabilityItemHandler;

public class InputHandler {

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (KeyBindings.keyOpenWheel.isPressed()) {
            EntityPlayerSP player = MinecraftTools.getPlayer(Minecraft.getMinecraft());
            RayTraceResult mouseOver = Minecraft.getMinecraft().objectMouseOver;
            if (mouseOver != null && mouseOver.typeOfHit == RayTraceResult.Type.BLOCK) {
                BlockPos pos = mouseOver.getBlockPos();
                WorldClient world = MinecraftTools.getWorld(Minecraft.getMinecraft());
                IWheelActions actions = getWheelActions(world, pos);
                if (actions != null) {
                    player.openGui(InteractionWheel.instance, GuiProxy.GUI_NOTE, player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
                }
            }
        }
    }

    public static IWheelActions getWheelActions(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        IWheelActions actions = null;
        if (block instanceof IWheelActions) {
            actions = (IWheelActions) block;
        } else {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IInventory) {
                actions = new InventoryWheelActions((IInventory) te);
            } else if (te != null && te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                actions = new ItemHandlerWheelActions(te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            }
        }
        return actions;
    }
}

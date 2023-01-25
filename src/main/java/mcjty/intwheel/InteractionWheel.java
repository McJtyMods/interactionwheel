package mcjty.intwheel;


import mcjty.intwheel.apiimp.InteractionWheelImp;
import mcjty.intwheel.apiimp.WheelActionRegistry;
import mcjty.intwheel.setup.ModSetup;
import net.minecraftforge.fml.common.Mod;

@Mod(InteractionWheel.MODID)
public class InteractionWheel {

    public static final String MODID = "interactionwheel";

    public static ModSetup setup = new ModSetup();

    public static InteractionWheel instance;
    public static InteractionWheelImp interactionWheelImp = new InteractionWheelImp();

    public static WheelActionRegistry registry = new WheelActionRegistry();

    public InteractionWheel() {
    }

    //    @Mod.EventHandler
//    public void imcCallback(FMLInterModComms.IMCEvent event) {
//        for (FMLInterModComms.IMCMessage message : event.getMessages()) {
//            if (message.key.equalsIgnoreCase("getTheWheel")) {
//                Optional<Function<IInteractionWheel, Void>> value = message.getFunctionValue(IInteractionWheel.class, Void.class);
//                if (value.isPresent()) {
//                    value.get().apply(interactionWheelImp);
//                } else {
//                    setup.getLogger().warn("Some mod didn't return a valid result with getTheWheel!");
//                }
//            }
//        }
//    }

}

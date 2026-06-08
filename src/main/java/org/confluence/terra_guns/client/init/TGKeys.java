package org.confluence.terra_guns.client.init;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

import java.util.function.Supplier;

public class TGKeys {
    public static final Supplier<KeyMapping> SHOOT = Suppliers.memoize(() -> new KeyMapping(
            "key.terra_guns.shoot",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_LEFT,
            "key.categories.gameplay"
    ));

    public static final Supplier<KeyMapping> AIM = Suppliers.memoize(() -> new KeyMapping(
            "key.terra_guns.aim",
            KeyConflictContext.IN_GAME,
            InputConstants.Type.MOUSE,
            GLFW.GLFW_MOUSE_BUTTON_RIGHT,
            "key.categories.gameplay"
    ));
}

package com.csdy.vampirismtinker.modifier;

import de.teamlapen.vampirism.entity.player.hunter.HunterPlayer;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import static com.csdy.vampirismtinker.modifier.method.ModifierUtil.vampirePower;

public class VampireBaseModifer extends Modifier implements MeleeDamageModifierHook {
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float baseDamage, float damage) {
        Player player = context.getPlayerAttacker();
        if (player == null) return 0;
        boolean message = !player.getCommandSenderWorld().isClientSide;
        VampirePlayer vampire = VampirePlayer.get(player);
        if (vampire.getLevel() < 1) {
            if (message) player.displayClientMessage(Component.translatable("text.vampirism.can_not_be_used_faction"), true);
            return 0;
        }
        return damage;
    }

    protected int vampirePlayerLevel(Player player){
        VampirePlayer vampire = VampirePlayer.get(player);
        return vampire.getLevel();
    }


    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE);
    }
}

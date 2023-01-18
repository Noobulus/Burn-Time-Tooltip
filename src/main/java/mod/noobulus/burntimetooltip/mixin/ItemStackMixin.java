package mod.noobulus.burntimetooltip.mixin;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static net.minecraft.block.entity.AbstractFurnaceBlockEntity.createFuelTimeMap;

@Mixin(value = ItemStack.class, priority = 42069)
public class ItemStackMixin {

    private final int DIVISOR_TICKS = 1;
    private final int DIVISOR_SECONDS = 20;
    private final int DIVISOR_ITEMS = 200;

    @Inject(method = "getTooltip", at = @At(value = "RETURN"))
    private void onGetTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) {
        List<Text> tooltip = cir.getReturnValue();
        float burnTime = getBurnTime((ItemStack) (Object) this, DIVISOR_ITEMS);
        if (burnTime > 0) {
            tooltip.add(Text.translatable("tooltip.burntimetooltip.burntime",
                    Text.literal(formatNumber(burnTime))).setStyle(Style.EMPTY.withColor(Formatting.GRAY)));
        }
    }

    private float getBurnTime(ItemStack fuel, int divisor) {
        Item item = fuel.getItem();
        return (float)createFuelTimeMap().getOrDefault(item, 0) / divisor;
    }

    private String formatNumber (float num) {
        if (num % 1 == 0) {
            return String.valueOf(((int)num));
        }
        return String.valueOf(num);
    }
}

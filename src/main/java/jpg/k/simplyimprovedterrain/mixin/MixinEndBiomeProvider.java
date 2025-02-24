package jpg.k.simplyimprovedterrain.mixin;

import jpg.k.simplyimprovedterrain.mixinapi.ISimplexNoiseGenerator;
import jpg.k.simplyimprovedterrain.util.noise.MetaballEndIslandNoise;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.EndBiomeProvider;
import net.minecraft.world.gen.SimplexNoiseGenerator;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EndBiomeProvider.class, priority = 500)
public class MixinEndBiomeProvider {

    @Shadow
    private @Final Biome field_242641_i;

    @Shadow
    private @Final Biome field_242642_j;

    @Shadow
    private @Final Biome field_242643_k;

    @Shadow
    private @Final Biome field_242644_l;

    @Shadow
    private @Final Biome field_242645_m;

    @Shadow
    private @Final SimplexNoiseGenerator generator;
    
    // Must be Overwrite for other mods (e.g. Abnormals Core) to mix into its return statements instead of only Vanilla's.
    @Overwrite
    public Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
        if ((long)biomeX * (long)biomeX + (long)biomeZ * (long)biomeZ <= 0x10000L) {
            return this.field_242641_i;
        } else {
            double f = MetaballEndIslandNoise.INSTANCE.getNoise(((ISimplexNoiseGenerator)(Object)generator).getPermTable(), biomeX * 4 + 2, biomeZ * 4 + 2);
            return f > 40.0F?this.field_242642_j:(f >= 0.0F?this.field_242643_k:(f < -20.0F?this.field_242644_l:this.field_242645_m));
        }
    }

    @Inject(method = "func_235317_a_", at = @At("HEAD"), cancellable = true)
    private static void inject_func_235317_a_(SimplexNoiseGenerator simplex, int biomeX, int biomeZ, CallbackInfoReturnable<Float> cir) {
        float f = (float)MetaballEndIslandNoise.INSTANCE.getNoise(((ISimplexNoiseGenerator)(Object)simplex).getPermTable(), biomeX * 8, biomeZ * 8);
        cir.setReturnValue(f);
        cir.cancel();
    }

}

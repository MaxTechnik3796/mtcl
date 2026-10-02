package cz.maxtechnik.mtcl.mixin;

import net.neoforged.fml.ModList;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;
public class MtclModMixinPlugin implements IMixinConfigPlugin{
	@Override
	public boolean shouldApplyMixin(String targetClassName,String mixinClassName){
		if(mixinClassName.endsWith("NetheriteBacktankMixin")) return ModList.get()!=null&&ModList.get().isLoaded("create");
		if(mixinClassName.endsWith("SemaphoreMixin")) return ModList.get()!=null&&ModList.get().isLoaded("railways");
		return true;
	}
	@Override
	public void onLoad(String mixinPackage){
	}
	@Override
	public String getRefMapperConfig(){
		return null;
	}
	@Override
	public void acceptTargets(Set<String> myTargets,Set<String> otherTargets){
	}
	@Override
	public List<String> getMixins(){
		return null;
	}
	@Override
	public void preApply(String targetClassName,ClassNode targetClass,String mixinClassName,IMixinInfo mixinInfo){
	}
	@Override
	public void postApply(String targetClassName,ClassNode targetClass,String mixinClassName,IMixinInfo mixinInfo){
	}
}
package dev.ftb.mods.ftbstructures.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author LatvianModder
 */
public abstract class FluidIngredient implements Predicate<FluidStack> {
	public static class FromID extends FluidIngredient {
		public static final int ID = 1;

		public final Fluid fluid;

		public FromID(Fluid f) {
			fluid = f;
		}

		@Override
		public boolean test(FluidStack fluidStack) {
			return fluid.isSame(fluidStack.getFluid());
		}

		@Override
		public void write(FriendlyByteBuf buffer) {
			buffer.writeByte(ID);
			buffer.writeResourceLocation(fluid.getRegistryName());
		}

		@Override
		public Collection<FluidStack> getMatchingStacks() {
			return Collections.singleton(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME));
		}
	}

	public static class FromTag extends FluidIngredient {
		public static final int ID = 2;

		public final ResourceLocation id;
		public final Tag<Fluid> tag;

		public FromTag(ResourceLocation i, Tag<Fluid> t) {
			id = i;
			tag = t;
		}

		@Override
		public boolean test(FluidStack fluidStack) {
			return tag.contains(fluidStack.getFluid());
		}

		@Override
		public void write(FriendlyByteBuf buffer) {
			buffer.writeByte(ID);
			buffer.writeResourceLocation(id);
		}

		@Override
		public Collection<FluidStack> getMatchingStacks() {
			List<FluidStack> list = new ArrayList<>();

			for (Fluid fluid : tag.getValues()) {
				list.add(new FluidStack(fluid, FluidAttributes.BUCKET_VOLUME));
			}

			return list;
		}
	}

	public static class FromList extends FluidIngredient {
		public static final int ID = 3;

		public final List<FluidIngredient> list = new ArrayList<>();

		@Override
		public boolean test(FluidStack fluidStack) {
			for (FluidIngredient ingredient : list) {
				if (ingredient.test(fluidStack)) {
					return true;
				}
			}

			return false;
		}

		@Override
		public void write(FriendlyByteBuf buffer) {
			buffer.writeByte(ID);
			buffer.writeVarInt(list.size());

			for (FluidIngredient ingredient : list) {
				ingredient.write(buffer);
			}
		}

		@Override
		public Collection<FluidStack> getMatchingStacks() {
			List<FluidStack> l = new ArrayList<>();

			for (FluidIngredient ingredient : list) {
				l.addAll(ingredient.getMatchingStacks());
			}

			return l;
		}
	}

	public static FluidIngredient deserialize(@Nullable JsonElement json) {
		if (json instanceof JsonArray) {
			FromList list = new FromList();

			for (JsonElement e : (JsonArray) json) {
				list.list.add(deserialize(e));
			}

			return list;
		} else if (json instanceof JsonObject) {
			JsonObject o = (JsonObject) json;

			if (o.has("tag")) {
				ResourceLocation id = new ResourceLocation(o.get("tag").getAsString());
				Tag<Fluid> tag = FluidTags.func_226157_a_().getTag(id);

				if (tag != null) {
					return new FromTag(id, tag);
				}
			} else if (o.has("fluid")) {
				Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(o.get("fluid").getAsString()));

				if (fluid != null && fluid != Fluids.EMPTY) {
					return new FromID(fluid);
				}
			}
		} else if (json instanceof JsonPrimitive) {
			Fluid fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(json.getAsString()));

			if (fluid != null && fluid != Fluids.EMPTY) {
				return new FromID(fluid);
			}
		}

		return new FromList();
	}

	public static FluidIngredient read(FriendlyByteBuf buffer) {
		switch (buffer.readByte()) {
			case FromID.ID: {
				Fluid fluid = ForgeRegistries.FLUIDS.getValue(buffer.readResourceLocation());

				if (fluid != null && fluid != Fluids.EMPTY) {
					return new FromID(fluid);
				}
			}
			case FromTag.ID: {
				ResourceLocation id = buffer.readResourceLocation();
				Tag<Fluid> tag = FluidTags.func_226157_a_().getTag(id);

				if (tag != null) {
					return new FromTag(id, tag);
				}
			}
			case FromList.ID: {
				int s = buffer.readVarInt();
				FromList list = new FromList();

				for (int i = 0; i < s; i++) {
					list.list.add(read(buffer));
				}

				return list;
			}
		}

		return new FromList();
	}

	public abstract void write(FriendlyByteBuf buffer);

	public abstract Collection<FluidStack> getMatchingStacks();
}
package mhfc.net.client.gui;

import static org.lwjgl.opengl.GL11.GL_QUADS;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Vector2f;

import org.lwjgl.opengl.GL11;

import mhfc.net.client.container.ContainerHunterBench;
import mhfc.net.client.gui.GuiListItem.Alignment;
import mhfc.net.client.quests.MHFCRegQuestVisual;
import mhfc.net.client.util.gui.MHFCGuiUtil;
import mhfc.net.common.core.registry.MHFCEquipementRecipeRegistry;
import mhfc.net.common.crafting.equipment.EquipmentRecipe;
import mhfc.net.common.crafting.equipment.EquipmentRecipe.RecipeType;
import mhfc.net.common.index.ResourceInterface;
import mhfc.net.common.item.ItemType;
import mhfc.net.common.item.ItemType.GeneralType;
import mhfc.net.common.tile.TileHunterBench;
import mhfc.net.common.util.Assert;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiHunterBench extends MHFCTabbedGui {
	public static final ResourceLocation BURN_BACKGROUND = new ResourceLocation(
			ResourceInterface.gui_hunterbench_burn_back_tex);
	public static final ResourceLocation BURN_FOREGROUND = new ResourceLocation(
			ResourceInterface.gui_hunterbench_burn_front_tex);
	public static final ResourceLocation BURN_TARGET = new ResourceLocation(
			ResourceInterface.gui_hunterbench_burn_target_tex);
	public static final ResourceLocation BACKGROUND = new ResourceLocation(ResourceInterface.gui_hunterbench_back_tex);
	public static final ResourceLocation HUNTER_BENCH_COMPLETE = new ResourceLocation(
			ResourceInterface.gui_hunterbench_complete_tex);
	public static final ResourceLocation FUEL_DURATION_MARKER = new ResourceLocation(
			ResourceInterface.gui_hunterbench_fuel_tex);

	private static class AdapterLayerBipedArmor extends LayerBipedArmor {
		public AdapterLayerBipedArmor() {
			super(null);
		}

		@Override
		public void setModelSlotVisible(ModelBiped model, EntityEquipmentSlot slotIn) {
			super.setModelSlotVisible(model, slotIn);
		}

		@Override
		public void setModelVisible(ModelBiped model) {
			super.setModelVisible(model);
		}
	}

	private static AdapterLayerBipedArmor armorLayerProxy = new AdapterLayerBipedArmor();

	static final int maxHeat = 500;
	static final int modelRectRelX = 228;
	static final int modelRectRelY = 45;
	static final int modelRectW = 7 * 18 - 2;
	static final int modelRectH = 96;

	public class TypeItem extends GuiListItem {
		ItemType type;

		public TypeItem(ItemType type) {
			this.type = type;
		}

		public ItemType getType() {
			return type;
		}

		@Override
		@SideOnly(Side.CLIENT)
		public String getRepresentationString() {
			String str = type.getNameString();
			return I18n.format(str);
		}

	}

	public class RecipeItem extends GuiListItem {
		String representation;
		EquipmentRecipe recipe;

		public RecipeItem(EquipmentRecipe recipe) {
			this.recipe = recipe;
			representation = recipe.getRecipeOutput().getDisplayName();
		}

		public EquipmentRecipe getRecipe() {
			return recipe;
		}

		@Override
		public String getRepresentationString() {
			return representation;
		}

	}

	protected abstract class BenchEntityTab extends MHFCGui implements IMHFCTab {
		protected TileHunterBench bench;
		protected float modelRotX, modelRotY;
		protected float maxRotation = 50;

		/**
		 *
		 * @param start
		 *            Start index of slots on this page, inclusive
		 * @param end
		 *            End index of slots on this page, exclusive
		 */
		public BenchEntityTab(TileHunterBench bench) {
			this.bench = bench;
			resetModelRot();
		}

		@Override
		public void draw(double mousePosX, double mousePosY, float partialTick) {
			GL11.glColor4f(1f, 1f, 1f, 1f);
			mc.getTextureManager().bindTexture(BACKGROUND);
			MHFCGuiUtil.drawTexturedBoxFromBorder(0, 0, zLevel, xSize, ySize, 0, 0, 1f, 1f);

			// updateListPositions();
			super.draw(mousePosX, mousePosY, partialTick);

			fontRenderer.drawSplitString("Inventory", 6, 12, 500, 0x404040);

			GuiHunterBench.this.startCrafting.visible = !bench.isWorking();
			GuiHunterBench.this.startCrafting.enabled = bench.canBeginCrafting();
			drawItemModelAndHeat(bench, modelRotX, modelRotY);
		};

		@Override
		public boolean handleClick(float relativeX, float relativeY, int button) throws IOException {
			if (isInModelWindow(mouseClickX, mouseClickY) && mouseClickButton == 1) {
				resetModelRot();
			}
			super.handleClick(relativeX, relativeY, button);
			return true;
		}

		private void resetModelRot() {
			modelRotX = -50f;
			modelRotY = 20f;
		}

		@Override
		protected void itemUpdated(IMHFCGuiItem item) {
			if (item instanceof ClickableGuiList<?>) {
				listUpdated((ClickableGuiList<?>) item);
			}
		}

		protected void listUpdated(ClickableGuiList<?> list) {}

		protected void updateListPositions() {}

		@Override
		public boolean containsSlot(Slot slot) {
			return slot.inventory == tileEntity
					|| (slot.inventory instanceof InventoryPlayer && slot.getSlotIndex() < 36);
		}

		@Override
		public void updateTab() {
			updateListPositions();
		}

		@Override
		public void onClose() {}

		@Override
		public void onOpen() {
			bench.refreshState();
		}

		@Override
		public void handleMovementMouseDown(float mouseX, float mouseY, int button, long timeDiff) {
			if (isInModelWindow(mouseClickX, mouseClickY) && mouseClickButton == 0) {
				modelRotX += (mouseX - mouseLastX);
				modelRotY += (mouseY - mouseLastY);
				if (Math.abs(modelRotY) > maxRotation) {
					modelRotY = maxRotation * Math.signum(modelRotY);
				}
			}
			super.handleMovementMouseDown(mouseX, mouseY, button, timeDiff);
		}
	}

	/**
	 * A tab that displays recipes on one recipe type, filtered by their output type. The user can select the output
	 * type in a list.
	 */
	protected abstract class FilteredRecipeTab extends BenchEntityTab {

		protected RecipeType recipeType;
		protected List<ItemType> itemTypes;

		protected Map<ItemType, Integer> indicesOfTypes;
		protected Map<EquipmentRecipe, Integer> indicesOfRecipes;
		protected ClickableGuiList<TypeItem> typeList;
		protected ClickableGuiList<RecipeItem> recipeList;

		public FilteredRecipeTab(TileHunterBench bench, RecipeType recipeType, List<ItemType> itemTypes) {
			super(bench);

			this.recipeType = recipeType;
			this.itemTypes = itemTypes;

			indicesOfRecipes = new HashMap<>();
			indicesOfTypes = new HashMap<>();

			typeList = new ClickableGuiList<>(70, ySize - 24);
			recipeList = new ClickableGuiList<>(70, ySize - 24, 20);
			typeList.setAlignment(Alignment.MIDDLE);
			initializeTypeList();
			addScreenComponent(typeList, new Vector2f(78, 12));
			addScreenComponent(recipeList, new Vector2f(153, 12));

			if (bench != null) {
				EquipmentRecipe recipe = bench.getRecipe();
				if (recipe != null) {
					RecipeType rectype = recipe.getRecipeType();
					ItemType itemtype = recipe.getOutputType();
					Integer selectedIndex = indicesOfTypes.get(itemtype);
					typeList.setSelected(selectedIndex == null ? -1 : selectedIndex.intValue());
					listUpdated(typeList);
					if (rectype == recipeType) {
						Integer indexOfRecipe = indicesOfRecipes.get(recipe);
						recipeList.setSelected(indexOfRecipe == null ? -1 : indexOfRecipe.intValue());
					}
				}
			}
		}

		private void initializeTypeList() {
			for (ItemType itemType : itemTypes) {
				indicesOfTypes.put(itemType, typeList.size());
				typeList.add(new TypeItem(itemType));
			}
		}

		@Override
		protected void listUpdated(ClickableGuiList<?> list) {
			ItemType typeOfSelection = getSelectedType();
			if (list == typeList) {
				recipeList.clear();
				fillRecipeList(typeOfSelection);
			} else if (list == recipeList) {
				RecipeItem recI = recipeList.getSelectedItem();
				bench.changeRecipe(recI == null ? null : recI.getRecipe());
			}
		}

		protected ItemType getSelectedType() {
			TypeItem selectedItem = typeList.getSelectedItem();
			return selectedItem == null ? ItemType.NO_OTHER : selectedItem.getType();
		}

		@Override
		protected void updateListPositions() {
			getPosition(typeList).set(78, 12);
			getPosition(recipeList).set(153, 12);
		}

		protected void fillRecipeList(ItemType typeOfSelection) {
			Set<EquipmentRecipe> correspondingRecipes = MHFCEquipementRecipeRegistry.getRecipesForType(recipeType);
			if (correspondingRecipes == null) {
				return;
			}
			for (EquipmentRecipe rec : correspondingRecipes) {
				if (rec.getOutputType() == typeOfSelection) {
					indicesOfRecipes.put(rec, recipeList.size());
					recipeList.add(new RecipeItem(rec));
				}
			}
		}

	}

	protected class CraftArmorTab extends FilteredRecipeTab {
		public CraftArmorTab(TileHunterBench bench) {
			super(bench, RecipeType.ARMOR, ItemType.armorTypes);
		}
	}

	protected class CraftWeaponTab extends FilteredRecipeTab {
		public CraftWeaponTab(TileHunterBench bench) {
			super(bench, RecipeType.WEAPON, ItemType.weaponTypes);
		}
	}

	protected class CraftUpgradeTab extends FilteredRecipeTab {
		public CraftUpgradeTab(TileHunterBench bench) {
			super(bench, RecipeType.UPGRADE, ItemType.weaponTypes);
			typeList.setItemWidth(20);
		}
	}

	protected class WeaponTreeTab implements IMHFCTab {

		private float mouseX = 0, mouseY = 0;
		private int baseX = 0, baseY = 0;

		@Override
		public void draw(double mousePosX, double mousePosY, float partialTick) {
			startCrafting.visible = false;
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			drawRect(10, 10, xSize - 10, ySize - 10, 0xFF101010);
			drawCenteredString(fontRenderer, "Not yet implemented", xSize / 2 + baseX, ySize / 2 + baseY, 0xaaaaaa);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glPopMatrix();
		}

		@Override
		public boolean handleClick(float relativeX, float relativeY, int button) {
			mouseX = relativeX;
			mouseY = relativeY;
			if (button == 1) {
				baseX = 0;
				baseY = 0;
			}
			return true;
		}

		@Override
		public boolean containsSlot(Slot slot) {
			return false;
		}

		@Override
		public void updateTab() {}

		@Override
		public void onClose() {}

		@Override
		public void onOpen() {}

		@Override
		public void handleMovementMouseDown(float mouseX, float mouseY, int button, long timeDiff) {
			if (button == 0) {
				baseX += mouseX - this.mouseX;
				baseY += mouseY - this.mouseY;
			}
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}

		@Override
		public void handleMouseUp(float mouseX, float mouseY, int button) {
			if (button == 0) {
				baseX += mouseX - this.mouseX;
				baseY += mouseY - this.mouseY;
			}
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}

		@Override
		public void handleMovement(float mouseX, float mouseY) {
			this.mouseX = mouseX;
			this.mouseY = mouseY;
		}

		@Override
		public void initializeContext(Minecraft mc) {}

	}

	public final GuiButton startCrafting;
	public final TileHunterBench tileEntity;

	public GuiHunterBench(
			InventoryPlayer par1InventoryPlayer,
			World par2World,
			TileHunterBench tileEntity,
			int x,
			int y,
			int z) {
		super(new ContainerHunterBench(par1InventoryPlayer, par2World, tileEntity, x, y, z));
		this.tileEntity = tileEntity;
		this.xSize = 374;
		this.ySize = 220;
		mc = Minecraft.getMinecraft();
		width = MHFCGuiUtil.minecraftWidth(mc);
		height = MHFCGuiUtil.minecraftHeight(mc);
		this.guiLeft = (width - this.xSize - tabWidth) / 2 + tabWidth;
		this.guiTop = (height - this.ySize) / 2;
		this.addTab(new CraftArmorTab(tileEntity), "Armor");
		this.addTab(new CraftWeaponTab(tileEntity), "Weapons");
		this.addTab(new CraftUpgradeTab(tileEntity), "Upgrade");
		this.addTab(new WeaponTreeTab(), "Weapon tree");

		startCrafting = new GuiButton(0, guiLeft + 228 + (xSize - 228 - 60) / 2, guiTop + 166, 40, 20, "Craft") {
			@Override
			public void mouseReleased(int p_146118_1_, int p_146118_2_) {
				GuiHunterBench.this.tileEntity.beginCrafting();
			}
		};

		selectTab();
	}

	/**
	 * Selects a tab based on the currently selected recipe
	 */
	protected void selectTab() {
		int tab = 0;
		EquipmentRecipe recipe = tileEntity.getRecipe();
		if (recipe != null) {
			switch (recipe.getRecipeType()) {
			case ARMOR:
			case MHFC:
			default:
				tab = 0;
				break;
			case WEAPON:
				tab = 1;
				break;
			case UPGRADE:
				tab = 2;
				tab = 0;
			}
		}
		setTab(tab);
	}

	private void drawItemModelAndHeat(TileHunterBench bench, float modelRotX, float modelRotY) {
		if (bench != null) {
			ItemStack itemToRender = bench.getStackInSlot(TileHunterBench.resultSlot);
			ItemType itemType = ItemType.getTypeOf(itemToRender);

			int rectX = modelRectRelX, rectY = modelRectRelY;
			int scale = MHFCGuiUtil.guiScaleFactor(mc);

			drawItemModel(itemToRender, rectX, rectY, modelRectW, modelRectH, scale, itemType, modelRotX, modelRotY);
			drawBenchOverlay(bench, rectX, rectY, modelRectW);
		}
	}

	private static boolean isInModelWindow(double mouseClickX, double mouseClickY) {
		return mouseClickX >= modelRectRelX //
				&& mouseClickX <= (modelRectRelX + modelRectW) //
				&& mouseClickY >= modelRectRelY //
				&& mouseClickY <= (modelRectRelY + modelRectH);
	}

	private void drawItemModel(
			ItemStack itemToRender,
			int rectX,
			int rectY,
			int rectW,
			int rectH,
			int guiScale,
			ItemType itemType,
			float modelRotX,
			float modelRotY) {
		// TODO: maybe use and armor stand internally to render the items at
		modelRotX /= 2;
		modelRotY /= 4;

		GlStateManager.pushMatrix();
		drawRect(rectX, rectY, rectX + rectW + 1, rectY + rectH, 0xFF000000);

		GL11.glScissor(rectX * guiScale, mc.displayHeight - rectY * guiScale, rectW * guiScale, rectH * guiScale);
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GlStateManager.clearDepth(1.0f);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		GlStateManager.enableTexture2D();
		GlStateManager.enableDepth();
		if (itemToRender != null) {
			if (itemType.getGeneralType() == GeneralType.ARMOR) {
				renderArmor(itemToRender, rectX, rectY, rectW, rectH, itemType, modelRotX, modelRotY);
			} else if (itemType.getGeneralType() == GeneralType.WEAPON) {
				renderWeapon(itemToRender, rectX, rectY, rectW, rectH, modelRotX, modelRotY);
			} else {
				// TODO: render else...
			}
		}

		GlStateManager.popMatrix();
	}

	private static void renderWeapon(
			ItemStack itemToRender,
			int rectX,
			int rectY,
			int rectW,
			int rectH,
			float modelRotX,
			float modelRotY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.translate(rectX + rectW / 2, rectY + rectH / 2, 40F);
		GlStateManager.scale(3F, 3F, 3F);
		GlStateManager.translate(3f, 5f, 0F);
		GlStateManager.rotate(90F, 1.0f, 0.0f, 0.0f);
		GlStateManager.rotate(90F, 0.0f, 0.0f, 1.0f);
		modelRotY = Math.min(Math.abs(modelRotY), 30f) * Math.signum(modelRotY);
		GlStateManager.rotate(modelRotX, 0.0f, 0.0f, -1.0f);
		GlStateManager.rotate(modelRotY, 0.0f, -1.0f, 0.0f);
		float sc = rectH / 8;
		GlStateManager.scale(sc, -sc, sc);

		Minecraft mc = FMLClientHandler.instance().getClient();
		// false == isLeftHand
		mc.getItemRenderer().renderItemSide(mc.player, itemToRender, TransformType.THIRD_PERSON_RIGHT_HAND, false);
		GL11.glFrontFace(GL11.GL_CW);
		mc.getItemRenderer().renderItemSide(mc.player, itemToRender, TransformType.THIRD_PERSON_RIGHT_HAND, false);
		GL11.glFrontFace(GL11.GL_CCW);
	}

	private void renderArmor(
			ItemStack itemToRender,
			int rectX,
			int rectY,
			int rectW,
			int rectH,
			ItemType itemType,
			float modelRotX,
			float modelRotY) {
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.translate(rectX + rectW / 2, rectY + rectH / 2, 40F);
		float defaultScale = 2F;
		switch (itemType) {
		case ARMOR_HEAD:
			GlStateManager.scale(defaultScale, defaultScale, defaultScale);
			GlStateManager.translate(3f, -22f, 0F);
			break;
		case ARMOR_BODY:
			GlStateManager.scale(3F, 3F, 3F);
			GlStateManager.translate(1f, -38f, 0F);
			break;
		case ARMOR_PANTS:
			GlStateManager.scale(defaultScale, defaultScale, defaultScale);
			GlStateManager.translate(1f, -45f, 0F);
			break;
		case ARMOR_BOOTS:
			GlStateManager.scale(defaultScale, defaultScale, defaultScale);
			GlStateManager.translate(1f, -55f, 0F);
			break;
		case WEAPON_BOW:
		case WEAPON_BIG_BOWGUN:
		case WEAPON_DOUBLE_SWORD:
		case WEAPON_GREAT_SWORD:
		case WEAPON_GUNLANCE:
		case WEAPON_HAMMER:
		case WEAPON_HUNTING_HORN:
		case WEAPON_LANCE:
		case WEAPON_LONG_SWORD:
		case WEAPON_SMALL_BOWGUN:
		case WEAPON_SWORD_AND_SHIELD:
		case NO_OTHER:
		default:
			Assert.unreachable("Expected an armor, got {}", itemType);
		}
		GlStateManager.rotate(modelRotX, 0.0f, 1.0f, 0.0f);
		GlStateManager.rotate(-modelRotY, 1.0f, 0.0f, 0.0f);
		float sc = rectH / 2;
		GlStateManager.scale(sc, sc, -sc);
		EntityEquipmentSlot armorType = ((net.minecraft.item.ItemArmor) itemToRender.getItem()).armorType;

		ResourceLocation texture = armorLayerProxy.getArmorResource(mc.player, itemToRender, armorType, null);
		ModelBiped model = armorLayerProxy.getModelFromSlot(armorType);
		model = ForgeHooksClient.getArmorModel(mc.player, itemToRender, armorType, model);
		mc.getTextureManager().bindTexture(texture);
		armorLayerProxy.setModelVisible(model);
		armorLayerProxy.setModelSlotVisible(model, armorType);

		if (model != null) {
			model.render(mc.player, 0, 0, 0, 0, 0, 0.06125f);
			GL11.glFrontFace(GL11.GL_CW);
			model.render(mc.player, 0, 0, 0, 0, 0, 0.06125f);
			GL11.glFrontFace(GL11.GL_CCW);
		}
	}

	private void drawBenchOverlay(TileHunterBench bench, int rectX, int rectY, int rectW) {
		// Draw the background required heat indicator
		final int burnHeight = 96;
		final int completeWidth = 34;
		int heat;
		float burnTexVDiff;
		float burnTexV;
		int burnTexHeight;
		int burnTexY;

		// Draw the foreground current heat indicator
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		mc.getTextureManager().bindTexture(BURN_BACKGROUND);
		heat = Math.min(bench.getHeatStrength(), maxHeat);
		burnTexVDiff = (float) (heat) / maxHeat;
		burnTexV = 1.0f - burnTexVDiff;
		burnTexHeight = (int) (burnTexVDiff * burnHeight);
		burnTexY = rectY + burnHeight - burnTexHeight;
		MHFCGuiUtil.drawTexturedRectangle(
				rectX + rectW + 4,
				burnTexY,
				10,
				burnTexHeight,
				0.0f,
				burnTexV,
				1.0f,
				burnTexVDiff);

		if (bench.getRecipe() != null) {
			GlStateManager.glLineWidth(1f);
			heat = bench.getRecipe().getRequiredHeat();
			heat = Math.min(heat, maxHeat);
			burnTexVDiff = (float) (heat) / maxHeat;
			burnTexHeight = (int) (burnTexVDiff * burnHeight);
			burnTexY = rectY + burnHeight - burnTexHeight;
			GlStateManager.disableTexture2D();
			GlStateManager.glLineWidth(2.0f);
			GlStateManager.glBegin(GL11.GL_LINES);
			GlStateManager.color(0.4f, 0.4f, 0.4f, 1.0f);
			GlStateManager.glVertex3f(rectX + rectW + 4, burnTexY + 0.7f, this.zLevel);
			GlStateManager.glVertex3f(rectX + rectW + 14, burnTexY + 0.7f, this.zLevel);
			GlStateManager.glEnd();
			GlStateManager.enableTexture2D();
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);

		// Draw heat target
		mc.getTextureManager().bindTexture(BURN_TARGET);
		heat = Math.min(TileHunterBench.getItemHeat(bench.getStackInSlot(TileHunterBench.fuelSlot)), maxHeat);
		if (heat > 0) {
			burnTexVDiff = (float) (heat) / maxHeat;
			burnTexHeight = (int) (burnTexVDiff * burnHeight);
			burnTexY = rectY + burnHeight - burnTexHeight;
			MHFCGuiUtil.drawTexturedBoxFromBorder(rectX + rectW + 4, burnTexY - 1, this.zLevel, 10, 3, 0);
		}

		// Draw front layer, the border
		mc.getTextureManager().bindTexture(BURN_FOREGROUND);
		MHFCGuiUtil.drawTexturedBoxFromBorder(rectX + rectW + 4, rectY - 1, this.zLevel, 10, burnHeight + 1, 0);

		// draw the heat length indicator
		mc.getTextureManager().bindTexture(FUEL_DURATION_MARKER);
		float remaining = bench.getHeatLength() / (float) bench.getHeatLengthOriginal();
		if (Float.isInfinite(remaining)) {
			remaining = 0;
		}
		int remain = (int) Math.ceil(remaining * 14);
		remaining = remain / 17f;

		Tessellator t = Tessellator.getInstance();
		VertexBuffer buffer = t.getBuffer();
		buffer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX);
		buffer.pos(353, 159 - remain, this.zLevel).tex(0f, 14f / 17 - remaining).endVertex();
		buffer.pos(353, 159, this.zLevel).tex(0f, 14f / 17).endVertex();
		buffer.pos(370, 159, this.zLevel).tex(1f, 14f / 17).endVertex();
		buffer.pos(370, 159 - remain, this.zLevel).tex(1f, 14f / 17 - remaining).endVertex();
		t.draw();

		// draw the completition gauge
		if (bench.getRecipe() != null) {
			float completition = bench.getItemSmeltDuration() / (float) bench.getRecipe().getDuration();
			int complete = (int) (completition * completeWidth);
			completition = complete / (float) completeWidth;
			mc.getTextureManager().bindTexture(HUNTER_BENCH_COMPLETE);
			MHFCGuiUtil.drawTexturedBoxFromBorder(
					298,
					145,
					this.zLevel,
					(int) (completition * completeWidth),
					17,
					0,
					0,
					completition,
					1f);
		}
	}

	@Override
	public void initGui() {
		buttonList.add(startCrafting);
		super.initGui();
		this.guiLeft = (this.width - this.xSize - tabWidth) / 2 + tabWidth;
		this.guiTop = (this.height - this.ySize) / 2;
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void drawTabBackgroundLayer() {
		GlStateManager.color(1f, 1f, 1f, 1f);
		this.mc.getTextureManager().bindTexture(MHFCRegQuestVisual.QUEST_BOARD_BACKGROUND);
		int posX = guiLeft;
		int posY = guiTop;
		MHFCGuiUtil.drawTexturedBoxFromBorder(posX, posY, this.zLevel, this.xSize, this.ySize, 0, 0, 1f, 1f);
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		startCrafting.xPosition = guiLeft + 228 + (xSize - 228 - 60) / 2;
		startCrafting.yPosition = guiTop + 166;
	}

}

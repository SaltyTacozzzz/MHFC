package mhfc.net.client.quests.api;

import mhfc.net.common.quests.properties.Property;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.nbt.NBTBase;

/**
 * Represents dynamic information for a questing player with an active quest (mission).
 *
 * @author WorldSEnder
 *
 */
public interface IMissionInformation extends IPagedView {

	/**
	 * Updates the mission's date from the nbt. The nbt was generated from {@link Property#dumpUpdates()} on the server
	 * and contains updated variable to be sent to the client.
	 *
	 * @param nbtTag
	 *            the tag to read in.
	 */
	void updateProperties(NBTBase nbtTag);

	/**
	 * Gets the original quest definition of this mission information. Used to display the static quest description on
	 * the quest board, instead of dynamic information for the questing player.
	 *
	 * @return
	 */
	IVisualDefinition getOriginalDefinition();

	default void cleanUp() {
		// Called before the client disposes this information
	}

	String shortStatus();

	/**
	 * Draws the mission's information, reflecting the current state of the its properties.
	 *
	 * @param positionX
	 * @param positionY
	 * @param width
	 * @param height
	 * @param page
	 * @param fontRenderer
	 * @see IVisualDefinition#drawInformation(int, int, int, int, int, FontRenderer)
	 */
	@Override
	void drawInformation(int positionX, int positionY, int width, int height, int page, FontRenderer fontRenderer);
}

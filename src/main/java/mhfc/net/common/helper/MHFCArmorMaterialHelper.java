package mhfc.net.common.helper;

import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class MHFCArmorMaterialHelper {
	
	
	
	public static int enchant = 50;
	// Armor Durability: Basic = 400<durA> | Donors Rank = 850<durB> | S Rank = 1500<durC>
	//TODO G Rank , Promo, Exclusive<Choosen Players>
	public static int durA = 400, durB = 900, durC = 1500;

	// Basic Armors
	public static final ArmorMaterial ArmorYukumo = EnumHelper.addArmorMaterial         ("yukumo", durA, new int[]  {2, 5, 4, 2}, enchant);
	public static final ArmorMaterial ArmorCephalos = EnumHelper.addArmorMaterial       ("cepha", durA, new int[]   {2, 5, 4, 3}, enchant);
	public static final ArmorMaterial ArmorVelociprey = EnumHelper.addArmorMaterial     ("velociprey",durA,new int[]{2, 5, 5, 2}, enchant);
	public static final ArmorMaterial ArmorGreatJaggi = EnumHelper.addArmorMaterial     ("greatjaggi",durA,new int[]{2, 6, 6, 3}, enchant);
	public static final ArmorMaterial ArmorRathalos = EnumHelper.addArmorMaterial       ("rathalos", durA, new int[]{2, 8, 7, 3}, enchant);
	public static final ArmorMaterial ArmorBarroth = EnumHelper.addArmorMaterial        ("barroth", durA, new int[] {2, 8, 7, 5}, enchant);
	public static final ArmorMaterial ArmorTigrex = EnumHelper.addArmorMaterial         ("tigrex", durA, new int[]  {2, 10, 6, 3}, enchant);
	public static final ArmorMaterial ArmorKirin = EnumHelper.addArmorMaterial          ("kirin", durA, new int[]   {2, 8, 7, 4},enchant);
	public static final ArmorMaterial ArmorLagiacrus = EnumHelper.addArmorMaterial      ("lagia", durA, new int[]   {2, 9, 6, 5},enchant);
	
	
	

	// Donors Rank Armors
	public static final ArmorMaterial ArmorDragoon = EnumHelper.addArmorMaterial        ("dragoon", durB, new int[]{4, 7, 4, 2}, enchant);

	// S Rank Armors
	public static final ArmorMaterial ArmorKirinS = EnumHelper.addArmorMaterial         ("kirinS", durC, new int[]{3, 15, 6, 4}, enchant);
	public static final ArmorMaterial ArmorTigrexB = EnumHelper.addArmorMaterial        ("kishin", durC, new int[]{3, 16, 6, 3}, enchant);
	public static final ArmorMaterial ArmorDeviljho = EnumHelper.addArmorMaterial       ("deviljho", durB, new int[] {3, 12, 4, 4}, enchant);
	
	//Community 
	public static final ArmorMaterial ArmorST_1_Bionic = EnumHelper.addArmorMaterial    ("bionic", durB, new int[]{4, 7, 4, 2}, enchant);
}

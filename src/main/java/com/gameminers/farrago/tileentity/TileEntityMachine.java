package com.gameminers.farrago.tileentity;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileEntityMachine extends TileEntity implements ISidedInventory {
	private int direction;
	protected final String normalName;
	protected String customName;
	protected TileEntityMachine(String normalName) {
		this.normalName = normalName;
	}
	@Override
	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		tag.setInteger("Direction", direction);
		if (this.hasCustomInventoryName()) {
			tag.setString("CustomName", this.customName);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		direction = tag.getInteger("Direction");
		if (tag.hasKey("CustomName", 8)) {
			this.customName = tag.getString("CustomName");
		}
	}
	
	public void setName(String name) {
		this.customName = name;
	}

	@Override
	public String getInventoryName() {
		return this.hasCustomInventoryName() ? this.customName : this.normalName;
	}

	@Override
	public boolean hasCustomInventoryName() {
		return this.customName != null && this.customName.length() > 0;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public int getDirection() {
		return direction;
	}
	public abstract boolean isOn();
}
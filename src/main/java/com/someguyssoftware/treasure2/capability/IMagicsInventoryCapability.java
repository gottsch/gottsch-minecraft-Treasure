package com.someguyssoftware.treasure2.capability;

@Deprecated
public interface IMagicsInventoryCapability {

	int getMaxSocketSize();

	int getMaxImbueSize();

	int getMaxInnateSize();

	int getSocketSize();

	int getImbueSize();

	int getInnateSize();

	void setMaxSocketSize(int maxSocketsSize);

	void setMaxImbueSize(int maxImbueSize);

	void setMaxInnateSize(int maxInnateSize);

	void setInnateSize(int innateSize);

	void setImbueSize(int imbueSize);

	void setSocketSize(int socketSize);

}
package armark1ng.shopitemsautobuyer;

public class RegisteredItem {

	private int itemId;
	private int minAmountInShop;
	private int minCoins;
	private int openedId;
	private boolean stackable;

	public RegisteredItem(int itemId, int minAmountInShop, int minCoins, int openedId, boolean stackable) {
		this.itemId = itemId;
		this.minAmountInShop = minAmountInShop;
		this.minCoins = minCoins;
		this.openedId = openedId;
		this.stackable = stackable;
	}

	public int getItemId() {
		return itemId;
	}

	public int getMinAmountInShop() {
		return minAmountInShop;
	}

	public int getMinCoins() {
		return minCoins;
	}

	public boolean isPack() {
		return getOpenedId() != -1;
	}

	public int getOpenedId() {
		return openedId;
	}

	public boolean isStackable() {
		return stackable;
	}

	@Override
	public String toString() {
		return "[id=" + itemId + ", min amount=" + minAmountInShop + ", max price=" + minCoins + "]";
	}

}
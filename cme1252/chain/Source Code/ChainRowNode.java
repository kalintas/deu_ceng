public class ChainRowNode {
	
	private int row;
	private ChainRowNode down;
	private ChainElementNode right;
	
	public ChainRowNode(int row) {
		
		this.row=row;
		down=null;
		right=null;
		
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public ChainRowNode getDown() {
		return down;
	}

	public void setDown(ChainRowNode down) {
		this.down = down;
	}

	public ChainElementNode getRight() {
		return right;
	}

	public void setRight(ChainElementNode right) {
		this.right = right;
	}

}

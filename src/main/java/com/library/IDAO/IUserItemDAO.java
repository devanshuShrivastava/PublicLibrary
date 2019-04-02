package com.library.IDAO;
import java.util.List;
import com.library.businessModels.UserItem;

public interface IUserItemDAO {
	
	public List<UserItem> getAllBorrowedItems();
	public boolean removeItem(UserItem item);
	public boolean addItem(UserItem item);	
	public boolean isItemOnHold(UserItem item);
	public boolean isItemBorrowed(UserItem item);
}

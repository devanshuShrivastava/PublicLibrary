package com.library.search;

import javax.servlet.http.HttpSession;

public interface IDBSearchController {
	public SearchResults search(ISearchRequest searchRequestDetails, HttpSession httpSession);
	void clearPreviousSearch(HttpSession httpSession);
}

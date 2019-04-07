package com.library.search;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import com.library.signOut.ISignOutObserver;
import com.library.signOut.SignOutController;

public class DBSearchController implements IDBSearchController, ISignOutObserver {
	
	private static final int FIRST_PAGE = 1;
	private Map<HttpSession, SearchRequestAndResults> sessionToSearchRAndR = new HashMap<>();
	private LinkedList<HttpSession> sessions = new LinkedList<>();
	@Inject
	private ISearchResultCoverImgProxy coverImageProxy;
	
	public DBSearchController() {
		SignOutController.instance().registerAsSignOutObserver(this);
		new Thread(new HttpSessionMonitor(sessions, this)).start();
	}
		
	private class SearchRequestAndResults {
		ISearchRequest searchRequest;
		ISearchResults results;
		
		public SearchRequestAndResults(ISearchRequest requestDetails, ISearchResults results) {
			this.searchRequest = requestDetails;
			this.results = results;
		}
	}

	@Override
	public SearchResults search(ISearchRequest currentRequest, HttpSession httpSession) {
		SearchRequestAndResults searchRAndR = null;
		
		boolean searchIsInProgress = sessionToSearchRAndR.containsKey(httpSession);
		boolean isNewSearchTerms = false;
		
		if(searchIsInProgress) {
			searchRAndR = sessionToSearchRAndR.get(httpSession);
			ISearchRequest request = searchRAndR.searchRequest;
			isNewSearchTerms = searchRAndR.searchRequest.isNewSearchTerms(currentRequest);
			if(isNewSearchTerms) {
				clearSearch(httpSession);
				SearchTermsAndPage prevTermsAndPage = request.getTermsAndPage();
				String newSearchTerms = currentRequest.getTermsAndPage().getSearchTerms();
				prevTermsAndPage.setRequestedResultsPageNumber(FIRST_PAGE);
				prevTermsAndPage.setSearchTerms(newSearchTerms);
				searchRAndR = executeSearchInDb(request,httpSession);
			} else {
				int pageNumber = currentRequest.getTermsAndPage().getRequestedResultsPageNumber();
				request.getTermsAndPage().setRequestedResultsPageNumber(pageNumber);
			}
		} else {
			sessions.add(httpSession);
			searchRAndR = executeSearchInDb(currentRequest, httpSession);
		}
		
		int requestedPageNumber = searchRAndR.searchRequest.getTermsAndPage().getRequestedResultsPageNumber(); 
		SearchResults resultSet = searchRAndR.results.getResultSetForPageNumber(requestedPageNumber);
		if(resultSet.isNotEmpty()) {
			coverImageProxy.loadCoverImages(resultSet, Integer.toString(requestedPageNumber), httpSession);
		}
		return resultSet;
	}

	private SearchRequestAndResults executeSearchInDb(ISearchRequest request, HttpSession httpSession) {
		ISearchResults searchResults = request.searchInDb();
		SearchRequestAndResults searchRAndR = new SearchRequestAndResults(request, searchResults);
		sessionToSearchRAndR.put(httpSession, searchRAndR);
		return searchRAndR;
	}

	@Override
	public boolean notifyUserSignOut(HttpSession httpSession) {
		httpSession.invalidate();
		return clearSearch(httpSession);
	}
	
	@Override
	public boolean clearSearch(HttpSession httpSession) {
		if(sessionToSearchRAndR.containsKey(httpSession)) {
			coverImageProxy.deleteCoverImagesForSearchResults(httpSession);
			sessionToSearchRAndR.remove(httpSession);
		return true;
	}
	return false;
	}
}

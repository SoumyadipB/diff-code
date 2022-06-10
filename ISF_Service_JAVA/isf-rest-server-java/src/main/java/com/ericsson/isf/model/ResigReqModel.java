package com.ericsson.isf.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ResigReqModel {
	

		@JsonFormat(pattern="yyyy-MM-dd")
		private Date releaseDate;
		
		private String signum;
		private String currentUser;
		public Date getReleaseDate() {
			return releaseDate;
		}
		public void setReleaseDate(Date releaseDate) {
			this.releaseDate = releaseDate;
		}
		public String getSignum() {
			return signum;
		}
		public void setSignum(String signum) {
			this.signum = signum;
		}
		public String getCurrentUser() {
			return currentUser;
		}
		public void setCurrentUser(String currentUser) {
			this.currentUser = currentUser;
		}
		
		
		
		
}

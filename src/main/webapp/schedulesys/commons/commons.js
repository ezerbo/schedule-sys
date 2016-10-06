(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('Commons', Commons);
	
	
	function Commons(){
		var commons = this;
		
		commons.getCurrentWeekStartDate = getCurrentWeekStartDate;
		commons.getCurrentWeekEndDate = getCurrentWeekEndDate;
		
		function getCurrentWeekStartDate(){
			var currentDate = new Date();
			var currentDateIndex = currentDate.getDay();
			var currentWeekStartDate = new Date(
					currentDate.getFullYear(),
					currentDate.getMonth(),
					currentDate.getDate() - currentDateIndex+1);
			return currentWeekStartDate;
		}
		
		function getCurrentWeekEndDate(){
			var currentWeekStartDate = commons.getCurrentWeekStartDate();
			var currentWeekEndDate = new Date(
					currentWeekStartDate.getFullYear(),
					currentWeekStartDate.getMonth(), 
					currentWeekStartDate.getDate() + 6);
			
			return currentWeekEndDate;
		}
		
		return commons;
	}
})();
(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('Commons', Commons);
	
	Commons.$Inject = ['$state', '$rootScope'];
	
	function Commons($state, $rootScope){
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
	
	function gotoPreviousState(reloadSate){
		$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: reloadState});
	}
})();
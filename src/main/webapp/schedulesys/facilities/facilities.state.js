(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider.state('home.facilities', {
			templateUrl: 'schedulesys/facilities/facilities.html',
			controller: 'FacilitiesController'
		})
	}
	
})();
(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider.state('home.caregivers', {
			templateUrl: 'schedulesys/caregivers/caregivers.html',
			controller: 'careGiversController'
		})
	}
	
})();
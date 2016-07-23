(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider.state('home.nurses', {
			templateUrl: 'schedulesys/nurses/nurses.html',
			controller: 'NursesController'
		})
	}
	
})();
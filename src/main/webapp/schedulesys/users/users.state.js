(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider.state('home.users', {
			templateUrl: 'schedulesys/users/users.html',
			controller: 'usersController'
		})
	}
	
})();
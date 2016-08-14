(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider.state('home.users', {
			url: '/users',
			templateUrl: 'schedulesys/users/users.html',
			controller: 'usersController'
		}).state('home.users.new', {
			url: '/new',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New User',
					templateUrl: 'schedulesys/users/user-dialog.html',
					parent: angular.element(document.body),
					controller: 'userDialogController',
					clickOutsideToClose:true
				}).then(function() {
					$state.go($state.parent, {}, { reload: true });
				}, function() {
					$state.go('^');
				});
			}]
		}).state('home.users.edit', {
			url:'/{id}/user/edit',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/users/user-dialog.html',
					parent: angular.element(document.body),
					controller: 'userDialogController',
					clickOutsideToClose:true
				}).then(function() {
					console.log('Clicked on save');
					
				}, function() {
					$state.go('^');
				});
			}]
//		,
//			onExit: ['$state', function($state){
//				$state.go($state.current, {}, { reload: true });
//			}]
			
		})
		.state('home.user-details', {
			url: '/{id}/user/details',
			templateUrl: 'schedulesys/users/user-details.html',
			controller: 'userDetailsController'
		})
	}
	
})();
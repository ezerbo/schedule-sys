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
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New User',
					templateUrl: 'schedulesys/users/user-dialog.html',
					parent: angular.element(document.body),
					controller: 'userDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		}).state('home.users.edit', {
			url:'/{id}/user/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/users/user-dialog-edit.html',
					parent: angular.element(document.body),
					controller: 'userDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
			
		})
		.state('home.user-details', {
			url: '/{id}/user/details',
			templateUrl: 'schedulesys/users/user-details.html',
			controller: 'userDetailsController'
		})
	}
	
})();
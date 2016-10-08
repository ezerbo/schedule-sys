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
					templateUrl: 'schedulesys/users/user-dialog.html',
					parent: angular.element(document.body),
					controller: 'userDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.user-profile',{
			url: '/user/profile',
			templateUrl: 'schedulesys/users/users.profile.html',
			controller: 'UserProfileController'
		})
		.state('activate-user',{
			url: '/activate',
			templateUrl: 'schedulesys/users/activate-user.html',
			controller: 'UserActivationController'
		})
		.state('password-reset-request',{
			url: '/reset-request',
			templateUrl: 'schedulesys/users/password-reset-request.html',
			controller: 'PasswordResetRequestController'
		})
		.state('reset-request',{
			url: '/reset-password',
			templateUrl: 'schedulesys/users/password-reset.html',
			controller: 'PasswordResetController'
		}) 
	}
	
})();
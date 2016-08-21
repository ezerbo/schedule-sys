(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider
		.state('home.nurses', {
			url: '/nurses',
			templateUrl: 'schedulesys/nurses/nurses.html',
			controller: 'NursesController'
		})
		.state('home.nurses.new', {
			url: '/new',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New Nurse',
					templateUrl: 'schedulesys/nurses/nurse-dialog.html',
					parent: angular.element(document.body),
					controller: 'NurseDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.nurses.edit', {
			url:'/{id}/nurses/edit',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/nurses/nurse-dialog.html',
					parent: angular.element(document.body),
					controller: 'NurseDialogController',
					clickOutsideToClose:true
				}).then(function() {
					console.log('Clicked on save');
					
				}, function() {
					$state.go('^');
				});
			}]
			
		})
		.state('home.nurse-details', {
			url: '/nurses/{id}/details',
			templateUrl: 'schedulesys/nurses/nurse-details.html',
			controller: 'NurseDetailsController'
		})
		.state('home.licenses-new', {
			url: 'nurse/{nurseId}/new',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New Nurse',
					templateUrl: 'schedulesys/licenses/license-dialog.html',
					parent: angular.element(document.body),
					controller: 'LicenseDialogController',
					clickOutsideToClose:true
				});
			}]
		})
		.state('home.nurse-details.add-license', {
			url:'/licenses',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/licenses/license-dialog.html',
					parent: angular.element(document.body),
					controller: 'LicenseDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.nurse-details.edit-license', {
			url:'/licenses/{licenseId}/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/licenses/license-dialog.html',
					parent: angular.element(document.body),
					controller: 'LicenseDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.nurses-scheduling', {
			url:'/nurses/{id}/schedules',
			data: {
				schedulingType: 'employee'
	        },
			templateUrl: 'schedulesys/schedules/facilities.scheduling-page.html',
			controller: 'FacilitySchedulingController'
		})
	}
})();
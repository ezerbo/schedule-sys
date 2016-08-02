(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider
		.state('home.facilities', {
			url: '/facilities',
			templateUrl: 'schedulesys/facilities/facilities.html',
			controller: 'FacilitiesController'
		})
		.state('home.facilities.new', {
			url: '/new',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New facility',
					templateUrl: 'schedulesys/facilities/facility-dialog.html',
					parent: angular.element(document.body),
					controller: 'FacilityDialogController',
					clickOutsideToClose:true
				}).then(function() {
					$state.go($state.parent, {}, { reload: true });
				}, function() {
					$state.go('^');
				});
			}]
		})
		.state('home.facilities.edit', {
			url:'/{id}/edit',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/facilities/facility-dialog.html',
					parent: angular.element(document.body),
					controller: 'FacilityDialogController',
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
		.state('home.facilities-details', {
			url: '/{id}/details',
			templateUrl: 'schedulesys/facilities/facilities-details.html',
			controller: 'FacilityDetailsController'
		})
		.state('home.facilities-scheduling', {
			url: '/{id}/schedules',
			templateUrl: 'schedulesys/schedules/facilities.scheduling-page.html',
			controller: 'FacilitySchedulingController'
		})
		.state('home.facilities-scheduling.add', {
			url: '/{id}/schedules/add',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/schedules/schedule-dialog.html',
					parent: angular.element(document.body),
					controller: 'FacilitySchedulingDialogController',
					clickOutsideToClose:true
				}).then(function() {
					console.log('Clicked on save');
				}, function() {
					$state.go('^');
				});
			}]
		})
	}
})();

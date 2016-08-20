(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider.state('home.caregivers', {
			url: '/caregivers',
			templateUrl: 'schedulesys/caregivers/caregivers.html',
			controller: 'careGiversController'
		}).state('home.caregivers.new', {
			url: '/new',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New Care-Giver',
					templateUrl: 'schedulesys/caregivers/caregiver-dialog.html',
					parent: angular.element(document.body),
					controller: 'careGiverDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		}).state('home.caregivers.edit', {
			url:'/{id}/caregiver/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/caregivers/caregiver-dialog.html',
					parent: angular.element(document.body),
					controller: 'careGiverDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.caregiver-details', {
			url: '/{id}/caregiver/details',
			templateUrl: 'schedulesys/caregivers/caregiver-details.html',
			controller: 'careGiverDetailsController'
		})
		.state('home.caregivers-scheduling', {
			url: '/care-givers/{id}/schedules',
			data: {
				schedulingType: 'employee'
	        },
			templateUrl: 'schedulesys/schedules/facilities.scheduling-page.html',
			controller: 'FacilitySchedulingController'
		})
	}
	
})();
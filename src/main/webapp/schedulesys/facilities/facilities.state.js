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
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New facility',
					templateUrl: 'schedulesys/facilities/facility-dialog.html',
					parent: angular.element(document.body),
					controller: 'FacilityDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.facilities.edit', {
			url:'/{id}/facility/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/facilities/facility-dialog.html',
					parent: angular.element(document.body),
					controller: 'FacilityDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.facilities-details', {
			url: '/facilities/{id}/details',
			templateUrl: 'schedulesys/facilities/facilities-details.html',
			controller: 'FacilityDetailsController'
		})
		.state('home.facilities-scheduling', {
			url: '/facilities/{id}/schedules',
			 data: {
	                schedulingType: 'facility'
	            },
			templateUrl: 'schedulesys/facility-schedules/facilities.scheduling-page.html',
			controller: 'FacilitySchedulingController'
		})
		.state('home.facilities-scheduling.add', {
			url: '/add',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/facility-schedules/schedule-dialog.html',
					parent: angular.element(document.body),
					controller: 'FacilitySchedulingDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.facilities-scheduling.edit', {
			url: '/{scheduleId}/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/facility-schedules/schedule-dialog.html',
					parent: angular.element(document.body),
					controller: 'FacilitySchedulingDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.facilities-details.add-staffmembers', {
			url: '/staff-members/new',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New Staff-Member',
					templateUrl: 'schedulesys/staff-members/staff-member-dialog.html',
					parent: angular.element(document.body),
					controller: 'StaffMemberDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		}).state('home.facilities-details.edit-staffmembers', {
			url:'/staff-members/{staffMemberId}/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/staff-members/staff-member-dialog.html',
					parent: angular.element(document.body),
					controller: 'StaffMemberDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
	}
})();

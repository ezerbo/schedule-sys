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
		}).state('home.caregivers.new', {
			url: '/new',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New Care-Giver',
					templateUrl: 'schedulesys/caregivers/caregiver-dialog.html',
					parent: angular.element(document.body),
					controller: 'careGiverDialogController',
					clickOutsideToClose:true
				}).then(function() {
					$state.go($state.parent, {}, { reload: true });
				}, function() {
					$state.go('^');
				});
			}]
		}).state('home.caregivers.edit', {
			url:'/{id}/caregiver/edit',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/caregivers/caregiver-dialog.html',
					parent: angular.element(document.body),
					controller: 'careGiverDialogController',
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
		.state('home.caregiver-details', {
			url: '/{id}/caregiver/details',
			templateUrl: 'schedulesys/caregivers/caregiver-details.html',
			controller: 'careGiverDetailsController'
		})
	}
	
})();
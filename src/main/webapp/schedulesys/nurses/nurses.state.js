(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider.state('home.nurses', {
			url: '/nurses',
			templateUrl: 'schedulesys/nurses/nurses.html',
			controller: 'NursesController'
		}).state('home.nurses.new', {
			url: '/new',
			onEnter: ['$stateParams', '$state', '$mdDialog', function($stateParams, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New Nurse',
					templateUrl: 'schedulesys/nurses/nurse-dialog.html',
					parent: angular.element(document.body),
					controller: 'NurseDialogController',
					clickOutsideToClose:true
				}).then(function() {
					$state.go($state.parent, {}, { reload: true });
				}, function() {
					$state.go('^');
				});
			}]
		}).state('home.nurses.edit', {
			url:'/{id}/nurse/edit',
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
//		,
//			onExit: ['$state', function($state){
//				$state.go($state.current, {}, { reload: true });
//			}]
			
		})
		.state('home.nurse-details', {
			url: '/{id}/nurse/details',
			templateUrl: 'schedulesys/nurses/nurse-details.html',
			controller: 'NurseDetailsController'
		})
	}
	
})();
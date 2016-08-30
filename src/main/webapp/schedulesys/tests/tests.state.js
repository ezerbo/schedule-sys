(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.config(stateConfig);
	
	stateConfig.$Inject = ['$stateProvider'];
	
	function stateConfig($stateProvider) {
		$stateProvider
		.state('home.tests', {
			url: '/tests',
			templateUrl: 'schedulesys/tests/tests.html',
			controller: 'TestsController'
		})
		.state('home.tests.new', {
			url: '/new',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					title: 'New test',
					templateUrl: 'schedulesys/tests/tests.dialog.html',
					parent: angular.element(document.body),
					controller: 'TestDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.tests.edit', {
			url:'/{id}/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/tests/tests.dialog.html',
					parent: angular.element(document.body),
					controller: 'TestDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.tests-details', {
			url: '/tests/{id}',
			templateUrl: 'schedulesys/tests/tests.details.html',
			controller: 'TestDetailsController'
		})
		.state('home.tests-details.new-sub-category', {
			url:'/sub-categories/new',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/test-subcategory/test-subcategory.dialog.html',
					parent: angular.element(document.body),
					controller: 'TestSubCategoryDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
		.state('home.tests-details.edit-sub-category', {
			url:'/sub-categories/{subCategoryId}/edit',
			onEnter: ['$rootScope', '$state', '$mdDialog', function($rootScope, $state, $mdDialog) {
				$mdDialog.show({
					templateUrl: 'schedulesys/test-subcategory/test-subcategory.dialog.html',
					parent: angular.element(document.body),
					controller: 'TestSubCategoryDialogController',
					clickOutsideToClose:true,
					onRemoving: function (){
						$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
					}
				});
			}]
		})
	}
})();
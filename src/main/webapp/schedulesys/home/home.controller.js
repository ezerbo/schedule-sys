(function(){
	'use strict';
	angular.module('scheduleSys')
			.controller('HomeController', HomeController);
	
	HomeController.$Inject = ['$scope'];
	
	function HomeController($scope){
		console.log('Home controller');
		$scope.currentNavItem = 'page1';
	}
	
})();
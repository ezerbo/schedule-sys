(function() {
	'use strict';
	
	angular
		.module("scheduleSys", [
		    'ngMaterial',
		    'ui.router',
		    'ngMessages',
		    'ngResource',
		    'angular-jwt',
		    'ngStorage',
		    'md.data.table',
		    'material.svgAssetsCache'
		]).run(run);
	
	run.$inject = ['$rootScope'];
	
	function run ($rootScope){
		 $rootScope.$on('$stateChangeSuccess', function (ev, to, toParams, from, fromParams) {
			 $rootScope.previousState = from;
			 $rootScope.previousStateParams = fromParams;
			});
	}
})();
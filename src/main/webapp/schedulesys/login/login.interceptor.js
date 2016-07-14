(function(){
	
	'use strict';
	
	angular.module('scheduleSys')
			.factory('AuthInterceptor', AuthInterceptor);
	
	AuthInterceptor.$Inject = ['$httpProvider'];
	
	function AuthInterceptor($httpProvider){
		return {
			'request': function(config) {
				console.log('configuration');
			}
		}
	}
})();
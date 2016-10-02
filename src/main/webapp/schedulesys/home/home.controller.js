(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('HomeController', HomeController);
	
	HomeController.$Inject = ['$scope', '$state', '$localStorage', '$sessionStorage', 'AuthenticationProvider', 'jwtHelper'];
	
	function HomeController($scope, $state, $localStorage, $sessionStorage, AuthenticationProvider, jwtHelper){
		var vm = this;
		vm.logout = logout;
		vm.showUsersMenuButton = showUsersMenuButton;
		
		showUsersMenuButton();
		
		function logout(){
			AuthenticationProvider.logout();
		}
		
		function showUsersMenuButton(){
			var auth_token = ($localStorage.auth_token) ? $localStorage.auth_token : $sessionStorage.auth_token;
			if(angular.isUndefined(auth_token)){
				$state.go('login', {}, {});
			}
			var tokenPayload = jwtHelper.decodeToken(auth_token);
			vm.showUsersMenu = (tokenPayload.auth == 'ADMIN') ? true : false;
		}
	}
	
})();
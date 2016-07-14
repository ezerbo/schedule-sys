(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.factory('AuthenticationProvider', AuthenticationProvider);
	
	AuthenticationProvider.$inject = ['$http', '$localStorage', '$sessionStorage'];
	
	function  AuthenticationProvider ($http, $localStorage, $sessionStorage){
		
		var service = {
				login: login,
				logout: logout,
				storeAuthenticationToken: storeAuthenticationToken
		};
		
		return service;
		
		function login(credentials, callback){
			
			$http.post('/authenticate', credentials).then(authSuccess, authFailure);
			
			function authSuccess(data) {
				var response = JSON.parse(JSON.stringify(data));
				var jwt = response.data.id_token;
				service.storeAuthenticationToken(jwt, credentials.rememberMe);
				
				callback(true);
			}
			
			function authFailure(response){
				callback(false);
			}
		}
		
		function storeAuthenticationToken(jwt, rememberMe) {
			if(rememberMe){
				$localStorage.authenticationToken = jwt;
			} else {
				$sessionStorage.authenticationToken = jwt;
			}
		}
		
		function logout() {
			delete $localStorage.authenticationToken;
			delete $sessionStorage.authenticationToken;
		}
	}
	
})();
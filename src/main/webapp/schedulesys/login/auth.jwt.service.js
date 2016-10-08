(function() {
	'use strict';
	angular
		.module('scheduleSys')
		.factory('AuthenticationProvider', AuthenticationProvider);
	
	AuthenticationProvider.$inject = ['$http', '$localStorage', '$sessionStorage', 'jwtHelper'];
	
	function  AuthenticationProvider ($http, $localStorage, $sessionStorage, jwtHelper){
		var principal = null;
		var service = {
				login: login,
				logout: logout,
				getPrincipal: getPrincipal,
				storeAuthenticationToken: storeAuthenticationToken
		};
		
		return service;
		
		function getPrincipal(){
			if(principal !== null)
				return principal;
			var jwt = ($localStorage.auth_token) ? $localStorage.auth_token : $sessionStorage.auth_token;
			return jwtHelper.decodeToken(jwt).sub;
		}
		
		function login(credentials, callback){
			
			$http.post('/authenticate', credentials).then(authSuccess, authFailure);
			
			function authSuccess(data) {
				var response = JSON.parse(JSON.stringify(data));
				var jwt = response.data.id_token;
				service.storeAuthenticationToken(jwt, credentials.rememberMe);
				var tokenPayload = jwtHelper.decodeToken(jwt);
				principal = tokenPayload.sub;
				callback(true);
			}
			
			function authFailure(response){
				callback(false);
			}
		}
		
		function storeAuthenticationToken(jwt, rememberMe) {
			if(rememberMe){
				$localStorage.auth_token = jwt;
			} else {
				$sessionStorage.auth_token = jwt;
			}
		}
		
		function logout() {
			delete $localStorage.auth_token;
			delete $sessionStorage.auth_token;
		}
	}
	
})();
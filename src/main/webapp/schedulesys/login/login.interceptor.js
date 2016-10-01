angular
  .module('scheduleSys')
  .config(function Config($httpProvider) {
	  $httpProvider.interceptors.push(['$q', '$location', '$localStorage', '$sessionStorage',
	                                   function($q, $location, $localStorage, $sessionStorage) {
          return {
              'request': function (config) {
                  config.headers = config.headers || {};
                  if ($localStorage.auth_token) {
                      config.headers.Authorization = 'Bearer ' + $localStorage.auth_token;
                  }else if($sessionStorage.auth_token){
                	  config.headers.Authorization = 'Bearer ' + $sessionStorage.auth_token;
                  }
                  return config;
              },
              'responseError': function(response) {
                  if(response.status === 401 || response.status === 403) {
                	  delete $localStorage.auth_token;
                	  delete $sessionStorage.auth_token;
                      $location.path('');
                  }
                  return $q.reject(response);
              }
          };
      }]);
  });
(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('usersService', usersService)
		.factory('UserAccountService', UserAccountService);
	
	usersService.$Inject = ['$resource'];
	
	function usersService($resource) {
		var resourceUrl = '/users/:id';
		
		return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT',
            	transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            },
            'remove':  {
                method: 'DELETE',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            },
            'save': {
                method: 'POST',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            
            }
        });
		
	}
	
	UserAccountService.$Inject = ['$resource'];
	
	function UserAccountService($resource){
		
		var resourceUrl = '/users/activate';
		
		return $resource(resourceUrl, {}, {
            'save': {
                method: 'POST',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            
            }
        });
	}
	
})();
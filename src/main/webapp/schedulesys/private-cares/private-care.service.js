(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('PrivateCareService', PrivateCareService)
		.factory('PrivateCareContactService', PrivateCareContactService);
	
	PrivateCareService.$Inject = ['$resource'];
	
	function PrivateCareService($resource) {
		var resourceUrl = '/private-cares/:id';
		
		return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {method: 'GET'},
            'update':{
                method: 'PUT',
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
	
	PrivateCareContactService.$Inject = ['$resource'];
	
	function PrivateCareContactService($resource){
		var resourceUrl = '/private-cares/:id/contacts/:contactId';
		return $resource(resourceUrl, {},{
			'query': { method: 'GET', isArray: true},
			'save': {
	                method: 'POST',
	                transformResponse: function (data) {
	                    if (data) {
	                        data = angular.toJson(data);
	                    }
	                    return data;
	                }
	            },
	        'update':{
	                method: 'PUT',
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
(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('NursesService', NursesService)
		.factory('NurseTestService', NurseTestService)
		.factory('NurseLicenseService', NurseLicenseService);
	
	NursesService.$Inject = ['$resource'];
	
	function NursesService($resource) {
		var resourceUrl = '/nurses/:id';
		
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
	
	NurseLicenseService.$Inject = ['$resource'];
	
	function NurseLicenseService($resource){
		var resourceUrl = '/nurses/:id/licenses/:licenseId';
		return $resource(resourceUrl, {},{
			'query': { method: 'GET', isArray: true},
			'save': {
				method: "POST", 
				 transformResponse: function (data) {
	                    if (data) {
	                        data = angular.toJson(data);
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
            }
		});
	}
	
	NurseTestService.$Inject = ['$resource'];
	
	function NurseTestService($resource){
		var resourceUrl = '/nurses/:id/tests/:testId'
			return $resource(resourceUrl, {}, {
				'query': { method: 'GET', isArray: true},
				'save': {
					method: "POST", 
					 transformResponse: function (data) {
		                    if (data) {
		                        data = angular.toJson(data);
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
	            }
			});
	}
	
})();
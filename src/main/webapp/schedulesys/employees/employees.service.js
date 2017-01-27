(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('EmployeesService', EmployeesService)
		.factory('EmployeeTestService', EmployeeTestService);	
	EmployeesService.$Inject = ['$resource'];
	
	function EmployeesService($resource){
		var resourceUrl = '/employees/:id';
		return $resource(resourceUrl, {},
				{'query': { method: 'GET', isArray: true}
		});
	}

	EmployeeTestService.$Inject = ['$resource'];
	
	function EmployeeTestService($resource){
		var resourceUrl = '/employees/:id/tests/:testId'
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
				'remove':  {
					method: 'DELETE',
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
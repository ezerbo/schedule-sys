(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('EmployeesService', EmployeesService)
		.factory('EmployeeTestService', EmployeeTestService)
		.factory('EmployeesScheduleService', EmployeesScheduleService);
	
	EmployeesService.$Inject = ['$resource'];
	
	function EmployeesService($resource){
		var resourceUrl = '/employees/:id';
		return $resource(resourceUrl, {},{
			  'get': {method: 'GET'}
		});
	}
	
	EmployeesScheduleService.$Inject = ['$resource'];
	
	function EmployeesScheduleService($resource){
		var resourceUrl = '/employees/:id/schedules';
		return $resource(resourceUrl, {},{
			'query': { method: 'GET', isArray: true,
				transformResponse: function(data){
            		try{
            			data = angular.fromJson(data);
            		}catch(error){
            			data = angular.toJson(data);
            		}
                    return data;
                }
			}
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
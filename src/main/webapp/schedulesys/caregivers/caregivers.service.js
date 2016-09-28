(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('careGiversService', careGiversService)
		.factory('CareGiverScheduleService', CareGiverScheduleService);
	
	careGiversService.$Inject = ['$resource'];
	
	function careGiversService($resource) {
		console.log('calling care-givers service');
		var resourceUrl = '/care-givers/:id';
		
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
	
	CareGiverScheduleService.$Inject = ['$resource'];
	
	function CareGiverScheduleService($resource){
		var resourceUrl = '/care-givers/:id/schedules';
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
	
})();
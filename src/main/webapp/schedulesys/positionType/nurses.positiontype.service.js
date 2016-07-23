(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('NursesPositionTypeService', NursesPositionTypeService);
	
	NursesPositionTypeService.$Inject = ['$resource'];
	
	function NursesPositionTypeService($resource) {
		console.log('calling nurses position service');
		var resourceUrl = '/position-types/1/positions/:id';
		
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
            'update': { method:'PUT' },
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
	
})();
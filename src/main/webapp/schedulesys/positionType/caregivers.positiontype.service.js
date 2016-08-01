(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('CareTakersPositionTypeService', CareTakersPositionTypeService);
	
	CareTakersPositionTypeService.$Inject = ['$resource'];
	
	function CareTakersPositionTypeService($resource) {
		console.log('calling CareTakers position service');
		var resourceUrl = '/position-types/2/positions/:id';
		
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
(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.directive('phonenumberDirective', ['$filter', function($filter) {
			/*
			Intended use:
				<phonenumber-directive placeholder='prompt' model='someModel.phonenumber'></phonenumber-directive>
			Where:
				someModel.phonenumber: {String} value which to bind only the numeric characters [0-9] entered
					ie, if user enters 617-2223333, value of 6172223333 will be bound to model
				prompt: {String} text to keep in placeholder when no numeric input entered
			*/
	 
			function link(scope, element, attributes) {
				// scope.inputValue is the value of input element used in template
				scope.inputValue = scope.phonenumberModel;
	 
				scope.$watch('inputValue', function(value, oldValue) {
					
					value = String(value);
					var number = value.replace(/[^0-9]+/g, '');
					scope.phonenumberModel = number;
					scope.inputValue = $filter('phonenumber')(number);
				});
			}
			
			return {
				link: link,
				restrict: 'E',
				scope: {
					phonenumberPlaceholder: '=placeholder',
					phonenumberModel: '=model',
				},
				//templateUrl: '/static/phonenumberModule/template.html',
				template: '<input ng-model="inputValue" type="tel" class="phonenumber" placeholder="{{phonenumberPlaceholder}}" title="Phonenumber (Format: (###)-###-####)">',
			};
		}])
	 
		.filter('phonenumber', function() {
		    /* 
		    Format phonenumber as: c (xxx) xxx-xxxx
		    	or as close as possible if phonenumber length is not 10
		    	if c is not '1' (country code not USA), does not use country code
		    */
		    
		    return function (number) {
			    /* 
			    @param {Number | String} number - Number that will be formatted as telephone number
			    Returns formatted number: (###) ###-####
			    	if number.length < 4: ###
			    	else if number.length < 7: (###) ###
	 
			    Does not handle country codes that are not '1' (USA)
			    */
		        if (!number) { return ''; }
	 
		        number = String(number);
	 
		        // Will return formattedNumber. 
		        // If phonenumber isn't longer than an area code, just show number
		        var formattedNumber = number;
	 
				// if the first character is '1', strip it out and add it back
				//var c = (number[0] == '1') ? '1 ' : '';
				//number = number[0] == '1' ? number.slice(1) : number;
	 
				// # (###) ###-#### as c (area) front-end
				var area = number.substring(0,3);
				var front = number.substring(3, 6);
				var end = number.substring(6, 10);
	 
				if (front) {
					formattedNumber = ("(" + area + ")-" + front);	
				}
				if (end) {
					formattedNumber += ("-" + end);
				}
				return formattedNumber;
		    };
		});
	
})();
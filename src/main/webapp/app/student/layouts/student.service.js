(function() {
	'use strict';
	angular.module('sampleApp').factory('Student', Student);

	Student.$inject = [ '$resource', 'DateUtils' ];

	function Student($resource, DateUtils) {
		var resourceUrl = 'api/students';
		
		
		return $resource(resourceUrl, {}, {
			'query' : {
				method : 'GET',
				isArray : true
			}
		});
	}
})();

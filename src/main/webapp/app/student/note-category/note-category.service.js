(function() {
    'use strict';
    angular
        .module('sampleApp')
        .factory('NoteCategory', NoteCategory);

    NoteCategory.$inject = ['$resource'];

    function NoteCategory ($resource) {
        var resourceUrl =  'api/note-categories/:id';

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
            'update': { method:'PUT' }
        });
    }
})();

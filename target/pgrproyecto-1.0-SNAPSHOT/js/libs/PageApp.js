(function () {
    var app = angular.module('pagemodule', ['ngRoute'])

    app.controller('PageController', 
    function($scope,$http){
            this.login=
            {
                correo:'', clave:''
            };
            
            this.valadar=function(){
                $http.get("rest/products/" + login.correo + "/" + login.clave).
                success(function (response) {$scope.page = response;}).
                error(function(data, status, headers, config) {
                   alert('error!');
            });
        };
     });

})();


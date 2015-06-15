(function () {
    var app = angular.module('singup', []);

app.controller('TipoRol',

     function($scope){
            $scope.rol = 'empleado';
            $scope.empleado =  true;
            $scope.postulante = true;
            
            $scope.Empleador=function(){
                $scope.empleado = false;
                $scope.postulante = true;
                $scope.rol = 'empleador';
            };
            $scope.Empleado=function(){
                $scope.empleado = true;
                $scope.postulante = false;
                $scope.rol = 'empleado';
            };
     }
     );
})();


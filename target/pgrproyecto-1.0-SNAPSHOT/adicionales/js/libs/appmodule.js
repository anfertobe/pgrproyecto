(function () {
    var app = angular.module('modone', ['ngRoute']);
   
    app.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider
                    // route for the about page
                    .when('/new', {
                        templateUrl: 'registro.html'

                    })

                    // route for the contact page
                    .when('/list', {
                        templateUrl: 'catalogo.html'

                    })

                    .when('/MNCategoria', {
                        templateUrl: 'MNCategoria.html'

                    })

                    .when('/home', {
                        templateUrl: 'home.html'

                    })

                    .when('/singup', {
                        templateUrl: 'singup.html'
                    })

                    .when('/MNOferta', {
                        templateUrl: 'MNOferta.html'
                    })
                    .when('/MNPersona', {
                        templateUrl: 'MNPersona.html'
                    })
                    .when('/MNInteres', {
                        templateUrl: 'MNInteres.html'
                    })
                    .when('/MNCalificacion', {
                        templateUrl: 'MNCalificacion.html'
                    })
                    .when('/MNLogin', {
                        templateUrl: 'MNLogin.html'
                    })
                    .otherwise({
                        redirectTo: '/MNLogin'
                    });
        }]);

     app.controller('perfilacion',
            function ($scope, $http) {
                this.Perfilacion={
                    hiddenHome:false,
                    hiddenOfertas:true,
                    hiddenPersona:true,
                    hiddenCalificar:true,
                    hiddenInteres:true,
                    hiddenComprar:true,
                    usuario:'',
                    nombre:'No hay usuario activo',
                }
                
                  this.cerrrarCesion= function () {
                      alert('Borrar');
                       delete sessionStorage.registro;
                       this.Perfilacion.usuario='';
                       this.Perfilacion.nombre='No hay usuario activo';
                                    
                                    
                  }
                
                    this.consultar= function () {
                        
                            if(typeof(Storage) !== "undefined") {
                                if (sessionStorage.registro) {
                                    this.Perfilacion.usuario=sessionStorage.registro;
                                    this.Perfilacion.nombre=sessionStorage.nombre;
                                    //alert('Es necesario abrir una nueva sesión en el browser');
                                    if(sessionStorage.tipo=="Postulante"){
                                       this.Perfilacion.hiddenOfertas=false; 
                                       this.Perfilacion.hiddenPersona=false;
                                       this.Perfilacion.hiddenCalificar=true;
                                       this.Perfilacion.hiddenInteres=false;
                                       this.Perfilacion.hiddenComprar=true;
                                    }else if(sessionStorage.tipo=="Publicante"){
                                       this.Perfilacion.hiddenOfertas=false; 
                                       this.Perfilacion.hiddenPersona=false;
                                       this.Perfilacion.hiddenCalificar=false;
                                       this.Perfilacion.hiddenInteres=true;
                                       this.Perfilacion.hiddenComprar=false;
                                    }else{
                                       this.Perfilacion.hiddenOfertas=true; 
                                       this.Perfilacion.hiddenPersona=true;
                                       this.Perfilacion.hiddenCalificar=true;
                                       this.Perfilacion.hiddenInteres=true;
                                       this.Perfilacion.hiddenComprar=true; 
                                    }
                                    
                                    
                                } else {
                                    this.Perfilacion.hiddenOfertas=true; 
                                       this.Perfilacion.hiddenPersona=true;
                                       this.Perfilacion.hiddenCalificar=true;
                                       this.Perfilacion.hiddenInteres=true;
                                       this.Perfilacion.hiddenComprar=true; 
                                }                  
                            } else {
                                alert('Sorry! No Web Storage support..');
                                this.Perfilacion.hiddenOfertas=true; 
                                this.Perfilacion.hiddenPersona=true;
                                this.Perfilacion.hiddenCalificar=true;
                                this.Perfilacion.hiddenInteres=true;
                                this.Perfilacion.hiddenComprar=true; 
                            }
                            
                    }
                        
     
                    }
                
                
            
    );
     

    
    app.controller('login',
            function ($scope, $http) {
                    
            
                    $scope.Publicantes = [];
                    $scope.Postulantes = [];
            
                    this.Usuario={
                        identificacion: 0,
                        Clave: 0
                    }
            
                    this.login= function () {
                        
                        var persona=null;
                        var tipo='';
                        var nombre='';
                        
                        for (var i = 0; i < $scope.Publicantes.length; i++) {
                            if ($scope.Publicantes[i].identificacion == this.Usuario.identificacion &&  $scope.Publicantes[i].identificacion == this.Usuario.Clave) {
                                 persona = $scope.Publicantes[i];
                                 nombre=persona.nombre;
                                 tipo='Publicante';
                            }
                        }
                    
                        if(persona==null){
                           for (var i = 0; i < $scope.Postulantes.length; i++) {
                               if ($scope.Postulantes[i].identificacion == this.Usuario.identificacion  &&  $scope.Postulantes[i].identificacion == this.Usuario.Clave) {
                                    persona = $scope.Postulantes[i];
                                    nombre=persona.nombre;
                                    tipo='Postulante';
                               }
                            }
                        }
                        
                        
                        if(persona==null){
                            alert('La persona no se encuentra en el sistema o los datos son incorrectos');
                        }else{
                            if(typeof(Storage) !== "undefined") {
                                if (sessionStorage.registro) {
                                    alert('Es necesario abrir una nueva sesión en el browser');
                                } else {
                                    sessionStorage.registro =  this.Usuario.identificacion ;
                                    sessionStorage.tipo =  tipo ;
                                    sessionStorage.nombre =  nombre ;
                                    alert('Login Éxitoso!!');
                                    window.location='#/home';
                                }                  
                            } else {
                                alert('Sorry! No Web Storage support..');
                            }
                            
                        }
                        
     
                    }
                    
                    this.registrarse = function(){
                        sessionStorage.tipo =  "Registro" ;
                        window.location='#/MNPersona';
                        
                    }  
                    this.consultar = function () {
                        $http.get("rest/tservice/Publicantes").
                                success(function (response) {
                                    $scope.Publicantes = response;
                                }).
                                error(function (data, status, headers, config) {
                                    alert('error!');
                                });
                         $http.get("rest/tservice/Postulantes").
                            success(function (response) {
                                $scope.Postulantes = response;
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                        });        
                    };
            
            
            }
    );
    
     
    app.controller('Calificacion',
            function ($scope, $http) {
            
                    $scope.ofertasG=null;
                    
                            $scope.ofertasT=null;
            
            
            
                    this.calificacion = {
                       id:0,rango: '',comentario: '', valor: 0
                   }
                        
          
                
                this.consultar = function () {
                    
            
                    
                };
            
                    this.registro = function () {
                        
                         $('#myPleaseWait').modal('show');
          
            
                       $http.put('rest/tservice/Calificacion/'+sessionStorage.oferta, this.calificacion).
                            success(function (data, status, headers, config) {
                                 $('#myPleaseWait').modal('hide');
                                alert('success!');
                                 window.location='#/MNOferta';
                            }).
                            error(function (data, status, headers, config) {
                                $('#myPleaseWait').modal('hide');
                                alert('error: ' + status + " - " + data );
                                window.location='#/MNOferta';
                            });
                      
                    };
            
            
            }
    );



    app.controller('controlregistro',
            function ($scope, $http) {
                this.producto =
                        {
                            idproducto: 0, nombre: '', precio: 0
                        };

                this.registrar = function () {
                    $http.put('rest/products', this.producto).
                            success(function (data, status, headers, config) {
                                alert('success!');
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                            });
                };
            }
    );

    app.controller('pedirProduc',
            function ($scope, $http) {
                this.consultar = function () {
                    $http.get("rest/products").
                            success(function (response) {
                                $scope.productos = response;
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                            });
                };
            });

    app.controller('TipoRol',
            function ($scope, $http) {
            
                $scope.OptionTipo=null;
                $scope.OptionFind=null;
                this.Categorias=[];
                $scope.ofertasG = [];
                $scope.Publicantes = [];
                $scope.Postulantes = [];
            
            
                this.habilitar={
                    hideAdicionar:true,
                    hideAplicar:true,
                    hideIntereses:true
                }

                this.Persona={
                    identificacion: 0,
                    nombre: '',
                    fechaNacimiento: new Date(),
                    correo: '',
                    direccion: '',
                    telefono: '',
                    pais: '',
                    region: '',
                    ciudad: '',
                }
                
                
                this.Postulante = {
                        identificacion: 0,hojaDeVida: {hojaDeVida: '',
                        fechaActualizacion: new Date(),foto: ''},
                        aspiracionSalarial: 0,
                        nombre: '',
                        fechaNacimiento: new Date(),
                        correo: '',
                        direccion: '',
                        telefono: '',
                        pais: '',
                        region: '',
                        ciudad: '',
                        ofertas: [],
                        ofertas_1: [],
                        intereses: [],
                        experienciaLaborals: []
                }
                
                this.Publicante={ 
                    identificacion:0,
                    experiencia:'',
                    fechaUltimaLicecia: new Date(),
                    nombre:'',
                    fechaNacimiento:new Date(),
                    direccion:'',
                    telefono:'',
                    pais:'',
                    ragion:'',
                    ciudad:'',
                    correo:'',
                    facturas:[],
                    ofertas:null
                }
                
                this.Buscar=function(){
                    var identiString = "";
                    var persona=null;
                  
                    for (var i = 0; i < $scope.Publicantes.length; i++) {
                        identiString = $scope.Publicantes[i].identificacion.toString();
                        if (identiString.equals($scope.OptionFind) || $scope.Publicantes[i].nombre.contains($scope.OptionFind)) {
                             persona = $scope.Publicantes[i];
                             this.Persona.region=persona.ragion;
                             $scope.OptionTipo="Publicante";
                             this.Publicante=persona;
                        }
                    }
                    
                    if(persona==null){
                        for (var i = 0; i < $scope.Postulantes.length; i++) {
                           if ($scope.Postulantes[i].identificacion == $scope.OptionFind || $scope.Postulantes[i].nombre.contains($scope.OptionFind)) {
                                persona = $scope.Postulantes[i];
                                this.Persona.region=persona.region;
                                $scope.OptionTipo="Postulante";
                                this.Postulante=persona;
                           }
                        }
     
                    }
                    
                    this.Persona.identificacion=persona.identificacion;
                    this.Persona.nombre=persona.nombre;
                    this.Persona.fechaNacimiento=persona.fechaNacimiento;
                    this.Persona.correo=persona.correo;
                    this.Persona.direccion=persona.direccion;
                    this.Persona.telefono=persona.telefono;
                    this.Persona.pais=persona.pais;
                    this.Persona.ciudad=persona.ciudad;
                    
                    this.habilitarControles();    
            
                    
                };

                this.aplicarOferta= function () {
                   var radios = document.getElementsByName('radAnswer');
          
                      $('#myPleaseWait').modal('show');
          
            
                    var find=false;

                    for (var i = 0, length = radios.length; i < length; i++) {
                        if (radios[i].checked) {
                            value=radios[i].value;
                          
                            find=true;
                            // only one radio can be logically checked, don't check the rest
                            break;
                        }
                    }
                    
                    if($scope.OptionTipo=="Postulante" && find){
                        
                      $http.put('rest/tservice//Ofertas/aplicarOferta/'+ sessionStorage.registro +'/'+value+'/').
                                success(function (data, status, headers, config) {
                                   $('#myPleaseWait').modal('hide');
                                   alert('success!');
                                }).
                                error(function (data, status, headers, config) {
                                   $('#myPleaseWait').modal('hide');
                                   alert('error: ' + status + " - " + data );
                       });      
                    
                    
                    
                    }else{
                        if(!find){
                            alert('No se ha seleccionado ninguna oferta');
                            
                        }else{
                            if($scope.OptionFind!=''){
                                alert('Debe buscar primero a la persona');
                            }else{
                                alert('Solo los postulantes pueden aplicar a las ofertas');
                            }
                        }
                        $('#myPleaseWait').modal('hide');
                    }
                    
                    
                };
                




            this.habilitarControles =  function (){
                    
                    if($scope.OptionTipo=="Postulante"){
                        document.getElementById("experiencia").disabled = true;
                        this.Publicante.experiencia="";
                        document.getElementById("hojadevida").disabled = false;
                        document.getElementById("fechaActualizacion").disabled = false;
                        document.getElementById("foto").disabled = false;
                        document.getElementById("aspiracionSalarial").disabled = false;
                    }else{
                        document.getElementById("experiencia").disabled = false;
                        this.Postulante.hojaDeVida.hojaDeVida="";
                        document.getElementById("hojadevida").disabled = true;
                        document.getElementById("foto").disabled = true;
                        this.Postulante.hojaDeVida.foto="";
                        document.getElementById("aspiracionSalarial").disabled = true;
                        this.Postulante.aspiracionSalarial="";
                        document.getElementById("fechaActualizacion").disabled = true;
                        this.Postulante.hojaDeVida.fechaActualizacion="";
                        //document.getElementById("adicionar").disabled = true;
                    }
                    
                    if(typeof(Storage) !== "undefined") {
                         if (sessionStorage.registro) {
                             if(sessionStorage.registro==this.Persona.identificacion && sessionStorage.tipo=="Postulante"){
                                 //alert('Habilitar aplicar');
                                 this.habilitar.hideAplicar=false;
                                 this.habilitar.hideIntereses=false;
                                 
                             }else{
                                 //alert('Desabilitar aplicar1');
                                 this.habilitar.hideAplicar=true;
                                 this.habilitar.hideIntereses=false;
                                 //alert(this.habilitar.hideAplicar);
                             }
                         }else{
                             if(sessionStorage.tipo=="Registro"){
                                 this.habilitar.hideAdicionar=false;
                                 this.habilitar.hideIntereses=true;
                             }
                         }                  
                    }else {
                         this.habilitar.hideAdicionar=true;
                         this.habilitar.hideAplicar=true;
                         this.habilitar.hideIntereses=true;
                         alert('Sorry! No Web Storage support..');
                    }
                   
            };


            this.registro = function () {
                 
                $('#myPleaseWait').modal('show');
          
            
                                
                if($scope.OptionTipo!= null){
                
                if($scope.OptionTipo=="Postulante"){
                    
                    this.Postulante.identificacion=this.Persona.identificacion;
                    this.Postulante.nombre=this.Persona.nombre;
                    this.Postulante.fechaNacimiento=this.Persona.fechaNacimiento;
                    this.Postulante.correo=this.Persona.correo;
                    this.Postulante.direccion=this.Persona.direccion;
                    this.Postulante.telefono=this.Persona.telefono;
                    this.Postulante.pais=this.Persona.pais;
                    this.Postulante.ciudad=this.Persona.ciudad;
                    this.Postulante.region=this.Persona.region;
                    
                    $http.put('rest/tservice/Postulantes', this.Postulante).
                                success(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    alert('success!');
                                    window.location='#/MNLogin';
                                                              
                                }).
                                error(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    alert('error: ' + status + " - " + data );
                                    
                                });
                    
                }else{
                    
                    this.Publicante.identificacion=this.Persona.identificacion;
                    this.Publicante.nombre=this.Persona.nombre;
                    this.Publicante.fechaNacimiento=this.Persona.fechaNacimiento;
                    this.Publicante.correo=this.Persona.correo;
                    this.Publicante.direccion=this.Persona.direccion;
                    this.Publicante.telefono=this.Persona.telefono;
                    this.Publicante.pais=this.Persona.pais;
                    this.Publicante.ciudad=this.Persona.ciudad;
                    this.Publicante.ragion=this.Persona.region;
                    
                    $http.put('rest/tservice/Publicantes', this.Publicante).
                                success(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    window.location='#/MNLogin';
                                    
                                }).
                                error(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    alert('error: ' + status + " - " + data );
                    });
                }
            }else{
                
                alert('Debe escoger un tipo de persona');
                
            }
                    
            };
            
                
            this.MNInteres = function () {
                window.location='#/MNInteres';
            };
                
            this.consultar = function () {
                
                $('#myPleaseWait').modal('hide');
                
                   $http.get("rest/tservice/Ofertas").
                            success(function (response) {
                                $scope.ofertasG = response;
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                            });
                    $http.get("rest/tservice/Publicantes").
                            success(function (response) {
                                $scope.Publicantes = response;
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                            });
                     $http.get("rest/tservice/Postulantes").
                            success(function (response) {
                                $scope.Postulantes = response;
                    
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                     });
                     
                     
                   if(typeof(Storage) !== "undefined") {
                         if (sessionStorage.registro) {
                             if(sessionStorage.tipo=="Postulante"){
                                 this.habilitar.hideAplicar=false;
                                 this.habilitar.hideAdicionar=true;
                                 this.habilitar.hideIntereses=false;
                              }else{
                                 this.habilitar.hideAplicar=true;
                                 this.habilitar.hideAdicionar=true;
                                 this.habilitar.hideIntereses=false;
                              }
                           $scope.OptionFind=sessionStorage.registro;
                         }else{
                             if(sessionStorage.tipo=="Registro"){
                                 this.habilitar.hideAdicionar=false;
                                 this.habilitar.hideIntereses=true;
                             }
                         }                  
                    }else {
                         this.habilitar.hideAplicar=true;
                         this.habilitar.hideAdicionar=true;
                         this.habilitar.hideIntereses=true;
                         alert('Sorry! No Web Storage support..');
                    }
                    
             };
           }
    );
    
 app.controller('PagoLicencia',
    function($scope,$http){
        this.pago={
         codigoSeguridad:'',
         nombreTarjeta:'',
         numeroTarjeta:''
       };
       
       this.usuario=0;
        
        if(typeof(Storage) !== "undefined") {
                         if (sessionStorage.registro) {
                             if(sessionStorage.tipo=="Publicante"){
                                 this.usuario=sessionStorage.registro;
                              }
                         }                  
                    }
        
       this.licencia=null;
       
       this.TraerLicencias = function(){
            $http.get("rest/tservice/licencias").success(function (response) {
                $scope.licencias = response;
                }).error(function (data, status, headers, config) {
                alert('error!' + data);
            });  
       };
       
       this.pagarLicencia = function(){
           this.licencia = this.licencia.split('-')[0];
       
            $('#myPleaseWait').modal('show');
                
           
           $http.post("rest/tservice/pagarlicencias/"+this.licencia+"/usuario/"+this.usuario, this.pago).
            success(function (data, status, headers, config) {
                alert('Pago realizado Satisfactoriamente.');
                 $('#myPleaseWait').modal('hide');
                
            }).
            error(function (data, status, headers, config) {
                alert('error: ' + status + " - " + data );
                 $('#myPleaseWait').modal('hide');
                
            });

       };
    });
    
    app.controller('Categoria',
            function ($scope, $http) {
                       
                    $scope.Categorias=[];    
                    $scope.OptionOf=null;

                    $scope.Postulantes=null;
                    
                    
                    this.Postulante = {
                        identificacion: 0,hojaDeVida: {hojaDeVida: '',
                        fechaActualizacion: new Date(),foto: ''},
                        aspiracionSalarial: 0,
                        nombre: '',
                        fechaNacimiento: new Date(),
                        correo: '',
                        direccion: '',
                        telefono: '',
                        pais: '',
                        region: '',
                        ciudad: '',
                        ofertas: [],
                        ofertas_1: [],
                        intereses: [],
                        experienciaLaborals: []
                }
                    
                    this.Interes={
                        id:0,
                        experiencia:[],
                        categorias:[]
                        
                    };
                    
                    this.Categoria={
                        id:0,
                        interes:[],
                        nombre:'',
                        ofertas:[]
                    };
                    
                    this.agregarInteres=function () {
                        
                        var idPersona=null;
                        
                        if(typeof(Storage) !== "undefined") {
                                if (sessionStorage.registro) {
                                    idPersona=sessionStorage.registro  ;
                                } else {
              
                                    alert('Es necesario abrir una nueva sesión en el browser');
                                    
                                }                  
                            } else {
                                alert('Sorry! No Web Storage support..');
                        }
                     
            
                     
                        var persona=null;
                        
                            for (var i = 0; i < $scope.Postulantes.length; i++) {
                                if ($scope.Postulantes[i].identificacion == idPersona) {
                                    persona = $scope.Postulantes[i];
                                 }
                            }
     
                           if(persona!=null){
     
                           this.Postulante=persona;
                          
                           
                           if (persona.intereses.length==0){
                            
                                    this.Categoria.interes[this.Categoria.interes.length]=this.Interes;
                          
                                    persona.intereses[persona.intereses.length]=this.Interes;
                            
                                    $http.put('rest/tservice/Interes',persona).
                                         success(function (data, status, headers, config) {
                                             alert('success!');
                                             window.location='#/MNPersona';
                                         }).
                                         error(function (data, status, headers, config) {
                                             alert('error: ' + status + " - " + data );
                                     });

                                    this.Interes={
                                        id:0,
                                        experiencia:[],
                                        categorias:[]
                                    }
                            }else{
                                alert('La persona ya posee un interes asociado');
                                 window.location='#/MNPersona';
                            }
                        }else{
                             alert('No es posible encontrar la persona');
                              window.location='#/MNPersona';
                        }
                        

                  };
              

                    this.consultar = function () {
                        
                     $http.get("rest/tservice/Categorias").
                            success(function (response) {
                                $scope.Categorias = response;
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!' + data);
                            });
                     $http.get("rest/tservice/Postulantes").
                            success(function (response) {
                                $scope.Postulantes = response;
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!' + data);
                            });
                    };
            
                    this.cargarCategoria= function () {
                        var ofertaT = null;
 
                        
 
                        for (var i = 0; i < $scope.Categorias.length; i++) {
                              if ($scope.Categorias[i].id == $scope.OptionOf.split('-')[0] && $scope.Categorias[i].nombre == $scope.OptionOf.split('-')[1].replace('(', '').replace(')', '')) {
                                  alert('Find');
                                  ofertaT = $scope.Categorias[i];
                             }
                        }

                       alert('Value');

                       this.Categoria=ofertaT;
                    };
            
                    this.registro = function () {
                        
                        $http.put('rest/tservice/Categorias', this.Categoria).
                                success(function (data, status, headers, config) {
                                    alert('success!');
                                }).
                                error(function (data, status, headers, config) {
                                    alert('error: ' + status + " - " + data );
                                });
                };
            }    
    );
    
    
    
    app.controller('Oferta',
            function ($scope, $http) {
                       
                $scope.OptionOf=null;
                $scope.OptionPub=null;
                 
                this.habilitar = {
                    hideAdicionar:true,
                    hideSelecionarEmpleado:true,
                    hideCalificar:true
                }
                
                
                 
                this.Oferta = {
                       id:'',
                       calificacion: {rango: '',comentario: '', valor: 0},
                       postulante:{identificacion: 0,hojaDeVida: {hojaDeVida: '',
                                fechaActualizacion: new Date(),foto: ''},
                                aspiracionSalarial: 0,
                                nombre: '',
                                fechaNacimiento: new Date(),
                                correo: '',
                                direccion: '',
                                telefono: '',
                                pais: '',
                                region: '',
                                ciudad: '',
                                ofertas: [],
                                ofertas_1: [],
                                intereses: [],
                                experienciaLaborals: []
                       },
                       publicante:{ identificacion:0,experiencia:'',fechaUltimaLicecia: new Date(),
                                    nombre:'',fechaNacimiento:new Date(),direccion:'',telefono:'',
                                    pais:'',ragion:'',ciudad:'',correo:'',facturas:[],ofertas:[]
                                                    
                       },
                       fechaCreacion:new Date(),
                       fechaFinalizacion:new Date(),
                       valor:0,
                       descripcion:'',
                       estado:'',
                       categorias: [],
                       postulantes: []
                };
                
            
            $scope.ofertasG = [];
            
           
            $scope.Publicantes = [];
            
            this.calificar= function () {

                if(this.Oferta.id!=''){
                    if(typeof(Storage) !== "undefined") {
                          if (sessionStorage.registro) {
                                sessionStorage.oferta=this.Oferta.id;
                                window.location='#/MNCalificacion';
                           }                  
                    }
                }else{
                    alert('No ha seleccionado una oferta');
                }
             
            };
            this.seleccionarEmpleado= function () {
                   var radios = document.getElementsByName('radAnswer');

                    $('#myPleaseWait').modal('show');
                    var value;
                    
                    var find=false;

                    for (var i = 0, length = radios.length; i < length; i++) {
                        if (radios[i].checked) {
                            value=radios[i].value;
                          
                            find=true;
                            // only one radio can be logically checked, don't check the rest
                            break;
                        }
                    }
                    
                    if(find){
                        
                    $http.put('rest/tservice//Ofertas/agregarEmpleadoOferta/'+ value +'/'+this.Oferta.id+'/').
                                success(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    alert('success!');
                                }).
                                error(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    alert('error: ' + status + " - " + data );
                                });
                    
                    
                }else{
                    alert('Debe seleccionar un empleado');
                    $('#myPleaseWait').modal('hide');
                    
                }
                    
            };
                

                
            this.cargarOferta   = function () {
               var ofertaT = null;
 
               for (var i = 0; i < $scope.ofertasG.length; i++) {
                     if ($scope.ofertasG[i].id == $scope.OptionOf.split('-')[0] && $scope.ofertasG[i].descripcion == $scope.OptionOf.split('-')[1].replace('(', '').replace(')', '')) {
                         ofertaT = $scope.ofertasG[i];
                    }
               }
               
               this.Oferta=ofertaT;
               
               if(typeof(Storage) !== "undefined") {
                      if (sessionStorage.registro) {
                          if(sessionStorage.tipo=="Postulante"){
                              this.habilitar.hideCalificar=true;
                              this.habilitar.hideAdicionar=true;
                              this.habilitar.hideSelecionarEmpleado=true;
                          }else{
                              this.habilitar.hideCalificar=false;
                              this.habilitar.hideAdicionar=false;
                              this.habilitar.hideSelecionarEmpleado=false;
                          
                          }
                          
                      }
               }
               
               
               //Ya tiene empleado
               if(this.Oferta.postulante!=null){
                   this.Oferta.postulantes=null;
               }
                      
            };
            
            this.consultar = function () {
                
                    var OfertasFiltro=[];
                
                     $http.get("rest/tservice/Ofertas").
                            success(function (response) {
                                $scope.ofertasG = response;
                                if (sessionStorage.registro) {
                                    if(sessionStorage.tipo=="Publicante"){
                                         for (var i = 0; i < $scope.ofertasG.length; i++) {
                                              if($scope.ofertasG[i].publicante.identificacion==sessionStorage.registro){
                                                  OfertasFiltro[OfertasFiltro.length]=$scope.ofertasG[i];
                                              }
                                          }
                                        $scope.ofertasG=OfertasFiltro;
                                    }
                                }
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                            });
                            
                       $http.get("rest/tservice/Publicantes").
                            success(function (response) {
                                $scope.Publicantes = response;
                            }).
                            error(function (data, status, headers, config) {
                                alert('error!');
                            });
                            
                            if(typeof(Storage) !== "undefined") {
               
                      if (sessionStorage.registro) {
                          if(sessionStorage.tipo=="Postulante"){
                              this.habilitar.hideCalificar=true;
                              this.habilitar.hideAdicionar=true;
                              this.habilitar.hideSelecionarEmpleado=true;
                          }else{
                                                           
                              this.habilitar.hideCalificar=false;
                              this.habilitar.hideAdicionar=false;
                              this.habilitar.hideSelecionarEmpleado=false;
                          
                          }
                      }
                      
               }
               
                            
            };
                
            this.registro = function () {
                var ofertaT = null;
                $('#myPleaseWait').modal('show');
                
                if(typeof(Storage) !== "undefined") {
                         if (sessionStorage.registro) {

                           $scope.OptionPub=sessionStorage.registro;
                         }                  
                    }else {
                         this.habilitar.hideAplicar=true;
                         this.habilitar.hideAdicionar=true;
                         alert('Sorry! No Web Storage support..');
                    }
 
                for (var i = 0; i < $scope.Publicantes.length; i++) {
                        if ($scope.Publicantes[i].identificacion == $scope.OptionPub) {
                             ofertaT = $scope.Publicantes[i];
                        }
                }
                this.Oferta.publicante=ofertaT;
               
                this.Oferta.postulante=null;
               
                 $http.put('rest/tservice/Ofertas/'+this.Oferta.publicante.identificacion, this.Oferta).
                                success(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    alert('success!');
                                }).
                                error(function (data, status, headers, config) {
                                    $('#myPleaseWait').modal('hide');
                                    alert('error: ' + status + " - " + data );
                                    
                                });
                };
                
                
            this.modificar = function () {
                   $http.put('rest/tservice/Ofertas/0', this.Oferta).
                                success(function (data, status, headers, config) {
                                    alert('success!');
                                }).
                                error(function (data, status, headers, config) {
                                    alert('error: ' + status + " - " + data );
                                });
                };
               
                    
                }
                    
        
                    
    );



})();
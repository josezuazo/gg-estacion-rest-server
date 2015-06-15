angular.module('RegistroVentasApp', ['ngResource'])
        .controller('RegistroVentasController', function ($scope, $http, $log, $resource) {
            $scope.flagSeleccionarUsuario = false;
            $scope.leyendoDatoSurtidor = false;
            $scope.flagInformacion = false;
            $scope.flagErrores = false;
            $scope.flagVehiculo = false;
            $scope.flagResumenVenta = false;
            $scope.flagConsultarPuntos = false;
            $scope.flagMostrarSurtidores = false;
            $scope.flagLectura = false;
            $scope.flagResumenVenta = false;
            $scope.flagValidar = true;
            $scope.flagValidarError=false;
            $scope.flagValidarOK=false;
            $scope.flagInformacionCambioPuntos = false;
            $scope.flagCambioPuntos = false;
            $scope.stompClient = null;

            $scope.usuario;
            $scope.usuarios = [];
            $scope.surtidores = [];
            $scope.surtidor;
            $scope.vehiculo;
            $scope.empresa;
            $scope.producto;
            $scope.productoCambiar;
            $scope.mensajeError;
            $scope.puntosAcumulados = 0;


            $scope.cargarUsuarios = function () {
                $http.get("/listadoUsuario").success(function (data, status, headers, config) {
                    $scope.usuarios = data;
                }).error($scope.fnError);
            };

            $scope.leerSurtidores = function () {
                $http.get("/listadoSurtidor").success(function (data, status, headers, config) {
                    $scope.surtidores = data;
                }).error($scope.fnError);
            };

            $scope.leerLlavero = function () {
                $scope.flagVehiculo = true;
                $scope.buscarEmpresa($scope.vehiculo.empresaId);
            };

/*
            $scope.leerLlavero = function () {
                $http.get("/vehiculo/leer").success(function (data, status, headers, config) {
                    $scope.flagVehiculo = true;
                    $scope.vehiculo = data;
                    $scope.buscarEmpresa($scope.vehiculo.empresaId);
                }).error($scope.fnError);
            };
*/

            $scope.buscarEmpresa = function (id) {
                $http.get("/empresa/" + id).success(function (data, status, headers, config) {
                    $scope.empresa = data;
                }).error($scope.fnError);
            };

            $scope.buscarProducto = function (id) {
                $http.get("/producto/" + id).success(function (data, status, headers, config) {
                    $scope.producto = data;
                }).error($scope.fnError);
            };

            $scope.leerDatosSurtidor = function (index) {
                $scope.flagInformacion = true;
                $scope.surtidor = $scope.surtidores[index];
                $http.get("/surtidor/leer/" + $scope.surtidor.surtidorId).success(function (data, status, headers, config) {
                    $scope.flagInformacion = false;
                    $scope.surtidorLectura = data;
                    $scope.buscarProducto(4);
                }).error($scope.fnError);
            };

            $scope.registrarVenta = function () {
                $http.put("/registro/agregar", {usuario: $scope.usuario, surtidor: $scope.surtidor, producto: $scope.producto, lectura: $scope.surtidorLectura, empresa: $scope.empresa, vehiculo: $scope.vehiculo}).success(function (data, status, headers, config) {
                    $scope.fechaProximaCarga = data.fecha;
                    $scope.obtenerPuntosDisponibles();
                }).error($scope.fnError);
            };

            $scope.obtenerPuntosDisponibles = function(){
                $http.get("/registro/puntos/"+$scope.empresa.empresaId).success(function (data, status, headers, config) {
                    $scope.puntosAcumulados = data;
                    $scope.flagResumenVenta = true;
                    $scope.flagLectura = false;
                }).error($scope.fnError);
            }

            $scope.obtenerPuntosDisponiblesCambioPuntos = function(){
                $http.get("/registro/puntos/"+$scope.empresa.empresaId).success(function (data, status, headers, config) {
                    $scope.puntosAcumulados = data;
                    $scope.obtenerProductos();
                }).error($scope.fnError);
            }

            $scope.seleccionarUsuario = function (index) {
                $scope.leerSurtidores();
                $scope.usuario = $scope.usuarios[index];
                $scope.flagSeleccionarUsuario = false;
                $scope.flagMostrarSurtidores = true;
                $scope.flagValidar = false;
                $scope.flagLectura = true;
                $scope.flagCambioPuntos = true;
                $scope.leerLlavero();
            };

            $scope.nuevaVenta = function () {
                $scope.flagSeleccionarUsuario = false;
                $scope.leyendoDatoSurtidor = false;
                $scope.flagInformacion = false;
                $scope.flagErrores = false;
                $scope.flagVehiculo = false;
                $scope.flagResumenVenta = false;
                $scope.flagConsultarPuntos = false;
                $scope.flagMostrarSurtidores = false;
                $scope.flagLectura = false;
                $scope.flagResumenVenta = false;
                $scope.flagValidar = true;
                $scope.flagValidarError=false;
                $scope.flagValidarOK=false;
                $scope.flagInformacionCambioPuntos = false;
                $scope.flagCambioPuntos = false;
                $scope.connect();
            };

            $scope.mostrarSurtidoresDiv = function () {
                return $scope.flagMostrarSurtidores;
            };
            $scope.mostrarLecturaDiv = function () {
                return $scope.flagLectura;
            };
            $scope.mostrarResumenVentaDiv = function(){
                return $scope.flagResumenVenta;
            };
            $scope.showConsultaPuntos = function (){
                $scope.flagConsultarPuntos = true;
                $scope.flagResumenVenta = false;
                $scope.flagValidar = false;
                $scope.flagMostrarSurtidores = false;
                $scope.flagLectura = false;
            };
            $scope.mostrarValidadVehiculo = function(){
                return $scope.flagValidar;
            };
            $scope.mostrarSeleccionarUsuario = function(){
                return $scope.flagSeleccionarUsuario;
            };
            $scope.obtenerProductos = function () {
                $http.get("/producto/listado/"+$scope.vehiculo.empresaId+"/"+$scope.puntosAcumulados).success(function (data, status, headers, config) {
                    $scope.productos = data;
                }).error($scope.fnError);
            };

            $scope.buscarEmpresaCambioPuntos = function (id) {
                $http.get("/empresa/" + id).success(function (data, status, headers, config) {
                    $scope.empresa = data;
                    $scope.obtenerPuntosDisponiblesCambioPuntos();
                }).error($scope.fnError);
            };

            $scope.leerLlaveroCambioPuntos = function () {
                $http.get("/vehiculo/leer").success(function (data, status, headers, config) {
                    $scope.vehiculo = data;
                    $scope.buscarEmpresaCambioPuntos($scope.vehiculo.empresaId);
                    $scope.flagInformacionCambioPuntos = false;
                }).error($scope.fnError);
            };

            $scope.registrarCambioPuntos = function (index) {
                $scope.producto = $scope.productos[index];
                $http.put("/registro/cambiarPuntos", {surtidor: $scope.surtidores[0], usuario: $scope.usuario, producto: $scope.producto, empresa: $scope.empresa, vehiculo: $scope.vehiculo}).success(function (data, status, headers, config) {
                    $scope.obtenerPuntosDisponiblesCambioPuntos();
                    $scope.flagInformacionCambioPuntos=true;
                    $scope.flagResumenVenta = false;
                }).error($scope.fnError);
            };
            $scope.fnError = function (data, status, headers, config) {
                $scope.flagErrores = true;
                $scope.mensajeError = "Error inesperado en el servidor";
                $log.error($scope.mensajeError);
            };
/*
            $scope.leerLlaveroValidar = function () {
                $http.get("/validar/vehiculo").success(function (data, status, headers, config) {
                    $scope.flagValidarOK = data;
                    $scope.flagValidarError = !$scope.flagValidarOK;
                    $scope.flagSeleccionarUsuario = true;
                }).error($scope.fnError);
            };
*/
            $scope.fnError = function (data, status, headers, config) {
                $scope.flagErrores = true;
                $scope.mensajeError = "Error inesperado en el servidor";
                $log.error($scope.mensajeError);
            };

            $scope.connect = function(){
                $scope.disconnect();
                var socket = new SockJS('/wsvehiculo');
                $scope.stompClient = Stomp.over(socket);
                $scope.stompClient.connect({}, function(frame) {
                    //setConnected(true);
                    console.log('Connected: ' + frame);
                    $scope.stompClient.subscribe('/wsvehiculo/getvehiculo', function(data){
                        var o = JSON.parse(data.body);
                        $scope.flagValidarOK = o.activado;
                        $scope.vehiculo = o.vehiculo;
                        $scope.flagValidarError = !$scope.flagValidarOK;
                        $scope.flagSeleccionarUsuario = true;
                        $scope.disconnect();
                        $scope.$apply();
                    });
                });
            };

            $scope.disconnect = function () {
                if ($scope.stompClient != null) {
                    $scope.stompClient.disconnect();
                }
                //setConnected(false);
                console.log("Disconnected");
            };

            $scope.nuevaVenta();
            $scope.cargarUsuarios();
            //$scope.connect();

        });
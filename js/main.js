app = angular.module('app', []);

var SERVERIP = 'http://10.55.33.11:8080/';

app.controller('mainCtrl', function($scope, $http, $timeout, $interval){

	$scope.loading = true;

	$scope.orders = $scope.products = [];

	setTime = function(){
		date = new Date();
		$scope.curTime = date.toLocaleString('ru', 
			{
				year: 'numeric',
				month: 'long',
				day: 'numeric',
				hour: 'numeric',
				minute: 'numeric',
				second: 'numeric'
			});
	}
	setTime();
	$interval(setTime, 1000);
	

	$scope.updateOrders = function(){
		$http.get(SERVERIP + 'orders').then(function(response){
			response.data.orders = response.data.orders.map(function(order, i, orders){
				order.products = order.products.map(function(product, k, products){
					product.info = $scope.products.filter(function(product_info){
						return product_info.id == product.id;
					})[0];
					return product;
				});
				return order;
			});

			var newItems = response.data.orders.filter(function(order){
				for (var i in $scope.orders)
					if ($scope.orders[i].id == order.id)
						return false;
				return true;
			});

			$timeout(function(){
				for (var i in newItems){
					$scope.orders.push(newItems[i]);
				}
				if ($scope.loading)
					$scope.loading = false;
			});
		});
	}

	$http.get(SERVERIP + 'products').then(function(response){
		$scope.products = response.data.products;

		$scope.updateOrders();
		$interval($scope.updateOrders, 5000);
	});

});
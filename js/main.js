app = angular.module('app', []);

var SERVERIP = 'http://10.55.33.11:8080/';

app.controller('mainCtrl', function($scope, $rootScope, $http, $timeout, $interval){

	$scope.loading = true;

	$scope.orders = $scope.products = [];

	$rootScope.timeDiff = $scope.timeDiff = function(end){
		var begin = new Date();
		var end = new Date(end);
		var diff = new Date(end - begin);
		return diff.toLocaleString('ru',
		{
			hour: 'numeric',
			minute: 'numeric',
			second: 'numeric'
		});
	}

	$rootScope.getTime = $scope.getTime = function(date){
		if (!date)
			date = new Date();
		else date = new Date(date);

		return date.toLocaleString('ru', 
			{
				year: 'numeric',
				month: 'long',
				day: 'numeric',
				hour: 'numeric',
				minute: 'numeric',
				second: 'numeric'
			});
	}

	setTime = function(){
		date = new Date();
		$scope.curTime = $scope.getTime();
	}
	setTime();
	$interval(setTime, 1000);
	

	$scope.updateOrders = function(){
		$http.get(SERVERIP + 'orders').then(function(response){
			var newItems = response.data.orders.filter(function(order){
				for (var i in $scope.orders)
					if ($scope.orders[i].id == order.id)
						return false;
				return true;
			});

			newItems = newItems.map(function(order){
				order.products = order.products.map(function(product){
					product.info = $scope.products.filter(function(product_info){
						return product_info.id == product.id;
					})[0];
					return product;
				});
				return order;
			});

			newItems = newItems.map(function(order){
				order.total = 0;
				order.products.map(function(product){
					order.total += product.info.cost * product.count;
				});
				return order;
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
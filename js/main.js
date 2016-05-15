app = angular.module('app', ['ui.bootstrap']);

var SERVERIP = 'http://10.55.33.11:8080/';

app.controller('mainCtrl', function($scope, $rootScope, $http, $timeout, $interval){

	$scope.loading = true;

	$scope.orders = $scope.products = [];

	$scope.filterMode = 'all';

	$scope.getFilter = function(order){
		//console.log($scope.filterMode);
		switch ($scope.filterMode){
			case 'all':
				return true;
			case 'incomplete':
				return order.completed == false;
			case 'complete':
				return order.completed == true;
		}
	}

	$rootScope.timeDiff = $scope.timeDiff = function(end){
		var begin = new Date(Date.now());
		var end = new Date(end.replace(' ', 'T'));
		var diff = end - begin;
		with (Math){
			diff = floor(diff / 1000);
			var hours = floor(diff / 60 / 60);
			var minutes = floor((diff % 3600) / 60);
			var seconds = diff % 60;
			var result = '' + floor(hours / 10) + (hours % 10) + ':' + floor(minutes / 10) + (minutes % 10) + ':' + floor(seconds / 10) + (seconds % 10);
		}
		return result;
	}

	$rootScope.getTime = $scope.getTime = function(date){
		if (!date)
			date = new Date();
		else date = new Date(date.replace(' ', 'T'));

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
				order.total = 0;
				order.completed = false;
				order.products = order.products.map(function(product){
					product.info = $scope.products.filter(function(product_info){
						return product_info.id == product.id;
					})[0];
					order.total += product.info.cost * product.count;
					return product;
				});
				return order;
			});

			$timeout(function(){
				for (var i in newItems){
					$scope.orders.push(newItems[i]);
				}
				$scope.orders = $scope.orders.filter(function(order){
					for (var i in response.data.orders)
						if (response.data.orders[i].id == order.id)
							return true;
					return false;
				});
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
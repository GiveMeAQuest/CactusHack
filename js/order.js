app.directive('order', function($interval, $uibModal){

	link = function(scope, el, attrs){

		scope.expired = false;

		el.click(function(){
			var modal = $uibModal.open({
				templateUrl: 'modal.html',
				size: 'sm',
				resolve: {
					order: scope.order
				},
				controller: function($scope, $uibModalInstance, order){
					$scope.order = order;
					$scope.close = function(){
						$uibModalInstance.close();
					}
				}
			});
		});

		checkExpired = function(){
			scope.timeLeft = scope.$root.timeDiff(scope.order.prepare_time);
			if (new Date(scope.timeLeft) < new Date(Date.now()))
				scope.expired = true;
		}

		checkExpired();
		$interval(checkExpired, 1000);

		el.css({
			cursor: 'pointer'
		});
	}

	return {
		restrict: 'A',
		link: link,
		scope: {
			order: '=data'
		},
		templateUrl: 'order.html'
	}
});
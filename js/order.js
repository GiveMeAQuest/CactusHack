app.directive('order', function($interval){

	link = function(scope, el, attrs){

		scope.showDetails = scope.expired = false;

		checkExpired = function(){
			scope.timeLeft = scope.$root.timeDiff(scope.order.prepare_time);
			if (new Date(scope.timeLeft) < new Date(Date.now()))
				scope.expired = true;
		}

		checkExpired();
		$interval(checkExpired, 1000);

		el.css({
			cursor: 'pointer'
		})

		body = el.find('.order-body');

		body.css({
			display: 'none'
		});

		(function(body){
			scope.$watch('showDetails', function(to, from){
				if (from != to)
					body.slideToggle(300);
			});
		})(body);
	}

	return {
		restrict: 'A',
		replace: true,
		link: link,
		scope: {
			order: '=data',
			timeDiff: '=timeDiff'
		},
		templateUrl: 'order.html'
	}
});
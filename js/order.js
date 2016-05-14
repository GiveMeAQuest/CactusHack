app.directive('order', function(){

	link = function(scope, el, attrs){

		scope.showDetails = false;

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
					body.slideToggle();
			});
		})(body);
	}

	return {
		restrict: 'A',
		replace: true,
		link: link,
		scope: {
			order: '=data'
		},
		templateUrl: 'order.html'
	}
});
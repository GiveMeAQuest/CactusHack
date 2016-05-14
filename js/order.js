app.directive('order', function(){
	return {
		restrict: 'A',
		replace: true,
		scope: {
			order: '=data'
		},
		templateUrl: 'order.html'
	}
});
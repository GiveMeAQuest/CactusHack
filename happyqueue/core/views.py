import datetime

from django.conf import settings
from django.shortcuts import render
from django.contrib.staticfiles.templatetags.staticfiles import static
from django.utils.decorators import method_decorator
from django.views.generic import TemplateView, UpdateView
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt


class IndexView(TemplateView):
    template_name = 'base.html'


class OrdersView(UpdateView):

    orders = {'orders': [{
        'id': 1,
        'order_time': datetime.time(1, 2, 3),
        'prepare_time': datetime.time(2, 3, 3),
        'products': [
            {'id': 1,
             'count': 3}
        ]
    }, {
        'id': 2,
        'order_time': datetime.time(5, 2, 3),
        'prepare_time': datetime.time(5, 3, 3),
        'products': [
            {'id': 2,
             'count': 1}
        ]
    }]}

    @method_decorator(csrf_exempt)
    def dispatch(self, *args, **kwargs):
        return super(OrdersView, self).dispatch(*args, **kwargs)

    def get(self, request, *args, **kwargs):
        return JsonResponse(self.orders, safe=False)

    def post(self, request, *args, **kwargs):
        return HttpResponse()


class ProductsView(TemplateView):

    products = {'products': [
                {'id': 1, 'cost': 100, 'name': 'Hamburger', 'image': settings.URL + static('images/1.jpg'),
                'height': 2880, 
                'width': 1800}, 
                {'id': 2, 'cost': 200, 'name':'Hot-Dog', 'image': settings.URL + static('images/2.jpg'),
                'height': 554, 
                'width': 365},
                {'id': 3, 'cost': 12, 'name':'Double Hamburger', 'image': settings.URL + static('images/3.jpg'),
                'height': 924, 
                'width': 693},
                {'id': 4, 'cost': 20, 'name':'Pancakes', 'image': settings.URL + static('images/4.jpg'),
                'height': 570, 
                'width': 400},
                {'id': 5, 'cost': 5, 'name':'Chicken Chop', 'image': settings.URL + static('images/5.jpg'),
                'height': 540, 
                'width': 360}
            ]
        }

    def get(self, request, *args, **kwargs):
        return JsonResponse(self.products, safe=False)


users = {'users': []}


class LoginView(TemplateView):

    def get(self, request, *args, **kwargs):
        current_user = {'id': len(users['users']) + 1, 'name': kwargs['name']}
        users['users'].append(current_user)
        return JsonResponse(current_user, safe=False)
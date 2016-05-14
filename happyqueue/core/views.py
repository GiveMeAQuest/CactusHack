import datetime

from django.conf import settings
from django.shortcuts import render
from django.contrib.staticfiles.templatetags.staticfiles import static

from django.views.generic import TemplateView
from django.http import JsonResponse, HttpResponse


class IndexView(TemplateView):
    template_name = 'base.html'


class OrdersView(TemplateView):

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

    def get(self, request, *args, **kwargs):
        return JsonResponse(self.orders, safe=False)


class ProductsView(TemplateView):

    products = {'products': [
                {'id': 1, 'cost': 100, 'name': 'ham', 'image': settings.URL + static('images/1.jpg')}, 
                {'id': 2, 'cost': 200, 'name':'pasta', 'image': settings.URL + static('images/2.jpg')}
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
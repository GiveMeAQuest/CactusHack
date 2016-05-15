import datetime
import os

from django.conf import settings
from django.shortcuts import render
from django.contrib.staticfiles.templatetags.staticfiles import static
from django.utils.decorators import method_decorator
from django.views.generic import TemplateView, UpdateView
from django.http import JsonResponse, HttpResponse
from django.views.decorators.csrf import csrf_exempt


orders = {'orders': []}

users = {'users': []}


class IndexView(TemplateView):
    template_name = 'base.html'


class OrdersView(UpdateView):
    @method_decorator(csrf_exempt)
    def dispatch(self, *args, **kwargs):
        return super(OrdersView, self).dispatch(*args, **kwargs)

    def get(self, request, *args, **kwargs):
        return JsonResponse(orders, safe=False)

    def post(self, request, *args, **kwargs):
        prod_ids = map(int, request.POST.get('id').strip().split(' '))
        count = map(int, request.POST.get('count').strip().split(' '))
        prod_info = zip(prod_ids, count)
        current_order = {}
        current_order['id'] = len(orders['orders']) + 1
        current_order['order_time'] = datetime.datetime.utcnow()
        time = request.POST.get('prepare_time').split(':')
        dtime = datetime.time(int(time[0]), int(time[1]))
        datetime_cur = datetime.datetime.combine(datetime.datetime.now().date(), dtime)
        current_order['prepare_time'] = datetime_cur.strftime("%Y-%m-%d %H:%M:%S")
        current_order['access_token'] = os.urandom(4).encode('hex')
        products = []
        for i in prod_info:
            d = {}
            d['id'] = i[0]
            d['count'] = i[1]
            products.append(d)
        current_order['products'] = products
        current_order['user'] = request.POST.get('username')[0]
        orders['orders'].append(current_order)
        return JsonResponse(current_order, safe=False)


class ProductsView(TemplateView):

    products = {'products': [
        {'id': 1,
         'cost': 100,
         'name': 'Hamburger',
         'image': settings.URL + static('images/1.png'),
         'height': 2880,
         'width': 1800}, {'id': 2,
                          'cost': 200,
                          'name': 'Hot-Dog',
                          'image': settings.URL + static('images/2.jpg'),
                          'height': 554,
                          'width': 365},
        {'id': 3,
         'cost': 12,
         'name': 'Double Hamburger',
         'image': settings.URL + static('images/3.jpg'),
         'height': 924,
         'width': 693}, {'id': 4,
                         'cost': 20,
                         'name': 'Pancakes',
                         'image': settings.URL + static('images/4.jpg'),
                         'height': 570,
                         'width': 400},
        {'id': 5,
         'cost': 5,
         'name': 'Chicken Chop',
         'image': settings.URL + static('images/5.jpg'),
         'height': 540,
         'width': 360}
    ]}

    def get(self, request, *args, **kwargs):
        return JsonResponse(self.products, safe=False)


class LoginView(TemplateView):
    def get(self, request, *args, **kwargs):
        current_user = {'id': len(users['users']) + 1, 'name': kwargs['name'],
        }
        users['users'].append(current_user)
        return JsonResponse(current_user, safe=False)
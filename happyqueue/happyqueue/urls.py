from django.conf.urls import patterns, include, url
from django.conf import settings

from django.contrib import admin

from core.views import IndexView, OrdersView, ProductsView, LoginView
admin.autodiscover()

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'happyqueue.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^$', IndexView.as_view(), name='index'),
    url(r'^orders/', OrdersView.as_view(), name='orders'),
    url(r'^products/', ProductsView.as_view(), name='products'),
    url(r'^login/(?P<name>.+)/$', LoginView.as_view(), name='login'),
)

if settings.DEBUG:
    # static files (images, css, javascript, etc.)
    urlpatterns += patterns('',
        (r'^media/(?P<path>.*)$', 'django.views.static.serve', {
        'document_root': settings.MEDIA_ROOT}))
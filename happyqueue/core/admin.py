from django.contrib import admin
from core.models import Product


class ProductAdmin(admin.ModelAdmin):
    list_display = ['name', 'cost', 'image',]
    list_filter = ('name', )


admin.site.register(Product, ProductAdmin)
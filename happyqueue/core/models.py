from __future__ import unicode_literals

from django.db import models


class Product(models.Model):
    name = models.CharField(max_length=30, default=None)
    cost = models.IntegerField(default=0)
    image= models.ImageField(upload_to='product_image')

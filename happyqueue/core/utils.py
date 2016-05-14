import urllib, cStringIO
from PIL import Image


def get_img_dimensions(url):
    file = cStringIO.StringIO(urllib.urlopen(url).read())
    im = Image.open(file)
    width, height = im.size
    if link.has_key('height'):
        height = link['height']  # set height if site modifies it
    if link.has_key('width'):
        width = link['width']  # set width if site modifies it
    return width, height
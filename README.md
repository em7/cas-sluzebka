# cas-sluzebka

Program for counting trip time inside/outside business hours. This utility can be useful if you travel for business trips and travel time is sometimes inside and sometimes outside your business hours. Since these times need to be invoiced separately, you need to count them.

This utility is currently available only in Czech language.  
![Windows screenshot](https://raw.githubusercontent.com/em7/cas-sluzebka/master/doc/windows-screen-1.0.0.png)
![Linux screenshot](https://raw.githubusercontent.com/em7/cas-sluzebka/master/doc/linux-screen-1.0.0.png)
![MacOS screenshot](https://raw.githubusercontent.com/em7/cas-sluzebka/master/doc/macosx-screen-1.0.0.png)

## Latest downloads

**NOTE** This program needs Java 8 or better to run. You can get it [e.g. from this site](http://java.com).

Latest version is [1.0.0](https://github.com/em7/cas-sluzebka/releases/tag/v1.0.0).

* [Windows 64 bit](https://github.com/em7/cas-sluzebka/releases/download/v1.0.0/cas-sluzebka-1.0.0-windows.zip)
* [Linux 64 bit](https://github.com/em7/cas-sluzebka/releases/download/v1.0.0/cas-sluzebka-1.0.0-linux-gtk.tar.gz)
* [MacOS 64 bit](https://github.com/em7/cas-sluzebka/releases/download/v1.0.0/cas-sluzebka-1.0.0-macosx.zip)

## How helpful it is?

3 times needs to be calculated for a typical business trip. Travel time outside business hours, travel time inside and time spent working.

From a scientific analysis which I just made up follows that average programmer spent 20 seconds counting the times. This can mean a lot for a mid-sized company which has 350 employees each travelling 6 times a yer.

```
Total business trips: 350 x 6 = 2,100 trips
Time spent calculating times by hand: 2,100 x 15s = 42,000s = 700m = 11.6h
Since employee is usually pissed off when doing paperwork, calculate it as well.
My smoking break usually takes 7 minutes:
Pissed off tax: (2,100 trips x 7m) + 11.6h = 14,700m + 11.6h = 245h = 256.6h

Time spent calculating times by utility: 2,100 x 5s = 10,500s = 175m = 3h
Time savings: 253,6h

Average monthly salary in Czech Republic (Prague, 3rd quarter 2017): CZK 36,312
Employer cost for gross salary of CZK 36,312: CZK 48,659
Since the employee can't do something useful, calculate total cost. Approx. salary cost x 3:
CZK 48,659 x 3 = CZK 145,977.
Month has usually 160h in CZ (40h working week): CZK 912 / hour

Total year savings: time * hour cost = 253,6 * CZK 912 = CZK 231,283.
So, average sized company save around $12,300 per year using this software.
```

## Compiling

See documentation in `doc` directory.

## License

EPL, see LICENSE file. Don't forget to buy me a beer if you like the software.

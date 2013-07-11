$ sudo apt-get install libimage-exiftool-perl


You can retrieve the existing orientation information via exiftool as follows:

$ exiftool -Orientation -n image.jpg

This will display the internal value of the orientation information held in the MIE tags. You can return the value as an English string by omitting the -n flag. You can find additional information here regarding particular rotation/orientation values.

Changing the orientation data with exiftool can be done as follows:

$ exiftool -Orientation=1 -n image.jpg

Here, the orientation is set to 1, indicating no rotation. These numbers are defined as per the EXIF specification; you can see what effect different rotation values have in the link above.

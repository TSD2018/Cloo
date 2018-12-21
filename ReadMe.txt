Readme For CLoo

Edited by Ashwin

0.01 Dec 17, 2018 Kartik Venkataraman: First revision from the pre-prototype
    Included an object (singleton class) called CurrentLocation which stores
    the latlng once captured, and passes that on to all required classes.
    LocationAdaptor is modified to compute the distance (as the crow flies)
    between the toilet location and users current location.  Also the Rating
    is now shown as stars in the landing page instead of a number.
    -*-

0.02 Dec 17, 2018 Kartik Venkataraman
    Issues to be addressed
    a) The app crashes the first time it runs
    b) CurrentLocation Object is not updated when the location is refreshed
       from other activities.  Might have to include this code there and/or
       make those activites use this object value.


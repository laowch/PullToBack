Android PullToBack effect like Inbox 
==========

This project implements a PullToBack effect like Inbox (A mail app by Google).

## Included Features

- Supports both Pulling Down from the top, and Pulling Up from the bottom.
- Animated Scrolling for all devices.
- Currently works with:
    * **ListView**
    * **ScrollView**
    * **A form with EditText (You can assgin EditText as scroll child with the method PullToBackLayout.setScrollChild().)**
    * **Any other view can be scroll or not, in theory.(I dont test it.)**


## Known Issus

- To avoid misoperation, when you scroll the child view to the border and continue scroll, it should be stucked if you don't Up your finger. 
- There's some conflicts with ViewPager.








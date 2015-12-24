MyJaPhoO

My Java Photo Organizer

MyJaPhoO is an application to organize large photo and/or video collections.

Requirements

This software requires Java Runtime Version 1.6 or higher to be installed on your system.
The software can not handle video media itself and uses therefore some other programs:

- VLC (to generate thumbs (for windows platform) and for playback of videos)
for playback optionally mplayer, kplayer
- ffmpegthumbnailer: to generate thumbnails for videos (for linux platforms)
This software is not bundled with myjaphoo, you have to properly install this programs on your own. Please check the paths of this programs in the options dialog of myjaphoo for correctness.

Features:

    can handle very large sets of media files
    data gets saved in a relational database (Derby database). therefore its possible to access and use SQL
    generation fo thumbnails
    simple integrated picture viewer. Movies are viewed via external player (e.g. VLC).
    files can be tagged
    Tags can be structured in trees
    Tags could be “tagged” by so called metatags. Means its possible to put on a “orthogonal” tag structure.
    Comments, Titles and Ratings could be added
    Filterfunction (special Filtersyntax to filter by attributes)
    Group By functions to produce grouped views of  the material (by predefined attributes or by user defined groupings defined via a Grouping syntax). Multiple groupings are possible.
    Search of duplicates
        Duplikates are identified by a checksum  (CRC32).
        Group By View to display the duplicates
        Easy deletion of duplicates
        View filter for duplicates (to remove the duplicates just in the view)
        View filter for duplicates which are created by groupings : (due to multiple assignments to different groups).
    Comparison function between different myjaphoo databases (mainly to find out duplicates)
    GUI Layout can be adjusted and saved in so called Perspectives
    creation of Bookmarks (consist of Filter and group by)
    Undo-Funktion for all essential functions
    Changelog for all essential functions
    History Funktion to get back to previous views (and filters and groupings)
    function to copy media files
    export function to import meta data in another myjaphoo database
    Scripting in Groovy is possible. Macros can be added as UI commands to menus
    Three different Thumb Views:
        in general the UI is combined by a tree view which contains the media files for navigating  and a  Detail View which shows Thumbs
        Thumb-Listen Modus: shows the thumbs with all atributes assigned to a media file
        regular Thumb View: shows thumbs in a grid
        Stripe Thumb Mode: shows a stripe for each subcategorisation of the current selected node in the media tree; Stripes can be opened to show all thumbs of that stripe; all thumbs of a stripe can be selected via double click on the first column
        you can open multiple UI Main Views which all have different Filters, Group By definitions and views on the data.


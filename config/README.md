# About the config directory

The config directory is used to store your game configuration files. You can structure this any way you like with any subdirectory structure that you think is appropriate. One thing you *must not do* is create a **master** subfolder to this folder that has any content. The **master** subfolder will be used for testing your submissions. When we are grading your submissions we will remove any **master** subdirectory and replace it with our version required for the tests.

A recommended structure for this folder is:

<pre>
config (this folder) <br>
    |- config/egc (files and subfolders containing Escape Game Configuration files) <br>
    |- config/xml (files and subfolders that have the same structure as the **egc** folder, but have the XML generate configurations
</pre>
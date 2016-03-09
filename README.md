# Binder-Plugin-Intellij
This is a plugin for Android Studio and IntelliJ that connects to the Binder Library built at the High Bay. This plugin makes Binder even easier to use!

# Binder Plugin
To use.
Download Plugin.
Click Binder in Menu Bar.
Click Download Binder.
Give base address https://yoursite.com/
Watch Magically All of you files are donwloaded and linked in your project. 
Start using binder.


# What it does
Downloads and Extracts files to a Binder folder.
to base of app. ( in the main src ) 
Binder.Data.Dtos => Where all of the DTOs are.
Binder.Libraries => Where the DataSources Enums are.
Binder => Where the Webservice,DataInterface,TestData,and DataSingleton are.

Checks for Application File in Manifest. ( Adds if not there or insturcts you how to link to the DataSingleton if the Application File is available. )

extras.
adds all imports to all of the files.
adds url to the Webservice class.

#Warning Redownloading will overwrite any changes you have made to the binder files.

#### This is a proof of concept and has been throughly tested but still may have its quirks.
#License

Copyright (c) 2016 The Board of Trustees of The University of Alabama All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
Neither the name of the University nor the names of the contributors may be used to endorse or promote products derived from this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.


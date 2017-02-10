:: GPL Ghostscript 9.07 (2013-02-14)
:: Copyright (C) 2012 Artifex Software, Inc.  All rights reserved.
:: Usage: gs [switches] [file1.ps file2.ps ...]
:: Most frequently used switches: (you can use # in place of =)
::  -dNOPAUSE           no pause after page   | -q       `quiet', fewer messages
::  -g<width>x<height>  page size in pixels   | -r<res>  pixels/inch resolution
::  -sDEVICE=<devname>  select device         | -dBATCH  exit after last file
::  -sOutputFile=<file> select output file: - for stdout, |command for pipe,
::                                          embed %d or %ld for page #
:: Input formats: PostScript PostScriptLevel1 PostScriptLevel2 PostScriptLevel3 PDF
:: Default output device: display
:: Available devices:
::    bbox bit bitcmyk bitrgb bj10e bj200 bjc600 bjc800 bmp16 bmp16m bmp256
::    bmp32b bmpgray bmpmono bmpsep1 bmpsep8 cdeskjet cdj550 cdjcolor cdjmono
::    cp50 declj250 deskjet devicen display djet500 djet500c eps9high eps9mid
::    epson epsonc epswrite ibmpro ijs inkcov jetp3852 jpeg jpegcmyk jpeggray
::    laserjet lbp8 lj250 ljet2p ljet3 ljet3d ljet4 ljet4d ljetplus m8510
::    mswindll mswinpr2 necp6 nullpage pamcmyk32 pamcmyk4 pbm pbmraw pcx16
::    pcx24b pcx256 pcxcmyk pcxgray pcxmono pdfwrite pgm pgmraw pgnm pgnmraw pj
::    pjxl pjxl300 pkmraw plan planc plang plank planm plib plibc plibg plibk
::    plibm png16 png16m png256 pngalpha pnggray pngmono pngmonod pnm pnmraw
::    ppm ppmraw ps2write psdcmyk psdrgb psmono pswrite pxlcolor pxlmono r4081
::    spotcmyk st800 stcolor svg t4693d2 t4693d4 t4693d8 tek4696 tiff12nc
::    tiff24nc tiff32nc tiff48nc tiff64nc tiffcrle tiffg3 tiffg32d tiffg4
::    tiffgray tifflzw tiffpack tiffscaled tiffscaled24 tiffscaled4 tiffscaled8
::    tiffsep tiffsep1 txtwrite uniprint
:: Search path:
::    C:\Program Files\gs\gs9.07\bin ; C:\Program Files\gs\gs9.07\lib ;
::    C:\Program Files\gs\gs9.07\fonts ; %rom%Resource/Init/ ; %rom%lib/ ;
::    c:/gs/gs9.07/Resource/Init ; c:/gs/gs9.07/lib ;
::    c:/gs/gs9.07/Resource/Font ; c:/gs/fonts
:: Initialization files are compiled into the executable.
:: For more information, see c:/gs/gs9.07/doc/Use.htm.
:: Please report bugs to bugs.ghostscript.com.
:: 
:: 
:: gswin64c -stxtfile=junk.txt
gswin64c -dBATCH -dNOPAUSE -dSAFER -dDELAYBIND -dWRITESYSTEMDICT -dSIMPLE -sDEVICE=txtwrite -dTextFormat=2 -dFirstPage=1 -dLastPage=1 -sOutputFile=C:\out.txt C:\in.pdf
## PDFRpt

I built this library because I didn't think the popular [iText PDF library](https://itextpdf.com/) was very convenient for my use case, which is general purpose report generation. On the other hand, most reporting tools are worse, forcing one to rely on XML/XSL, fussy GUI's, awkward configuration systems and so forth.

PDFRpt acts as a "wrapper" API for iText, and requires no specific knowledge of iText on the part of the programmer, while permitting one to "drill down" into iText APIs and make use of more advanced capabilities (there are many).

Some of the good things include:
* Lightweight, as in only 14 classes (not including iText, of course)
* Fast, as in nanoseconds, not seconds (thanks in part to iText, of course)
* Relative positioning and alignment
* Automated handling of page overflow and numbering
* Automatic text wrapping
* Common graphical elements including geometric shapes, embedded images, fonts & colors
* No annotations, reflection, byte-code rewriting, configuration files, native calls or other cruft necessary
* Detailed javadocs & examples

### Documentation:
[Javadoc is here](https://zaboople.github.io/javadoc/pdfrpt/overview-summary.html)

[These tests](https://github.com/zaboople/pdfrpt/tree/master/java/org/tmotte/pdfrpt/test) double as pretty good examples for usage.

### Download:
[Download jar file binaries here](https://zaboople.github.io/downloads/pdfrpt.1.0.1.zip). The download includes a copy of the iText 5 jar file & javadocs.

### Other notes:

PDFRpt is designed to work with the "old" version 5 of iText; the current version is 7. I've chosen to stick with the old version for now because:
  - It's stable
  - Version 7 introduces a complex arrangement of dependencies - including external ones - to the point that it is assumed I will use Maven (or at least Gradle) to handle everything, and by extension anything that uses PDFRpt.
  - Version 7 is not backwards compatible; requiring me to spend a good deal of time on the upgrade. And I'm kind of lazy.

Nonetheless it will eventually be necessary to bite the bullet.

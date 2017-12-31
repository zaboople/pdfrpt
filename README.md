## PDFRpt

I built this library because I didn't think the popular iText library was very convenient & straightforward for my use case, which is general purpose report generation. On the other hand, most reporting tools are even worse, forcing one to rely on XML/XSL, WYSIWYG GUI's, awkward configuration systems and so forth.

PDFRpt acts as a "wrapper" API for iText, and requires no specific knowledge of iText on the part of the programmer. However, it still allows one to drill down to the underlying iText core to take advantage of advanced capabilities. Features:

* Fast, efficient & lightweight - only 14 classes (not including iText)
* Relative positioning with precise alignment down to the pixel
* Automated handling of page overflow and numbering
* Automatic text wrapping
* Essential graphics including geometric shapes, embedded images, fonts & colors
* No annotations, reflection, byte-code rewriting, configuration files, native calls or other cruft necessary.
* Includes detailed javadocs & examples

### Documentation:
[Javadoc is here](https://zaboople.github.io/javadoc/pdfrpt/overview-summary.html)

[These tests](https://github.com/zaboople/pdfrpt/tree/master/java/org/tmotte/pdfrpt/test/report) provide some easy-to-understand, each-stands-on-its-own examples of usage.

### Download:
[Download jar file binaries here](https://zaboople.github.io/downloads/pdfrpt.1.0.1.zip). The download includes a copy of the iText 5 jar file & javadocs.

### Other notes:
PDFRpt is designed to work with the "old" version 5 of iText; the current version is 7. I've chosen to stick with the old version (for now) because:
  - It's stable and unlikely to change
  - Version 7 introduces an (arguably typical) morass of dependencies, including external ones, to the point that it is assumed you will use Maven (or at least Gradle) to handle everything. I prefer to not back myself into that corner.
  - Version 7 rewrites the entire API and requires me to spend a good deal of time on the upgrade. I'm procrastinating for now.

Nonetheless it will eventually be necessary to bite the bullet.
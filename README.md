## PDFRpt

I built this library because I didn't think iText - a Java PDF generation library - was very concise & straightforward for my use case. PDFRpt acts as a "wrapper" API for iText while still allowing one to use the underlying iText core as necessary. Features:

* Relative positioning (this is 3px to the right/left/top/bottom of that)
* Page numbering
* Handling of page overflow & text wrapping
* Basic graphics, fonts, colors, etc.
* Precise control & alignment, down to the pixel

### Documentation:
[Javadoc is here](https://zaboople.github.io/javadoc/pdfrpt/overview-summary.html)

[These tests](https://github.com/zaboople/pdfrpt/tree/master/java/org/tmotte/pdfrpt/test/report) provide some easy-to-understand examples of basic usage.

### Download:
[Download jar file binaries here](https://zaboople.github.io/downloads/pdfrpt.1.0.1.zip). The download includes a copy of the iText 5 jar file & javadocs.

### Other notes:
PDFRpt is designed to work with the "old" version 5 of iText; the current version is 7. I've chosen to stick with the old version (for now) because:
  - It's stable and unlikely to change
  - Version 7 introduces an (arguably typical) morass of dependencies, including external ones, to the point that it is assumed you will use Maven (or at least Gradle) to handle everything. I prefer to not back myself into that corner.
  - Version 7 rewrites the entire API and requires me to spend a good deal of time on the upgrade. I'm procrastinating for now.

Nonetheless it will eventually be necessary to bite the bullet.
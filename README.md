## PDFRpt
FIXME closeable????
FIXME make single-test test output
FIXME docs claim version 1.4 or something stupid like that
FIXME rebuild javadocs and distro
FIXME include the old pdfrpt javadocs zip;
FIXME do footerPrintAndClose everywhere

I built this library because I didn't think iText - a Java PDF generation library - was very easy to use. PDFRpt acts as a "wrapper" API for iText, making the parts that should be concise & straightforward as they ought to be, while still allowing one to use the underlying iText core as necessary. Features:

* Relative positioning (this is 3px to the right/left/top/bottom of that)
* Page numbering
* Handling of page overflow & text wrapping
* Basic graphics, fonts, colors, etc.
* Precise control down to the pixel

### Documentation:
[Javadoc is here](https://zaboople.github.io/javadoc/pdfrpt/overview-summary.html)

[These tests](https://github.com/zaboople/pdfrpt/tree/master/java/org/tmotte/pdfrpt/test/report) provide some easy-to-understand examples of basic usage.

### Download:
[Download jar file binaries here](https://zaboople.github.io/downloads/pdfrpt.1.0.1.zip). The download includes a copy of the iText 5 jar file & javadocs.

### Other notes:
PDFRpt is designed to work with the "old" version 5 of iText; the current version is 7. I've chosen to stick with the old version (for now) because
  - It's stable and unlikely to change
  - Version 7 introduces a morass of dependencies, including external ones, to the point that it is assumed you will use Maven (or at least Gradle) to handle the whole mess. This is a problem because:
      - Ant gives me more control and flexiblity than Maven & Gradle without having to think backwards & upside down to get there.
      - I don't like the idea of introducing unknown dependencies into my builds. I only trust what I've specifically examined and intentionally downloaded.
      - I especially don't like the idea of expecting others to hold themselves to a lower standard.
  - Version 7 rewrites the entire API and requires me to spend a good deal of time on the upgrade. In other words, I'm lazy.

Nonetheless it will eventually be necessary to bite the bullet.
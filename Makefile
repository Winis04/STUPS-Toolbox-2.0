JC = javac
JDOC = javadoc
JSOURCES = $(shell find $(SOURCEDIR) -name *.java)
JOUTPUTPATH = build/
JCLASSPATH_UNIX = $(JOUTPUTPATH):$(JLIBS_UNIX)
JCLASSPATH_WINDOWS = $(JOUTPUTPATH);$(JLIBS_WINDOWS)
JLIBS_UNIX = $(shell find $(LIBDIR) -name "*.jar" | tr "\n" ":")
JLIBS_WINDOWS = $(shell find $(LIBDIR) -name "*.jar" | tr "\n" ";")
JMAINCLASS = bla.GUI

JAVA = java
SCCURL = https://jcenter.bintray.com/de/hhu/stups/sablecc/3.2.10/sablecc-3.2.10.jar
SCCSOURCES = $(shell find $(SOURCEDIR) -name *.scc)
SCCOUTPUT = $(shell find $(find src/ -name '*.scc' | sed -e "s/.scc/\//") -type d -name 'analysis' -or -name 'lexer' -or -name 'node' -or -name 'parser')

LATEX = pdflatex

SOURCEDIR = src/
LIBDIR = lib/
TEXDIR = tex/
DOCDIR = doc/
ZIP_NAME = toolbox.zip
UNIX_EXECUTABLE = toolbox.sh
WINDOWS_EXECUTABLE = toolbox.bat

default: package

all: package doc text presentation

package: build
	echo "$(JAVA) -cp $(JCLASSPATH_UNIX) $(JMAINCLASS)" > $(UNIX_EXECUTABLE)
	chmod +x $(UNIX_EXECUTABLE)
	echo "$(JAVA) -cp $(JCLASSPATH_WINDOWS) $(JMAINCLASS)" > $(WINDOWS_EXECUTABLE)
	zip -r $(ZIP_NAME) $(UNIX_EXECUTABLE) $(WINDOWS_EXECUTABLE) $(JOUTPUTPATH) $(LIBDIR)

unix: build
	echo "$(JAVA) -cp $(JCLASSPATH_UNIX) $(JMAINCLASS)" > $(UNIX_EXECUTABLE)
	chmod +x $(UNIX_EXECUTABLE)

windows: build
	echo "$(JAVA) -cp $(JCLASSPATH_WINDOWS) $(JMAINCLASS)" > $(WINDOWS_EXECUTABLE)

build: download_dependencies parsers
	$(JC) -d $(JOUTPUTPATH) -cp $(JCLASSPATH_UNIX) $(JSOURCES)

develope: download_dependencies
	$(JAVA) -jar sablecc.jar $(SCCSOURCES)

clean_develope:
	if [ -d de/ ]; then rm -r de/; fi
	rm -r $(SCCOUTPUT)

download_dependencies:
	if [ ! -f sablecc.jar ]; then wget -O sablecc.jar $(SCCURL); fi

parsers:
	if [ ! -d $(JOUTPUTPATH) ]; then mkdir -p $(JOUTPUTPATH); fi
	$(JAVA) -jar sablecc.jar $(SCCSOURCES) -d $(JOUTPUTPATH)

mrproper: clean
	if [ -f sablecc.jar ]; then rm sablecc.jar; fi
	
clean:
	if [ -d $(JOUTPUTPATH) ]; then rm -r $(JOUTPUTPATH); fi
	if [ -d $(DOCDIR) ]; then rm -r $(DOCDIR); fi
	if [ -f $(UNIX_EXECUTABLE) ]; then rm $(UNIX_EXECUTABLE); fi
	if [ -f $(WINDOWS_EXECUTABLE) ]; then rm $(WINDOWS_EXECUTABLE); fi
	if [ -f $(ZIP_NAME) ]; then rm $(ZIP_NAME); fi
	if [ -f $(TEXDIR)text/ba.aux ]; then rm $(TEXDIR)text/ba.aux; fi
	if [ -f $(TEXDIR)text/ba.bbl ]; then rm $(TEXDIR)text/ba.bbl; fi
	if [ -f $(TEXDIR)text/ba.blg ]; then rm $(TEXDIR)text/ba.blg; fi
	if [ -f $(TEXDIR)text/ba.lof ]; then rm $(TEXDIR)text/ba.lof; fi
	if [ -f $(TEXDIR)text/ba.log ]; then rm $(TEXDIR)text/ba.log; fi
	if [ -f $(TEXDIR)text/ba.lol ]; then rm $(TEXDIR)text/ba.lol; fi
	if [ -f $(TEXDIR)text/ba.lot ]; then rm $(TEXDIR)text/ba.lot; fi
	if [ -f $(TEXDIR)text/ba.out ]; then rm $(TEXDIR)text/ba.out; fi
	if [ -f $(TEXDIR)text/ba.toc ]; then rm $(TEXDIR)text/ba.toc; fi
	if [ -f ba.pdf ]; then rm ba.pdf; fi
	if [ -f $(TEXDIR)presentation/master.log ]; then rm $(TEXDIR)presentation/master.log; fi
	if [ -f $(TEXDIR)presentation/textput.log ]; then rm $(TEXDIR)presentation/textput.log; fi
	if [ -f $(TEXDIR)presentation/master.toc ]; then rm $(TEXDIR)presentation/master.toc; fi
	if [ -f $(TEXDIR)presentation/master.aux ]; then rm $(TEXDIR)presentation/master.aux; fi
	if [ -f $(TEXDIR)presentation/master.nav ]; then rm $(TEXDIR)presentation/master.nav; fi
	if [ -f $(TEXDIR)presentation/master.out ]; then rm $(TEXDIR)presentation/master.out; fi
	if [ -f $(TEXDIR)presentation/master.pdfpc ]; then rm $(TEXDIR)presentation/master.pdfpc; fi
	if [ -f $(TEXDIR)presentation/master.snm ]; then rm $(TEXDIR)presentation/master.snm; fi
	if [ -f $(TEXDIR)presentation/master.vrb ]; then rm $(TEXDIR)presentation/master.vrb; fi
	if [ -f presentation.pdf ]; then rm presentation.pdf; fi

doc:
	$(JDOC) -cp $(JCLASSPATH_UNIX) -d $(DOCDIR) $(JSOURCES)

text:
	cd $(TEXDIR)/text/; $(LATEX) ba.tex; bibtex ba; $(LATEX) ba.tex; $(LATEX) ba.tex; mv ba.pdf ../../ba.pdf; cd ../../
	
presentation:
	cd $(TEXDIR)/presentation/; $(LATEX) master.tex; $(LATEX) master.tex; mv master.pdf ../../presentation.pdf; cd ../../

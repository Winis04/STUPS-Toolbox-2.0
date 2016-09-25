JC = javac
JDOC = javadoc
JSOURCES = $(shell find $(SOURCEDIR) -name *.java)
JOUTPUTPATH = build/
JCLASSPATH_UNIX = $(JOUTPUTPATH):$(JLIBS_UNIX)
JCLASSPATH_WINDOWS = $(JOUTPUTPATH);$(JLIBS_WINDOWS)
JLIBS_UNIX = $(shell find $(LIBDIR) -name "*.jar" | tr "\n" ":")
JLIBS_WINDOWS = $(shell find $(LIBDIR) -name "*.jar" | tr "\n" ";")
JMAINCLASS = GUI

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

all: package doc latex

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
	if [ -f tex/ba.aux ]; then rm tex/ba.aux; fi
	if [ -f tex/ba.bbl ]; then rm tex/ba.bbl; fi
	if [ -f tex/ba.blg ]; then rm tex/ba.blg; fi
	if [ -f tex/ba.lof ]; then rm tex/ba.lof; fi
	if [ -f tex/ba.log ]; then rm tex/ba.log; fi
	if [ -f tex/ba.lol ]; then rm tex/ba.lol; fi
	if [ -f tex/ba.lot ]; then rm tex/ba.lot; fi
	if [ -f tex/ba.out ]; then rm tex/ba.out; fi
	if [ -f tex/ba.toc ]; then rm tex/ba.toc; fi
	if [ -f ba.pdf ]; then rm ba.pdf; fi

doc:
	$(JDOC) -cp $(JCLASSPATH_UNIX) -d $(DOCDIR) $(JSOURCES)

latex:
	cd $(TEXDIR); $(LATEX) ba.tex; bibtex ba; $(LATEX) ba.tex; $(LATEX) ba.tex; mv ba.pdf ../ba.pdf; cd ..

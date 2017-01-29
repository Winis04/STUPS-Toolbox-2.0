package GrammarSimulator;

import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * @author Isabel
 * @since 26.10.2016
 */
public class Matrix implements Printable{
    private final int rows;
    private final int columns;
    private final List<String> word;
    private final HashSet<Nonterminal>[][] matrix;
    private int spacing;
    public Matrix(int rows, int columns, List<String> word) {
        this.word=word;
        matrix=new HashSet[rows][columns];
        this.rows=rows;
        this.columns=columns;
        for(int j=0;j<matrix.length;j++) {
            for (int i = 0; i < matrix[j].length; i++) {
                matrix[j][i]=new HashSet<>();
            }
        }

        this.spacing=3;

    }
    public void addToCell(int c, int r, Nonterminal nt) {
        if(c > 0) {
            matrix[r][c].add(nt);
        }
        for (HashSet<Nonterminal>[] aMatrix : matrix) {
            for (HashSet<Nonterminal> anAMatrix : aMatrix) {
                int tmp = 0;
                for (Symbol s : anAMatrix) {
                    tmp += s.getName().length();
                }
                tmp += 2 * anAMatrix.size();
                if (tmp > spacing) {
                    this.spacing = tmp;
                }
            }
        }
    }

    @Override
    public void printConsole(BufferedWriter writer) {
        horizontalLine(writer);
        if(columns > 10) {
            Printer.print("|  i | ",writer);
        } else {
            Printer.print("| i | ", writer);
        }
        //print headline
        IntStream.range(1,columns).
                forEach(s ->  {
                    if(columns >= 10) {
                        if(s < 10) {
                            Printer.print(s, writer);
                            for (int k = 0; k < spacing - 3; k++) {
                                Printer.print(" ", writer);
                            }
                            Printer.print(" | ", writer);
                        } else {
                            Printer.print(s, writer);
                            for (int k = 0; k < spacing - 3; k++) {
                                Printer.print(" ", writer);
                            }
                            Printer.print("| ", writer);
                        }
                    } else {
                        Printer.print(s, writer);
                        for (int k = 0; k < spacing - 3; k++) {
                            Printer.print(" ", writer);
                        }
                        Printer.print(" | ", writer);
                    }
                });
        Printer.println("",writer);
        horizontalLine(writer);
        for(int j=rows-1;j>-1;j--) {
            if(columns > 10) {
                if(j >= 10) {
                    Printer.print("| "+j+" | ",writer);
                } else {
                    Printer.print("|  "+j+" | ",writer);
                }
            } else {
                Printer.print("| "+j+" | ",writer);
            }

            for(int i=1;i<columns;i++) {
                HashSet<Nonterminal> tmp=matrix[j][i];
                String cellContent=tmp.stream().map(Nonterminal::getName).collect(joining(", "));
                Printer.print(cellContent,writer);
                int fill=0;



                fill=spacing-cellContent.length()-2;

                for(int k=0;k< fill;k++) {
                    Printer.print(" ",writer);
                }
                Printer.print(" | ",writer);
            }
            Printer.println("",writer);
            horizontalLine(writer);
        }
        Printer.print("| j | ",writer);
        for (String aWord : word) {
            Printer.print(aWord, writer);
            for (int j = 0; j < spacing - 2; j++) {
                Printer.print(" ", writer);
            }
            Printer.print("| ", writer);
        }
       Printer.println("",writer);
        horizontalLine(writer);
    }


    private void horizontalLine(BufferedWriter writer) {
        if(columns >= 10) {
            Printer.print("+----", writer);
        } else {
            Printer.print("+---", writer);
        }
        for(int i=1;i<columns;i++) {
            Printer.print("+",writer);
            for(int j=0;j<this.spacing;j++) {
                Printer.print("-",writer);
            }
        }
        Printer.println("+",writer);

    }
    @Override
    public void printLatex(BufferedWriter writer, String space) {
      
        Printer.print(space+"\\begin{table}[h!]\n",writer);
        Printer.print(space+"\t\\centering\n",writer);
        Printer.print(space+"\t\\caption{CYK}\n",writer);
        String s="|";
        for(int i = 0; i<rows; i++) {
            s+="c|";
        }
        Printer.print(space+"\t\\begin{tabular}{"+s+"}\n",writer);
        //1 & 2 & 3\\
        //\hline
        Printer.print(space+"\t\t\\hline\n",writer);
        for(int r=rows-1;r>=0;r--) {
            Printer.print(space+"\t\t",writer);
            for(int c=1;c<columns;c++) {
                Printer.print(this.getCell(c,r).stream().map(nonterminal -> "$"+nonterminal.getName()+"$").collect(joining(", ")),writer);
                if(c<columns-1) {
                    Printer.print(" & ",writer);
                }
            }


            Printer.print("\\\\\n"+space+"\t\t\\hline\n",writer);
        }
        Printer.print(space+"\t\t\\hline\n",writer);
        Printer.print(space+"\t\t",writer);
        for(int i=0;i<word.size();i++) {
            Printer.print(word.get(i), writer);
            if(i<word.size()-1) {
                Printer.print(" & ",writer);
            }
        }
        Printer.print("\\\\\n"+space+"\t\t\\hline\n",writer);

        Printer.print(space+"\t\\end{tabular}\n",writer);
        Printer.print(space+"\\end{table}\n\n",writer);
    }


    public HashSet<Nonterminal> getCell(int c, int r) {
        return matrix[r][c];
    }
    public void clearCell(int c, int r) {
        matrix[r][c]=new HashSet<>();
    }

    public List<String> getWord() {
        return word;
    }

    public int getNumberOfRows() {
        return rows;
    }

    public int getNumberOfColumns() {
        return columns;
    }
}

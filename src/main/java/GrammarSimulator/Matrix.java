package GrammarSimulator;

import Print.Printable;
import Print.Printer;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

/**
 * this class describes the table that one can obtain by doing the cyk-algorithm.
 * @author Isabel
 * @since 26.10.2016
 */

public class Matrix implements Printable{

    private final int rows;

    private final int columns;

    private final List<String> word;


    private final HashMap<Integer,HashMap<Integer,HashSet<Nonterminal>>> matrix;


    private int spacing;

    /**
     * constructor
     * @param rows number of rows
     * @param columns number of columns
     * @param word the word belonging to this matrix
     */

    Matrix(int rows, int columns, List<String> word) {
        this.word=word;
        matrix = new HashMap<>();
        this.rows=rows;
        this.columns=columns;
        for(int j=0;j<rows;j++) {
            matrix.put(j, new HashMap<>());
            for(int i=0; i < columns;i++) {

                matrix.get(j).put(i,new HashSet<>());
            }
        }


        this.spacing=3;



    }

    /**
     * add a Nonterminal to a certain cell
     * @param c the column index of the cell
     * @param r the row index of the cell
     * @param nt the {@link Nonterminal} which should be added
     */

    public void addToCell(int c, int r, Nonterminal nt) {
        if(c > 0) {
            matrix.get(r).get(c).add(nt);
        }
        for(HashMap<Integer,HashSet<Nonterminal>> row : matrix.values()) {
            for(HashSet<Nonterminal> cell : row.values()) {
                int tmp=0;
                for(Nonterminal s : cell) {
                    tmp += s.getName().length();
                }
                tmp += 2* cell.size();
                if(tmp > spacing) {
                    this.spacing=tmp;
                }

            }
        }
    }


    @Override
    public void printConsole() {
        horizontalLine();
        if(columns > 10) {
            Printer.print("|  i | ");
        } else {
            Printer.print("| i | ");
        }
        //print headline
        IntStream.range(1,columns).
                forEach(s ->  {
                    if(columns >= 10) {
                        if(s < 10) {
                            Printer.print(Character.toString((char)s));
                            for (int k = 0; k < spacing - 3; k++) {
                                Printer.print(" ");
                            }
                            Printer.print(" | ");
                        } else {
                            Printer.print(Character.toString((char)s));
                            for (int k = 0; k < spacing - 3; k++) {
                                Printer.print(" ");
                            }
                            Printer.print("| ");
                        }
                    } else {
                        Printer.print(Character.toString((char)s));
                        for (int k = 0; k < spacing - 3; k++) {
                            Printer.print(" ");
                        }
                        Printer.print(" | ");
                    }
                });
        Printer.print("\n");
        horizontalLine();
        for(int j=rows-1;j>-1;j--) {
            if(columns > 10) {
                if(j >= 10) {
                    Printer.print("| "+j+" | ");
                } else {
                    Printer.print("|  "+j+" | ");
                }
            } else {
                Printer.print("| "+j+" | ");
            }

            for(int i=1;i<columns;i++) {
                HashSet<Nonterminal> tmp=matrix.get(j).get(i);
                String cellContent=tmp.stream().map(Nonterminal::getName).collect(joining(", "));
                Printer.print(cellContent);
                int fill;



                fill=spacing-cellContent.length()-2;

                for(int k=0;k< fill;k++) {
                    Printer.print(" ");
                }
                Printer.print(" | ");
            }
            Printer.print("\n");
            horizontalLine();
        }
        Printer.print("| j | ");
        for (String aWord : word) {
            Printer.print(aWord);
            for (int j = 0; j < spacing - 2; j++) {
                Printer.print(" ");
            }
            Printer.print("| ");
        }
       Printer.print("\n");
        horizontalLine();
    }



    private void horizontalLine() {
        if(columns >= 10) {
            Printer.print("+----");
        } else {
            Printer.print("+---");
        }
        for(int i=1;i<columns;i++) {
            Printer.print("+");
            for(int j=0;j<this.spacing;j++) {
                Printer.print("-");
            }
        }
        Printer.print("+\n");

    }

    @Override
    public void printLatex(String space) {
      
        Printer.print(space+"\\begin{table}[h!]\n");
        Printer.print(space+"\t\\centering\n");
        Printer.print(space+"\t\\caption{CYK}\n");
        String s="|";
        for(int i = 0; i<rows; i++) {
            s+="c|";
        }
        Printer.print(space+"\t\\begin{tabular}{"+s+"}\n");
        //1 & 2 & 3\\
        Printer.print(space+"\t\t\\hline\n");
        for(int r=rows-1;r>=0;r--) {
            Printer.print(space+"\t\t");
            for(int c=1;c<columns;c++) {
                Printer.print(this.getCell(c,r).stream().map(nonterminal -> "$"+nonterminal.getName()+"$").collect(joining(", ")));
                if(c<columns-1) {
                    Printer.print(" & ");
                }
            }


            Printer.print("\\\\\n"+space+"\t\t\\hline\n");
        }
        Printer.print(space+"\t\t\\hline\n");
        Printer.print(space+"\t\t");
        for(int i=0;i<word.size();i++) {
            Printer.print(word.get(i));
            if(i<word.size()-1) {
                Printer.print(" & ");
            }
        }
        Printer.print("\\\\\n"+space+"\t\t\\hline\n");

        Printer.print(space+"\t\\end{tabular}\n");
        Printer.print(space+"\\end{table}\n\n");
    }

    /**
     * Getter-Method for a cell of {@link #matrix}
     * @param c the column index
     * @param r the row index
     * @return a {@link HashSet} of {@link Nonterminal}s
     */

    public HashSet<Nonterminal> getCell(int c, int r) {
        return matrix.get(r).get(c);
    }

    /**
     * Getter-Method for {@link #word}, the word of this matrix
     * @return the {@link #word}
     */

    public List<String> getWord() {
        return word;
    }

    /**
     * Getter-method for {@link #rows}, the number of rows.
     * @return {@link #rows}
     */

    public int getNumberOfRows() {
        return rows;
    }

    /**
     * Getter-method for {@link #columns}, the number of columns.
     * @return {@link #columns}
     */

    public int getNumberOfColumns() {
        return columns;
    }
}

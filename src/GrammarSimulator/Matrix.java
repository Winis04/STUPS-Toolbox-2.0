package GrammarSimulator;

import GrammarSimulator.Nonterminal;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.*;

/**
 * Created by Isabel on 26.10.2016.
 */
public class Matrix {
    private int rows;
    private int columns;
    private String word;
    private HashSet<Nonterminal>[][] matrix;
    int spacing;
    public Matrix(int rows, int columns, String word) {
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
        for(int j=0;j<matrix.length;j++) {
            for (int i = 0; i < matrix[j].length; i++) {
                int tmp=0;
                for(Symbol s : matrix[j][i]) {
                    tmp+=s.getName().length();
                }
                tmp+=2*matrix[j][i].size();
                if(tmp>spacing) {
                    this.spacing=tmp;
                }
            }
        }
    }
    public void print() {
        horizontalLine();
        System.out.print("| i | ");
        IntStream.range(1,columns).
                forEach(s ->  {
                    System.out.print(s);
                    for(int k=0;k< spacing-3;k++) {
                        System.out.print(" ");
                    }
                    System.out.print(" | ");
                });
        System.out.println("");
        horizontalLine();
        for(int j=rows-1;j>-1;j--) {
            System.out.print("| "+j+" | ");
            for(int i=1;i<columns;i++) {
                HashSet<Nonterminal> tmp=matrix[j][i];
                String cellContent=tmp.stream().map(nonterminal -> nonterminal.getName()).collect(joining(", "));
                System.out.print(cellContent);
                int fill=0;



                fill=spacing-cellContent.length()-2;

                for(int k=0;k< fill;k++) {
                    System.out.print(" ");
                }
                System.out.print(" | ");
            }
            System.out.println("");
            horizontalLine();        }
    }
    public void printWithWord() {
        this.print();
        System.out.print("| j | ");
        for(int i=0;i<word.length();i++) {
            System.out.print(word.charAt(i));
            for(int j=0;j<spacing-2;j++) {
                System.out.print(" ");
            }
            System.out.print("| ");
        }
        System.out.println("");
        horizontalLine();
    }
    private void horizontalLine() {
        System.out.print("+---");
        for(int i=1;i<columns;i++) {
            System.out.print("+");
            for(int j=0;j<this.spacing;j++) {
                System.out.print("-");
            }
        }
        System.out.println("+");

    }
    public HashSet getCell(int c, int r) {
        return matrix[r][c];
    }
    public void clearCell(int c, int r) {
        matrix[r][c]=new HashSet<>();
    }

}

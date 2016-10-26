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
    private HashSet<Nonterminal>[][] matrix;
    int max;
    public Matrix(int rows, int columns) {
        matrix=new HashSet[rows][columns];
        this.rows=rows;
        this.columns=columns;
        for(int j=0;j<matrix.length;j++) {
            for (int i = 0; i < matrix[j].length; i++) {
                matrix[j][i]=new HashSet<>();
            }
        }
       this.max=0;
        for(int j=0;j<matrix.length;j++) {
            for(int i=0;i<matrix[j].length;i++) {
                if(matrix[j][i].size()>max) {
                    max=matrix[j][i].size();
                }
            }
        }

    }
    public void addToCell(int c, int r, Nonterminal nt) {
        matrix[r][c].add(nt);
    }
    public void print() {
        int max=0;
        for(int j=0;j<matrix.length;j++) {
            for(int i=0;i<matrix[j].length;i++) {
                if(matrix[j][i].size()>max) {
                    max=matrix[j][i].size();
                }
            }
        }
        int pacing=3*max-2;
        String space = new String(new char[pacing-1]).replace('\0', ' ');
        //first line
        String firstLine="| i | "+IntStream.rangeClosed(1,columns-1).mapToObj(i -> ((Integer) i).toString()).collect(joining(space+" | "))+space+" |";
        Matrix.horizontalLine(firstLine.length(),pacing);
        System.out.println(firstLine);
        Matrix.horizontalLine(firstLine.length(),pacing);
        for(int j=0;j<rows;j++) {
            System.out.print("| "+(rows-j-1)+" | ");
            for(int i=1;i<columns;i++) {
                HashSet<Nonterminal> tmp=matrix[j][i];

                System.out.print(tmp.stream().map(nonterminal -> nonterminal.getName()).collect(joining(", ")));
                for(int k=0;k< pacing-tmp.size()*3+2;k++) {
                    System.out.print(" ");
                }
                System.out.print(" | ");
            }
            System.out.println("");
            Matrix.horizontalLine(firstLine.length(),pacing);        }
    }
    private static void horizontalLine(int length,int pacing) {
        int i=0;
        System.out.print("----");
        while(i+pacing+2<length) {
            System.out.print("+");
            for(int j=0;j<pacing+2;j++) {
                System.out.print("-");
            }
            i+=pacing+2;
        }
        System.out.println("+");
    }
    public static Matrix test(int c, int r) {
        Matrix res=new Matrix(r,c);
        for(int i=0;i<c;i++) {
            for(int j=0;j<r;j++) {
                res.addToCell(i,j,new Nonterminal("A",null));
            }
        }
        res.addToCell(0,0,new Nonterminal("T",null));
        res.addToCell(0,0,new Nonterminal("S",null));
        res.addToCell(0,0,new Nonterminal("R",null));
        res.addToCell(c-1,r-1,new Nonterminal("T",null));
        res.addToCell(c-1,r-1,new Nonterminal("S",null));
        res.addToCell(1,1,new Nonterminal("R",null));
        return res;
    }
}

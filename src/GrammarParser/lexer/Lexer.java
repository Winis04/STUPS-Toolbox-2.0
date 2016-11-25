/* This file was generated by SableCC (http://www.sablecc.org/). */

package GrammarParser.lexer;

import java.io.*;
import java.util.*;
import GrammarParser.node.*;
import de.hhu.stups.sablecc.patch.*;
import java.util.concurrent.LinkedBlockingQueue;

@SuppressWarnings({"unused"}) 
public class Lexer implements ITokenListContainer
{
    protected Token token;
    protected State state = State.INITIAL;

    private PushbackReader in;
    private int line;
    private int pos;
    private boolean cr;
    private boolean eof;
    private final StringBuffer text = new StringBuffer();
    
	private List<IToken> tokenList;
    private final Queue<IToken> nextList = new LinkedBlockingQueue<IToken>();

	private IToken tok;

    public Queue<IToken> getNextList() {
        return nextList;
    }

	public List<IToken> getTokenList() {
		return tokenList;
	}
	
	private void setToken(Token t) {
	  tok = t;
   	  token = t;	
	}
	
	
	public void setTokenList(final List<IToken> list) {
		tokenList = list;
	}

     
    protected void filter() throws LexerException, IOException
    {
        // Do nothing
    }

    protected void filterWrap() throws LexerException, IOException
    {
       filter();
       if (token != null) {
	          getTokenList().add(token); 
              nextList.add(token);
	   }
    }


    public Lexer(  PushbackReader in)
    {
        this.in = in;
    	setTokenList(new ArrayList<IToken>());
    }
    
    public Token peek() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.setToken(getToken());
            filterWrap();
        }

        return (Token) nextList.peek();
    }

    public Token next() throws LexerException, IOException
    {
        while(this.token == null)
        {
            this.setToken(getToken());
            filterWrap();
        }

        Token result = (Token) nextList.poll();
        this.setToken(null);
        return result;
    }

    protected Token getToken() throws IOException, LexerException
    {
        int dfa_state = 0;

        int start_pos = this.pos;
        int start_line = this.line;

        int accept_state = -1;
        int accept_token = -1;
        int accept_length = -1;
        int accept_pos = -1;
        int accept_line = -1;

          int[][][] gotoTable = Lexer.gotoTable[this.state.id()];
          int[] accept = Lexer.accept[this.state.id()];
        this.text.setLength(0);

        while(true)
        {
            int c = getChar();

            if(c != -1)
            {
                switch(c)
                {
                case 10:
                    if(this.cr)
                    {
                        this.cr = false;
                    }
                    else
                    {
                        this.line++;
                        this.pos = 0;
                    }
                    break;
                case 13:
                    this.line++;
                    this.pos = 0;
                    this.cr = true;
                    break;
                default:
                    this.pos++;
                    this.cr = false;
                    break;
                }

                this.text.append((char) c);

                do
                {
                    int oldState = (dfa_state < -1) ? (-2 -dfa_state) : dfa_state;

                    dfa_state = -1;

                    int[][] tmp1 =  gotoTable[oldState];
                    int low = 0;
                    int high = tmp1.length - 1;

                    while(low <= high)
                    {
                        int middle = (low + high) / 2;
                        int[] tmp2 = tmp1[middle];

                        if(c < tmp2[0])
                        {
                            high = middle - 1;
                        }
                        else if(c > tmp2[1])
                        {
                            low = middle + 1;
                        }
                        else
                        {
                            dfa_state = tmp2[2];
                            break;
                        }
                    }
                }while(dfa_state < -1);
            }
            else
            {
                dfa_state = -1;
            }

            if(dfa_state >= 0)
            {
                if(accept[dfa_state] != -1)
                {
                    accept_state = dfa_state;
                    accept_token = accept[dfa_state];
                    accept_length = this.text.length();
                    accept_pos = this.pos;
                    accept_line = this.line;
                }
            }
            else
            {
                if(accept_state != -1)
                {
                    switch(accept_token)
                    {
                    case 0:
                        {
                              Token token = new0(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 1:
                        {
                              Token token = new1(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 2:
                        {
                              Token token = new2(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 3:
                        {
                              Token token = new3(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 4:
                        {
                              Token token = new4(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 5:
                        {
                              Token token = new5(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 6:
                        {
                              Token token = new6(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 7:
                        {
                              Token token = new7(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 8:
                        {
                              Token token = new8(
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 9:
                        {
                              Token token = new9(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    case 10:
                        {
                              Token token = new10(
                                getText(accept_length),
                                start_line + 1,
                                start_pos + 1);
                            pushBack(accept_length);
                            this.pos = accept_pos;
                            this.line = accept_line;
                            return token;
                        }
                    }
                }
                else
                {
                    if(this.text.length() > 0)
                    {
                        throw new LexerException(
                            "[" + (start_line + 1) + "," + (start_pos + 1) + "]" +
                            " Unknown token: " + this.text);
                    }

                      EOF token = new EOF(
                        start_line + 1,
                        start_pos + 1);
                    return token;
                }
            }
        }
    }

    Token new0(  String text,   int line,   int pos) { return new TWhitespace(text, line, pos); }
    Token new1(  int line,   int pos) { return new TArrow(line, pos); }
    Token new2(  int line,   int pos) { return new TLBracket(line, pos); }
    Token new3(  int line,   int pos) { return new TRBracket(line, pos); }
    Token new4(  int line,   int pos) { return new TLBracket2(line, pos); }
    Token new5(  int line,   int pos) { return new TRBracket2(line, pos); }
    Token new6(  int line,   int pos) { return new TSemicolon(line, pos); }
    Token new7(  int line,   int pos) { return new TComma(line, pos); }
    Token new8(  int line,   int pos) { return new TLine(line, pos); }
    Token new9(  String text,   int line,   int pos) { return new TIdentifier(text, line, pos); }
    Token new10(  String text,   int line,   int pos) { return new TSymbol(text, line, pos); }

    private int getChar() throws IOException
    {
        if(this.eof)
        {
            return -1;
        }

        int result = this.in.read();

        if(result == -1)
        {
            this.eof = true;
        }

        return result;
    }

    private void pushBack(int acceptLength) throws IOException
    {
        int length = this.text.length();
        for(int i = length - 1; i >= acceptLength; i--)
        {
            this.eof = false;

            this.in.unread(this.text.charAt(i));
        }
    }

    protected void unread(  Token token) throws IOException
    {
          String text = token.getText();
        int length = text.length();

        for(int i = length - 1; i >= 0; i--)
        {
            this.eof = false;

            this.in.unread(text.charAt(i));
        }

        this.pos = token.getPos() - 1;
        this.line = token.getLine() - 1;
    }

    private String getText(int acceptLength)
    {
        StringBuffer s = new StringBuffer(acceptLength);
        for(int i = 0; i < acceptLength; i++)
        {
            s.append(this.text.charAt(i));
        }

        return s.toString();
    }

    private static int[][][][] gotoTable;
/*  {
        { // INITIAL
            {{9, 9, 1}, {10, 10, 2}, {13, 13, 3}, {32, 32, 4}, {39, 39, 5}, {40, 40, 6}, {41, 41, 7}, {44, 44, 8}, {45, 45, 9}, {59, 59, 10}, {65, 90, 11}, {95, 95, 12}, {97, 122, 13}, {123, 123, 14}, {124, 124, 15}, {125, 125, 16}, },
            {{9, 32, -2}, },
            {{9, 32, -2}, },
            {{9, 32, -2}, },
            {{9, 32, -2}, },
            {{0, 38, 17}, {40, 65535, 17}, },
            {},
            {},
            {},
            {{45, 45, 18}, },
            {},
            {{48, 57, 19}, {65, 90, 20}, {95, 95, 21}, {97, 122, 22}, },
            {{48, 122, -13}, },
            {{48, 122, -13}, },
            {},
            {},
            {},
            {{0, 38, 17}, {39, 39, 23}, {40, 65535, 17}, },
            {{62, 62, 24}, },
            {{48, 122, -13}, },
            {{48, 122, -13}, },
            {{48, 122, -13}, },
            {{48, 122, -13}, },
            {},
            {},
        }
    };*/

    private static int[][] accept;
/*  {
        // INITIAL
        {-1, 0, 0, 0, 0, -1, 2, 3, 7, -1, 6, 9, 9, 9, 4, 8, 5, -1, -1, 9, 9, 9, 9, 10, 1, },

    };*/

    public static class State
    {
        public final static State INITIAL = new State(0);

        private int id;

        private State(  int id)
        {
            this.id = id;
        }

        public int id()
        {
            return this.id;
        }
    }

    static 
    {
        try
        {
            DataInputStream s = new DataInputStream(
                new BufferedInputStream(
                Lexer.class.getResourceAsStream("lexer.dat")));

            // read gotoTable
            int length = s.readInt();
            gotoTable = new int[length][][][];
            for(int i = 0; i < gotoTable.length; i++)
            {
                length = s.readInt();
                gotoTable[i] = new int[length][][];
                for(int j = 0; j < gotoTable[i].length; j++)
                {
                    length = s.readInt();
                    gotoTable[i][j] = new int[length][3];
                    for(int k = 0; k < gotoTable[i][j].length; k++)
                    {
                        for(int l = 0; l < 3; l++)
                        {
                            gotoTable[i][j][k][l] = s.readInt();
                        }
                    }
                }
            }

            // read accept
            length = s.readInt();
            accept = new int[length][];
            for(int i = 0; i < accept.length; i++)
            {
                length = s.readInt();
                accept[i] = new int[length];
                for(int j = 0; j < accept[i].length; j++)
                {
                    accept[i][j] = s.readInt();
                }
            }

            s.close();
        }
        catch(Exception e)
        {
            throw new RuntimeException("The file \"lexer.dat\" is either missing or corrupted.");
        }
    }
}
